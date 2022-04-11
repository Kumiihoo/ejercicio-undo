package com.kreitek.editor.commands;

import com.kreitek.editor.*;

public class  CommandFactory {
    private static final CommandParser commandParser = new CommandParser();
    Originator originator = new Originator();
    CareTaker careTaker = new CareTaker();

    public Command getCommand(String commandLine) throws BadCommandException, ExitException {
        String[] args = commandParser.parse(commandLine);
        return switch (args[0]) {
            case "a" -> createAppendCommand(args[1]);
            case "u" -> createUpdateCommand(args[1], args[2]);
            case "d" -> createDeleteCommand(args[1]);
            case "undo" -> createUndoCommand();
            default -> throw new ExitException();
        };
    }

    private Command createUndoCommand() {
        String data = originator.getState();
        return new UndoCommand(data);
    }

    private Command createDeleteCommand(String lineNumber) {
        int number = Integer.parseInt(lineNumber);
        return new DeleteCommand(number);
    }

    private Command createUpdateCommand(String lineNumber, String data) {
        originator.setState(data);
        careTaker.add(originator.saveStateToMemento());
        int number = Integer.parseInt(lineNumber);
        return new UpdateCommand(data, number);
    }

    private Command createAppendCommand(String data) {
        originator.setState(data);
        careTaker.add(originator.saveStateToMemento());
        return new AppendCommand(data);
    }

}
