package frc.team7170.buttonboard;

import com.pi4j.io.gpio.*;
import com.pi4j.io.spi.SpiChannel;
import com.rrr.mcp23s17.MCP23S17;
import edu.wpi.first.networktables.*;

import java.io.IOException;
import java.util.*;

// TODO: random lights show, robot side of comm
public class Main {

    private static final Map<MCP23S17.Pin, String> commandMap;

    static {
        Map<MCP23S17.Pin, String> map = new HashMap<>();
        map.put(MCP23S17.Pin.PIN0, "elevatorLevel1");
        map.put(MCP23S17.Pin.PIN1, "elevatorLevel2");
        map.put(MCP23S17.Pin.PIN2, "elevatorLevel3");
        map.put(MCP23S17.Pin.PIN3, "frontArmsVertical");
        map.put(MCP23S17.Pin.PIN4, "frontArmsHorizontal");
        map.put(MCP23S17.Pin.PIN5, "eject");
        map.put(MCP23S17.Pin.PIN6, "pinToggle");
        map.put(MCP23S17.Pin.PIN7, "leftDriveWheelsForward");
        map.put(MCP23S17.Pin.PIN8, "leftDriveWheelsReverse");
        map.put(MCP23S17.Pin.PIN9, "rightDriveWheelsForward");
        map.put(MCP23S17.Pin.PIN10, "rightDriveWheelsReverse");
        map.put(MCP23S17.Pin.PIN11, "linearActuatorRetract");
        map.put(MCP23S17.Pin.PIN12, "linearActuatorExtend");
        map.put(MCP23S17.Pin.PIN13, "spinClimbDriveWheels");
        map.put(MCP23S17.Pin.PIN14, "shuffleLateralSlide");
        map.put(MCP23S17.Pin.PIN15, "random");
        commandMap = Collections.unmodifiableMap(map);
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
                iter.next().setAsOutput();
            }
            leds.writeIODIRA();
            leds.writeIODIRB();

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
        if (state) {
            System.out.println(String.format("Button %d released.", pin.getPinNumber()));
            pressedCache.add(commandMap.get(pin));
            if (pressedEntry.getValue().getStringArray().length == 0) {
                flushPressed();
            }
        } else {
            System.out.println(String.format("Button %d pressed.", pin.getPinNumber()));
            releasedCache.add(commandMap.get(pin));
            if (releasedEntry.getValue().getStringArray().length == 0) {
                flushReleased();
            }
        }
    }
}
