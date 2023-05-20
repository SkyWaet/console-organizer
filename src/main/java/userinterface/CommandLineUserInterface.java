package userinterface;

import commands.*;
import model.Command;
import storage.PersonStorage;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.EnumMap;
import java.util.Map;
import java.util.Scanner;

public class CommandLineUserInterface implements UserInterface {

    private final Scanner reader;
    private final PrintStream writer;

    private final Map<Command, CommandProcessor> commandProcessors = new EnumMap<>(Command.class);

    public CommandLineUserInterface(PersonStorage storage, InputStream readFrom, OutputStream writeTo) {
        this.reader = new Scanner(readFrom);
        this.writer = new PrintStream(writeTo);
        commandProcessors.put(Command.ADD, new AddCommandProcessor(storage, reader, writer));
        commandProcessors.put(Command.EDIT, new EditCommandProcessor(storage, reader, writer));
        commandProcessors.put(Command.FIND_ALL, new FindAllCommandProcessor(storage, reader, writer));
        commandProcessors.put(Command.FIND_ONE, new FindOneCommandProcessor(storage, reader, writer));
        commandProcessors.put(Command.EXIT, new ExitCommandProcessor(storage, reader, writer));
    }

    @Override
    public void interactWithUser() {
        try {
            greetUser();
            printMenu();
            processCommands();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void greetUser() throws IOException {
        writer.println("Welcome to console organizer application!");
    }

    private void printMenu() throws IOException {
        writer.println("Allowed actions:");
        for (var command : Command.values()) {
            writer.printf(">> %s: %s\n", command.getName(), command.getDescription());
        }
        writer.println();
    }

    private void processCommands() {
        Command userCommand = askForKnownCommand();
        while (!Command.EXIT.equals(userCommand)) {
            commandProcessors.get(userCommand).processCommand();
            userCommand = askForKnownCommand();
        }

    }

    private Command askForKnownCommand() {
        Command parsedCommand;
        do {
            writer.print("Enter command: ");
            String readCommand = reader.next();
            parsedCommand = Command.resolveByName(readCommand);
            if (parsedCommand == null) {
                writer.printf("Unknown command %s; try again\n", readCommand);
            }
        } while (parsedCommand == null);
        return parsedCommand;
    }
}
