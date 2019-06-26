package frc.team7170.buttonboard;

import com.pi4j.io.gpio.*;
import com.pi4j.io.spi.SpiChannel;
import com.rrr.mcp23s17.MCP23S17;
import edu.wpi.first.networktables.*;

import java.io.IOException;
import java.util.*;
import java.util.function.BiConsumer;

public final class ButtonBoard {

    private static final int TEAM_NUMBER = 7170;

    private static final Map<MCP23S17.Pin, CommandService> BUTTON_PIN_COMMAND_SERVICE_MAP = new HashMap<>();

    static {
        // Initialize IO Expanders.
        GpioController gpioController = GpioFactory.getInstance();
        GpioPinDigitalOutput invCSLEDs = gpioController.provisionDigitalOutputPin(RaspiPin.GPIO_03);
        GpioPinDigitalOutput invCSButtons = gpioController.provisionDigitalOutputPin(RaspiPin.GPIO_09);
        GpioPinDigitalInput interrupt = gpioController.provisionDigitalInputPin(RaspiPin.GPIO_08);
        try {
            leds = MCP23S17.newWithoutInterrupts(SpiChannel.CS0, invCSLEDs);
            buttons = MCP23S17.newWithTiedInterrupts(SpiChannel.CS0, invCSButtons, interrupt);
        } catch (IOException e) {
            System.err.println("failed IO expander initialization");
            throw new RuntimeException(e);
        }

        // Populate BUTTON_PIN_COMMAND_SERVICE_MAP.
        BiConsumer<MCP23S17.Pin, Boolean> pinSetter = ButtonBoard::setLEDUnchecked;
        BUTTON_PIN_COMMAND_SERVICE_MAP.put(MCP23S17.Pin.PIN4, new CommandService(
                new DeterministicCommandResolver(Command.ELEVATOR_LEVEL_1),
                new BlinkingLEDDisplayHandler(pinSetter))
        );
        BUTTON_PIN_COMMAND_SERVICE_MAP.put(MCP23S17.Pin.PIN5, new CommandService(
                new DeterministicCommandResolver(Command.ELEVATOR_LEVEL_2),
                new BlinkingLEDDisplayHandler(pinSetter))
        );
        BUTTON_PIN_COMMAND_SERVICE_MAP.put(MCP23S17.Pin.PIN6, new CommandService(
                new DeterministicCommandResolver(Command.ELEVATOR_LEVEL_3),
                new BlinkingLEDDisplayHandler(pinSetter))
        );
        BUTTON_PIN_COMMAND_SERVICE_MAP.put(MCP23S17.Pin.PIN9, new CommandService(
                new DeterministicCommandResolver(Command.FRONT_ARMS_VERTICAL),
                new BlinkingLEDDisplayHandler(pinSetter))
        );
        BUTTON_PIN_COMMAND_SERVICE_MAP.put(MCP23S17.Pin.PIN8, new CommandService(
                new DeterministicCommandResolver(Command.FRONT_ARMS_HORIZONTAL),
                new BlinkingLEDDisplayHandler(pinSetter))
        );
        BUTTON_PIN_COMMAND_SERVICE_MAP.put(MCP23S17.Pin.PIN1, new CommandService(
                new DeterministicCommandResolver(Command.EJECT),
                new BlinkingLEDDisplayHandler(pinSetter))
        );
        BUTTON_PIN_COMMAND_SERVICE_MAP.put(MCP23S17.Pin.PIN0, new CommandService(
                new DeterministicCommandResolver(Command.PIN_TOGGLE),
                new BlinkingLEDDisplayHandler(pinSetter))
        );
        BUTTON_PIN_COMMAND_SERVICE_MAP.put(MCP23S17.Pin.PIN14, new CommandService(
                new DeterministicCommandResolver(Command.LEFT_DRIVE_WHEELS_FORWARD),
                new FollowingLEDDisplayHandler(pinSetter))
        );
        BUTTON_PIN_COMMAND_SERVICE_MAP.put(MCP23S17.Pin.PIN15, new CommandService(
                new DeterministicCommandResolver(Command.LEFT_DRIVE_WHEELS_REVERSE),
                new FollowingLEDDisplayHandler(pinSetter))
        );
        BUTTON_PIN_COMMAND_SERVICE_MAP.put(MCP23S17.Pin.PIN12, new CommandService(
                new DeterministicCommandResolver(Command.RIGHT_DRIVE_WHEELS_FORWARD),
                new FollowingLEDDisplayHandler(pinSetter))
        );
        BUTTON_PIN_COMMAND_SERVICE_MAP.put(MCP23S17.Pin.PIN13, new CommandService(
                new DeterministicCommandResolver(Command.RIGHT_DRIVE_WHEELS_REVERSE),
                new FollowingLEDDisplayHandler(pinSetter))
        );
        BUTTON_PIN_COMMAND_SERVICE_MAP.put(MCP23S17.Pin.PIN11, new CommandService(
                new DeterministicCommandResolver(Command.LINEAR_ACTUATOR_RETRACT),
                new FollowingLEDDisplayHandler(pinSetter))
        );
        BUTTON_PIN_COMMAND_SERVICE_MAP.put(MCP23S17.Pin.PIN10, new CommandService(
                new DeterministicCommandResolver(Command.LINEAR_ACTUATOR_EXTEND),
                new FollowingLEDDisplayHandler(pinSetter))
        );
        BUTTON_PIN_COMMAND_SERVICE_MAP.put(MCP23S17.Pin.PIN7, new CommandService(
                new DeterministicCommandResolver(Command.SPIN_CLIMB_DRIVE_WHEELS),
                new FollowingLEDDisplayHandler(pinSetter))
        );
        BUTTON_PIN_COMMAND_SERVICE_MAP.put(MCP23S17.Pin.PIN2, new CommandService(
                new DeterministicCommandResolver(Command.SHUFFLE_LATERAL_SLIDE),
                new BlinkingLEDDisplayHandler(pinSetter))
        );
        BUTTON_PIN_COMMAND_SERVICE_MAP.put(MCP23S17.Pin.PIN3, new CommandService(
                new RandomCommandResolver(
                        Command.ELEVATOR_LEVEL_1,
                        Command.ELEVATOR_LEVEL_2,
                        Command.ELEVATOR_LEVEL_3,
                        Command.FRONT_ARMS_VERTICAL,
                        Command.FRONT_ARMS_HORIZONTAL,
                        Command.EJECT,
                        Command.PIN_TOGGLE,
                        Command.SHUFFLE_LATERAL_SLIDE
                ),
                new RandomDisplayHandler(pinSetter, gpioController.provisionPwmOutputPin(RaspiPin.GPIO_01)))
        );
    }

