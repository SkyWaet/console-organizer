import storage.InMemoryPersonStorage;
import userinterface.CommandLineUserInterface;
import userinterface.UserInterface;

public class Organizer {
    public static void main(String[] args) {
        UserInterface userInterface = new CommandLineUserInterface(new InMemoryPersonStorage(), System.in, System.out);
        userInterface.interactWithUser();
    }
}
