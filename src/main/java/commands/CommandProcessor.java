package commands;

import model.Command;

public interface CommandProcessor {
    Command getCommandType();

    void processCommand();
}