    private static MCP23S17 leds;
    private static MCP23S17 buttons;
    private static NetworkTableEntry pressedEntry;
    private static NetworkTableEntry releasedEntry;
    private static final List<String> pressedCache = new ArrayList<>();
    private static final List<String> releasedCache = new ArrayList<>();

    // Timings on busy loop pin-toggling:
    // period: ~1.2 us
    // rise time: <15 ns
    public static void main(String... args) {
        // Configure IO Expanders.
        try {
            // Configure LEDs IO Expander.
            for (Iterator<MCP23S17.PinView> iter = leds.getPinViewIterator(); iter.hasNext();) {
                MCP23S17.PinView pinView = iter.next();
                pinView.setAsOutput();
                pinView.set(true);
            }
            leds.writeIODIRA();
            leds.writeIODIRB();
            leds.writeOLATA();
            leds.writeOLATB();

            // Configure buttons IO Expander.
            for (Iterator<MCP23S17.PinView> iter = buttons.getPinViewIterator(); iter.hasNext();) {
                MCP23S17.PinView pinView = iter.next();
                pinView.enableInterrupt();
//                pinView.setDefaultComparisonValue(true);
//                pinView.toInterruptComparisonMode();
                pinView.enablePullUp();
            }
            buttons.writeGPPUA();
            buttons.writeGPPUB();
//            buttons.writeDEFVALA();
//            buttons.writeDEFVALB();
//            buttons.writeINTCONA();
//            buttons.writeINTCONB();
            buttons.writeGPINTENA();
            buttons.writeGPINTENB();
        } catch (IOException e) {
            System.err.println("failed IO expander configuration");
            throw new RuntimeException(e);
        }

        // Initialize network tables.
        {
            NetworkTableInstance instance = NetworkTableInstance.getDefault();
            System.out.println("Waiting for robot connection...");
            instance.startClientTeam(TEAM_NUMBER);
            try {
                // Block until NT connects.
                while (!instance.isConnected()) {
                    Thread.sleep(100);
                }
            } catch (InterruptedException e) {
                // Main thread should never be interrupted.
                throw new AssertionError(e);
            }
            System.out.println("Connected!");
            pressedEntry = instance.getEntry("buttonBoardPressed");
            releasedEntry = instance.getEntry("buttonBoardReleased");
        }

        pressedEntry.setStringArray(new String[0]);  // Set type.
        releasedEntry.setStringArray(new String[0]);  // Set type.
        pressedEntry.addListener(ButtonBoard::onPressedEntryCleared, EntryListenerFlags.kUpdate);
        releasedEntry.addListener(ButtonBoard::onReleasedEntryCleared, EntryListenerFlags.kUpdate);
        buttons.addGlobalListener(ButtonBoard::onButtonUpdate);
        System.out.println("Awaiting button presses...");

        // For testing.
//        try (Scanner scanner = new Scanner(System.in)) {
//            Random random = new Random();
//            MCP23S17.Pin pin = null;
//            while (true) {
//                scanner.nextLine();
//                int pinNumber = random.nextInt(16);
//                if (pin != null) {
//                    setLEDUnchecked(pin, false);
//                }
//                pin = MCP23S17.Pin.fromPinNumber(pinNumber);
//                setLEDUnchecked(pin, true);
//                System.out.println(pinNumber);
//                if (false) {
//                    break;
//                }
//            }
//        }

        try {
            // Nothing else to do in main thread; we're just waiting for interrupts now.
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            // Main thread should never be interrupted.
            throw new AssertionError(e);
        }
    }

