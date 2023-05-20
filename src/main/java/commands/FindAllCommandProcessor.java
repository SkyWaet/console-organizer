package commands;

import model.Command;
import storage.PersonStorage;

import java.io.PrintStream;
import java.util.Scanner;

public class FindAllCommandProcessor extends AbstractCommandProcessor {
    public FindAllCommandProcessor(PersonStorage storage, Scanner reader, PrintStream writer) {
        super(storage, reader, writer);
    }

    @Override
    public Command getCommandType() {
        return Command.FIND_ALL;
    }

    @Override
    public void processCommand() {
        writer.println("Processing \"find-all\" command.");
        storage.findAll().forEach(writer::println);
        writer.println("=================================");
    }


}
