package commands;

import model.Command;
import model.Person;
import storage.PersonStorage;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AddCommandProcessor extends AbstractCommandProcessor {

    public AddCommandProcessor(PersonStorage storage, Scanner reader, PrintStream writer) {
        super(storage, reader, writer);
    }

    @Override
    public Command getCommandType() {
        return Command.ADD;
    }

    @Override
    public void processCommand() {
        writer.println("Processing \"add\" command.");
        Person userInput = readPerson();
        Person saved = storage.add(userInput);
        writer.printf("Person successfully added with serviceNumber %d \n", saved.getServiceNumber());
    }

    private Person readPerson() {
        Person.PersonBuilder builder = Person.builder();

        writer.print("Enter surname: ");
        builder.surname(reader.next());

        writer.print("Enter name: ");
        builder.name(reader.next());

        writer.print("Enter patronymic: ");
        builder.patronymic(reader.next());

        writer.print("Enter position: ");
        builder.position(reader.next());

        writer.print("Enter organization: ");
        builder.organization(reader.next());

        writer.print("Enter email: ");
        builder.email(reader.next());

        writer.println("Enter phone numbers one in line. To finish your input, type \\q");

        List<String> phoneNumbers = new ArrayList<>();
        String line = reader.nextLine();
        while (!"\\q".equals(line)) {
            phoneNumbers.add(line);
            line = reader.nextLine();
        }
        builder.phoneNumbers(phoneNumbers);
        return builder.build();
    }
}
