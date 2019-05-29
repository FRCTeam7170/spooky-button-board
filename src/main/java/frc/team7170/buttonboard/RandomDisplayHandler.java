package frc.team7170.buttonboard;

import com.pi4j.io.gpio.GpioPinPwmOutput;
import com.rrr.mcp23s17.MCP23S17;

import java.util.Random;
import java.util.function.BiConsumer;

public class RandomDisplayHandler implements DisplayHandler {

    private static final int BUZZER_PWM_VALUE_RANDOM = 500;  // [0, 1023]
    private static final int NUM_RANDOM_JUMPS = 15;
    // Total period is 500 ms, buzzer on for half that, LED on for all of it.
    private static final int RANDOM_ON_TIME_MS = 250;
    private static final int RANDOM_OFF_TIME_MS = 250;
    private static final int BUZZER_PWM_VALUE_FINAL = 800;  // [0, 1023]
    private static final int NUM_FINAL_BLINKS = 3;
    private static final int FINAL_ON_TIME_MS = 250;
    private static final int FINAL_OFF_TIME_MS = 100;

    private final BiConsumer<MCP23S17.Pin, Boolean> ledSetter;
    private final GpioPinPwmOutput buzzer;
    private final Random random = new Random();

    public RandomDisplayHandler(BiConsumer<MCP23S17.Pin, Boolean> ledSetter, GpioPinPwmOutput buzzer) {
        this.ledSetter = ledSetter;
        this.buzzer = buzzer;
    }

    @Override
    public void buttonPressed(MCP23S17.Pin ledPin) {
        MCP23S17.Pin[] pins = MCP23S17.Pin.values();
        int last = 0;
        try {
            for (int i = 0; i < NUM_RANDOM_JUMPS; ++i) {
                // This never selects 0 as the first pin... not ideal.
                int randomPinNumber = last;
                while (randomPinNumber == last) {
                    randomPinNumber = random.nextInt(pins.length);
                }
                last = randomPinNumber;
                MCP23S17.Pin randomPin = pins[randomPinNumber];
                ledSetter.accept(randomPin, true);
                buzzer.setPwm(BUZZER_PWM_VALUE_RANDOM);
                Thread.sleep(RANDOM_ON_TIME_MS);
                buzzer.setPwm(0);
                Thread.sleep(RANDOM_OFF_TIME_MS);
                ledSetter.accept(randomPin, false);
            }
            for (int i = 0; i < NUM_FINAL_BLINKS; ++i) {
                ledSetter.accept(ledPin, true);
                buzzer.setPwm(BUZZER_PWM_VALUE_FINAL);
                Thread.sleep(FINAL_ON_TIME_MS);
                ledSetter.accept(ledPin, false);
                buzzer.setPwm(0);
                Thread.sleep(FINAL_OFF_TIME_MS);
            }
        } catch (InterruptedException e) {
            // GPIO event thread should never be interrupted (I think).
            throw new AssertionError(e);
        }
    }

    @Override
    public void buttonReleased(MCP23S17.Pin ledPin) {}
}
