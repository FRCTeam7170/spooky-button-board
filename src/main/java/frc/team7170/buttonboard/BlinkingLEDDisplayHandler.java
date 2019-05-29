package frc.team7170.buttonboard;

import com.rrr.mcp23s17.MCP23S17;

import java.util.function.BiConsumer;

public class BlinkingLEDDisplayHandler implements DisplayHandler {

    private static final int DELAY_MS = 500;  // Half cycle (i.e. from high-to-low or low-to-high).
    private static final int NUM_BLINKS = 4;  // Number of full cycles (i.e. from high to low to high).

    private class BlinkerThread extends Thread {

        private final MCP23S17.Pin pin;

        private BlinkerThread(MCP23S17.Pin pin) {
            this.pin = pin;
            setDaemon(true);
        }

        @Override
        public void run() {
            try {
                for (int i = 0; i < NUM_BLINKS; ++i) {
                    ledSetter.accept(pin, true);
                    Thread.sleep(DELAY_MS);

                    ledSetter.accept(pin, false);
                    Thread.sleep(DELAY_MS);
                }
            } catch (InterruptedException e) {
                ledSetter.accept(pin, false);
            }
        }
    }

    private final BiConsumer<MCP23S17.Pin, Boolean> ledSetter;
    // TODO: doc -- ref only kept to avoid GC
    private BlinkerThread blinkerThread;

    public BlinkingLEDDisplayHandler(BiConsumer<MCP23S17.Pin, Boolean> ledSetter) {
        this.ledSetter = ledSetter;
    }

    @Override
    public void buttonPressed(MCP23S17.Pin ledPin) {
        if (blinkerThread != null && blinkerThread.isAlive()) {
            blinkerThread.interrupt();
            try {
                blinkerThread.join();
            } catch (InterruptedException e) {
                // GPIO event thread should never be interrupted (I think).
                throw new AssertionError(e);
            }
        }
        blinkerThread = new BlinkerThread(ledPin);
        blinkerThread.start();
    }

    @Override
    public void buttonReleased(MCP23S17.Pin ledPin) {

    }
}
