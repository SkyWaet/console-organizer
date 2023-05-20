package model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
@Getter
public enum Command {

    ADD("add", "adds new user to storage"),
    EDIT("edit", "rewrites user in storage if exists"),
    FIND_ALL("find-all", "returns all users from storage"),
    FIND_ONE("find-one", "returns user that matches parameters. if two or more matches found, only one of them will be returned"),
    EXIT("exit", "closes the application");

    private final String name;
    private final String description;

    public static Command resolveByName(String name) {
        return Arrays.stream(Command.values()).filter(val -> val.name.equals(name)).findFirst().orElse(null);
    }

}
