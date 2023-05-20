package commands;

import model.Command;
import storage.PersonStorage;

import java.io.PrintStream;
import java.util.Scanner;

public class ExitCommandProcessor extends AbstractCommandProcessor {
    public ExitCommandProcessor(PersonStorage storage, Scanner reader, PrintStream writer) {
        super(storage, reader, writer);
    }

    @Override
    public Command getCommandType() {
        return Command.EXIT;
    }

    @Override
    public void processCommand() {
        //do nothing
    }
}
