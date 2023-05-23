package configuration;

import commands.*;
import model.Command;
import storage.InMemoryPersonStorage;
import storage.PersonStorage;
import storage.XmlPersonStorage;
import userinterface.CommandLineUserInterface;
import userinterface.UserInterface;

import java.io.*;
import java.util.EnumMap;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

public class ApplicationFactory {

    private final Properties properties;

    public ApplicationFactory() {
        properties = new Properties();
        try {
            properties.load(new FileInputStream("./src/main/resources/application.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public UserInterface configureInterface() {
        PersonStorage storage = configureStorage();
        InputStream userInput = System.in;
        OutputStream appOutput = System.out;
        Map<Command, CommandProcessor> commandProcessors = configureCommandProcessors(storage, userInput, appOutput);
        return new CommandLineUserInterface(userInput, appOutput, commandProcessors);
    }

    private PersonStorage configureStorage() {
        return switch (properties.getProperty("storage-type", "xml")) {
            case "in-memory" -> new InMemoryPersonStorage();
            case "xml" -> new XmlPersonStorage(properties.getProperty("filename", "storage.xml"));
            default ->
                    throw new IllegalArgumentException(String.format("Unknown storage type: %s", properties.getProperty("storage-type")));
        };
    }

    private Map<Command, CommandProcessor> configureCommandProcessors(PersonStorage storage, InputStream userInput, OutputStream appOutput) {
        Map<Command, CommandProcessor> commandProcessors = new EnumMap<>(Command.class);
        Scanner reader = new Scanner(userInput);
        PrintStream writer = new PrintStream(appOutput);
        commandProcessors.put(Command.ADD, new AddCommandProcessor(storage, reader, writer));
        commandProcessors.put(Command.EDIT, new EditCommandProcessor(storage, reader, writer));
        commandProcessors.put(Command.FIND_ALL, new FindAllCommandProcessor(storage, reader, writer));
        commandProcessors.put(Command.FIND_ONE, new FindOneCommandProcessor(storage, reader, writer));
        commandProcessors.put(Command.EXIT, new ExitCommandProcessor(storage, reader, writer));
        return commandProcessors;
    }

}
