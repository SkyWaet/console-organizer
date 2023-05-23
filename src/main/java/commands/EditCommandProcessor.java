package commands;

import model.Command;
import model.Person;
import storage.PersonStorage;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class EditCommandProcessor extends AbstractCommandProcessor {
    public EditCommandProcessor(PersonStorage storage, Scanner reader, PrintStream writer) {
        super(storage, reader, writer);
    }

    @Override
    public Command getCommandType() {
        return Command.EDIT;
    }

    @Override
    public void processCommand() {
        writer.println("Processing \"edit\" command.");
        int serviceNumber = getServiceNumber();
        Person userInput = readPerson();
        Person saved = storage.edit(serviceNumber, userInput);
        writer.printf("Person with serviceNumber %d was successfully edited\n", saved.getServiceNumber());
    }

    private int getServiceNumber() {
        while (true) {
            writer.print("Enter serviceNumber of user you would like to edit: ");
            try {
                int number = reader.nextInt();
                if (number > 0) {
                    return number;
                }
                writer.println("ServiceNumber must be positive. Try again");
            } catch (InputMismatchException | NumberFormatException e) {
                writer.println("Wrong number format. Try again");
            }
        }
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
            if (!line.isBlank()) {
                phoneNumbers.add(line);
            }
            line = reader.nextLine();
        }
        builder.phoneNumbers(phoneNumbers);
        return builder.build();
    }
}
