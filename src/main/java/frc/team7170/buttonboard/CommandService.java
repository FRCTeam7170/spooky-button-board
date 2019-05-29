package frc.team7170.buttonboard;

public class CommandService {

    private final CommandResolver commandResolver;
    private final DisplayHandler displayHandler;
    private Command lastCommand;

    public CommandService(CommandResolver commandResolver, DisplayHandler displayHandler) {
        this.commandResolver = commandResolver;
        this.displayHandler = displayHandler;
    }

    public Command getForPress() {
        lastCommand = commandResolver.resolveNext();
        displayHandler.buttonPressed(lastCommand.getBoundPin());
        return lastCommand;
    }

    public Command getForRelease() {
        displayHandler.buttonReleased(lastCommand.getBoundPin());
        return lastCommand;
    }
}
