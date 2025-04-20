package com.myapp.editor.controller.command;

public interface Command {
    void execute();
    void undo();
}