package frc.team7170.buttonboard;

import com.pi4j.io.gpio.*;
import com.pi4j.io.spi.SpiChannel;
import com.rrr.mcp23s17.MCP23S17;
import edu.wpi.first.networktables.*;

import java.io.IOException;
import java.util.*;

// TODO: random lights show, robot side of comm
public class Main {

    private static class Command {

        private final String name;
        private final boolean hold;

        private Command(String name, boolean hold) {
            this.name = name;
            this.hold = hold;
        }
    }

    private static class BlinkerThread extends Thread {

        private static final int DELAY_MS = 500;  // Half cycle (i.e. from high-to-low or low-to-high).
        private static final int NUM_BLINKS = 4;  // Number of full cycles (i.e. from high to low to high).

        private final MCP23S17.Pin pin;

        private BlinkerThread(MCP23S17.Pin pin) {
            setDaemon(true);
            this.pin = pin;
        }

        @Override
        public void run() {
            MCP23S17.PinView pinView = leds.getPinView(pin);
            try {
                for (int i = 0; i < NUM_BLINKS; ++i) {
                    pinView.set(false);
                    writeCorrespondingOLAT(pin);
                    Thread.sleep(DELAY_MS);

                    pinView.set(true);
                    writeCorrespondingOLAT(pin);
                    Thread.sleep(DELAY_MS);
                }
            } catch (IOException e) {
                System.err.println("error while blinking LED");
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                // Blinker threads should never be interrupted.
                throw new AssertionError(e);
            }
        }
    }

    private static final Map<MCP23S17.Pin, Command> commandMap;

    static {
        Map<MCP23S17.Pin, Command> map = new HashMap<>();
        map.put(MCP23S17.Pin.PIN0, new Command("elevatorLevel1", false));
        map.put(MCP23S17.Pin.PIN1, new Command("elevatorLevel2", false));
        map.put(MCP23S17.Pin.PIN2, new Command("elevatorLevel3", false));
        map.put(MCP23S17.Pin.PIN3, new Command("frontArmsVertical", false));
        map.put(MCP23S17.Pin.PIN4, new Command("frontArmsHorizontal", false));
        map.put(MCP23S17.Pin.PIN5, new Command("eject", false));
        map.put(MCP23S17.Pin.PIN6, new Command("pinToggle", false));
        map.put(MCP23S17.Pin.PIN7, new Command("leftDriveWheelsForward", true));
        map.put(MCP23S17.Pin.PIN8, new Command("leftDriveWheelsReverse", true));
        map.put(MCP23S17.Pin.PIN9, new Command("rightDriveWheelsForward", true));
        map.put(MCP23S17.Pin.PIN10, new Command("rightDriveWheelsReverse", true));
        map.put(MCP23S17.Pin.PIN11, new Command("linearActuatorRetract", true));
        map.put(MCP23S17.Pin.PIN12, new Command("linearActuatorExtend", true));
        map.put(MCP23S17.Pin.PIN13, new Command("spinClimbDriveWheels", true));
        map.put(MCP23S17.Pin.PIN14, new Command("shuffleLateralSlide", false));
        map.put(MCP23S17.Pin.PIN15, new Command("random", false));
        commandMap = Collections.unmodifiableMap(map);
    }

    private static MCP23S17 leds;
    private static MCP23S17 buttons;
    private static NetworkTableEntry pressedEntry;
    private static NetworkTableEntry releasedEntry;
    private static final List<String> pressedCache = new ArrayList<>();
    private static final List<String> releasedCache = new ArrayList<>();
    // TODO: doc -- this map is just so the threads dont get GCed
    private static final Map<MCP23S17.Pin, BlinkerThread> blinkerThreads = new HashMap<>();

    // Timings on busy loop pin-toggling:
    // period: ~1.2 us
    // rise time: <15 ns
    public static void main(String... args) {
        // Initialize IO Expanders.
        {
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
        }

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
            System.out.print("Waiting for robot connection...");
            instance.startClientTeam(7170);
            try {
                while (!instance.isConnected()) {
                    Thread.sleep(100);
                }
            } catch (InterruptedException e) {
                // Main thread should never be interrupted.
                throw new AssertionError(e);
            }
            System.out.println("connected!");
            pressedEntry = instance.getEntry("buttonBoardPressed");
            releasedEntry = instance.getEntry("buttonBoardReleased");
        }

        pressedEntry.setStringArray(new String[0]);  // Set type.
        releasedEntry.setStringArray(new String[0]);  // Set type.
        pressedEntry.addListener(Main::onPressedEntryCleared, EntryListenerFlags.kUpdate);
        releasedEntry.addListener(Main::onReleasedEntryCleared, EntryListenerFlags.kUpdate);
        buttons.addGlobalListener(Main::onButtonUpdate);
        System.out.println("Awaiting button presses...");

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

    private static void onButtonUpdate(boolean state, MCP23S17.Pin pin) {
        Command command = commandMap.get(pin);
        if (state) {
            System.out.println(String.format("Button %d released.", pin.getPinNumber()));

            // Issue the command.
            pressedCache.add(command.name);
            if (pressedEntry.getValue().getStringArray().length == 0) {
                flushPressed();
            }

            // Handle LEDs.
            if (command.hold) {
                setLED(pin, true);
            }
        } else {
            System.out.println(String.format("Button %d pressed.", pin.getPinNumber()));

            // Issue the command.
            releasedCache.add(command.name);
            if (releasedEntry.getValue().getStringArray().length == 0) {
                flushReleased();
            }

            // Handle LEDs.
            if (command.hold) {
                setLED(pin, false);
            } else {
                BlinkerThread blinkerThread = new BlinkerThread(pin);
                blinkerThread.start();
                blinkerThreads.put(pin, blinkerThread);
            }
        }
    }

    private static void setLED(MCP23S17.Pin pin, boolean value) {
        try {
            leds.getPinView(pin).set(value);
            writeCorrespondingOLAT(pin);
        } catch (IOException e) {
            System.err.println("error while setting LED");
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("SynchronizeOnNonFinalField")
    private static void writeCorrespondingOLAT(MCP23S17.Pin pin) throws IOException {
        // Register writes on MCP23S17 are not thread-safe--external synchronization required here.
        synchronized (leds) {
            if (pin.isPortA()) {
                leds.writeOLATA();
            } else {
                leds.writeOLATB();
            }
        }
    }
}
