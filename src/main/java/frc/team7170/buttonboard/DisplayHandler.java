package frc.team7170.buttonboard;

import com.rrr.mcp23s17.MCP23S17;

public interface DisplayHandler {

    void buttonPressed(MCP23S17.Pin ledPin);

    void buttonReleased(MCP23S17.Pin ledPin);
}
