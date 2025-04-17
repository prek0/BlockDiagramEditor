package controller.command;

public interface Command {
    void execute();
    void undo();
}