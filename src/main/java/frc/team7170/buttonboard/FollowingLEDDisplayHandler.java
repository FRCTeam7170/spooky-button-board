package frc.team7170.buttonboard;

import com.rrr.mcp23s17.MCP23S17;

import java.util.function.BiConsumer;

public class FollowingLEDDisplayHandler implements DisplayHandler {

    private final BiConsumer<MCP23S17.Pin, Boolean> ledSetter;

    public FollowingLEDDisplayHandler(BiConsumer<MCP23S17.Pin, Boolean> ledSetter) {
        this.ledSetter = ledSetter;
    }

    @Override
    public void buttonPressed(MCP23S17.Pin ledPin) {
        ledSetter.accept(ledPin, true);
    }

    @Override
    public void buttonReleased(MCP23S17.Pin ledPin) {
        ledSetter.accept(ledPin, false);
    }
}
