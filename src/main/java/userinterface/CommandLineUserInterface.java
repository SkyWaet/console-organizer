package userinterface;

import commands.CommandProcessor;
import model.Command;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Map;
import java.util.Scanner;

public class CommandLineUserInterface implements UserInterface {

    private final Scanner reader;
    private final PrintStream writer;

    private final Map<Command, CommandProcessor> commandProcessors;

    public CommandLineUserInterface(InputStream readFrom, OutputStream writeTo, Map<Command, CommandProcessor> commandProcessors) {
        this.reader = new Scanner(readFrom);
        this.writer = new PrintStream(writeTo);
        this.commandProcessors = commandProcessors;
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
