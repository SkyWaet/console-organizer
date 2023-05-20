package commands;

import model.Command;
import model.SearchParameters;
import storage.PersonStorage;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FindOneCommandProcessor extends AbstractCommandProcessor {
    public FindOneCommandProcessor(PersonStorage storage, Scanner reader, PrintStream writer) {
        super(storage, reader, writer);
    }

    @Override
    public Command getCommandType() {
        return Command.FIND_ONE;
    }

    @Override
    public void processCommand() {
        writer.println("Processing \"find-one\" command.");
        SearchParameters parameters = readParameters();
        storage.findOne(parameters).ifPresentOrElse(
                person -> {
                    writer.println("Search result");
                    writer.println(person);
                },
                () -> writer.println("No data found for your request")
        );

        writer.println("=================================");
    }

    private SearchParameters readParameters() {
        SearchParameters.SearchParametersBuilder builder = SearchParameters.builder();

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
