package frc.team7170.buttonboard;

import java.util.Random;

public class RandomCommandResolver implements CommandResolver {

    private final Random random = new Random();
    private final Command[] commandChoices;

    public RandomCommandResolver(Command... commandChoices) {
        this.commandChoices = commandChoices;
    }

    @Override
    public Command resolveNext() {
        return commandChoices[random.nextInt(commandChoices.length)];
    }
}
