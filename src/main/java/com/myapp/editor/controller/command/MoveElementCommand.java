package com.myapp.editor.controller.command;

import com.myapp.editor.model.DiagramElement;

public class MoveElementCommand implements Command {
    private DiagramElement element;
    private int oldX, oldY;
    private int newX, newY;

    public MoveElementCommand(DiagramElement element, int oldX, int oldY, int newX, int newY) {
        this.element = element;
        this.oldX = oldX;
        this.oldY = oldY;
        this.newX = newX;
        this.newY = newY;
    }

    @Override
    public void execute() {
        element.setPosition(newX, newY);
    }

    @Override
    public void undo() {
        element.setPosition(oldX, oldY);
    }
}
