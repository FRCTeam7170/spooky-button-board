package frc.team7170.buttonboard;

import com.rrr.mcp23s17.MCP23S17;

public enum Command {
    ELEVATOR_LEVEL_1(MCP23S17.Pin.PIN4),
    ELEVATOR_LEVEL_2(MCP23S17.Pin.PIN5),
    ELEVATOR_LEVEL_3(MCP23S17.Pin.PIN6),
    FRONT_ARMS_VERTICAL(MCP23S17.Pin.PIN9),
    FRONT_ARMS_HORIZONTAL(MCP23S17.Pin.PIN8),
    EJECT(MCP23S17.Pin.PIN1),
    PIN_TOGGLE(MCP23S17.Pin.PIN0),
    LEFT_DRIVE_WHEELS_FORWARD(MCP23S17.Pin.PIN14),
    LEFT_DRIVE_WHEELS_REVERSE(MCP23S17.Pin.PIN15),
    RIGHT_DRIVE_WHEELS_FORWARD(MCP23S17.Pin.PIN12),
    RIGHT_DRIVE_WHEELS_REVERSE(MCP23S17.Pin.PIN13),
    LINEAR_ACTUATOR_RETRACT(MCP23S17.Pin.PIN11),
    LINEAR_ACTUATOR_EXTEND(MCP23S17.Pin.PIN10),
    SPIN_CLIMB_DRIVE_WHEELS(MCP23S17.Pin.PIN7),
    SHUFFLE_LATERAL_SLIDE(MCP23S17.Pin.PIN3);

    private final MCP23S17.Pin boundPin;

    Command(MCP23S17.Pin boundPin) {
        this.boundPin = boundPin;
    }

    public MCP23S17.Pin getBoundPin() {
        return boundPin;
    }
}