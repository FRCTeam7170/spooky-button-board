package frc.team7170.buttonboard;

public class DeterministicCommandResolver implements CommandResolver {

    private final Command command;

    public DeterministicCommandResolver(Command command) {
        this.command = command;
    }

    @Override
    public Command resolveNext() {
        return command;
    }
}
