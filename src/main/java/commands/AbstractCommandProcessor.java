package commands;

import lombok.RequiredArgsConstructor;
import storage.PersonStorage;

import java.io.PrintStream;
import java.util.Scanner;

@RequiredArgsConstructor
public abstract class AbstractCommandProcessor implements CommandProcessor {

    protected final PersonStorage storage;
    protected final Scanner reader;
    protected final PrintStream writer;
}