    private static void onPressedEntryCleared(EntryNotification notification) {
        if (notification.value.getStringArray().length != 0) {
            throw new RuntimeException("stream protocol violated: entry updated to non-empty array");
        }
        flushPressed();
    }

    private static void onReleasedEntryCleared(EntryNotification notification) {
        if (notification.value.getStringArray().length != 0) {
            throw new RuntimeException("stream protocol violated: entry updated to non-empty array");
        }
        flushReleased();
    }

    private static void flushPressed() {
        if (!pressedCache.isEmpty()) {
            pressedEntry.setStringArray((String[]) pressedCache.toArray());
            pressedCache.clear();
        }
    }

    private static void flushReleased() {
        if (!releasedCache.isEmpty()) {
            releasedEntry.setStringArray((String[]) releasedCache.toArray());
            releasedCache.clear();
        }
    }

    private static void postPressedCommand(Command command) {
        pressedCache.add(command.name());
        if (pressedEntry.getValue().getStringArray().length == 0) {
            flushPressed();
        }
    }

    private static void postReleasedCommand(Command command) {
        releasedCache.add(command.name());
        if (releasedEntry.getValue().getStringArray().length == 0) {
            flushReleased();
        }
    }

    private static void onButtonUpdate(boolean state, MCP23S17.Pin pin) {
        CommandService commandService = BUTTON_PIN_COMMAND_SERVICE_MAP.get(pin);
        if (state) {
            System.out.println(String.format("Button %d released.", pin.getPinNumber()));
            postReleasedCommand(commandService.getForRelease());
        } else {
            System.out.println(String.format("Button %d pressed.", pin.getPinNumber()));
            postPressedCommand(commandService.getForPress());
        }
    }

    @SuppressWarnings("SynchronizeOnNonFinalField")
    private static void setLEDUnchecked(MCP23S17.Pin pin, boolean state) {
        try {
            // LEDs default to on, hence the negation here.
            leds.getPinView(pin).set(!state);
            // Register writes on MCP23S17 are not thread-safe--external synchronization required here.
            synchronized (leds) {
                if (pin.isPortA()) {
                    leds.writeOLATA();
                } else {
                    leds.writeOLATB();
                }
            }
        } catch (IOException e) {
            System.err.println("failed to set LED");
            throw new RuntimeException(e);
        }
    }
}
