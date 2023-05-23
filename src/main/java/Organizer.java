import configuration.ApplicationFactory;
import userinterface.UserInterface;

public class Organizer {
    public static void main(String[] args) {
        ApplicationFactory factory = new ApplicationFactory();
        UserInterface userInterface = factory.configureInterface();
        userInterface.interactWithUser();
    }
}
