package com.myapp.editor.controller.command;

import com.myapp.editor.model.DiagramElement;
import java.awt.Point;
import java.util.Map;

public class MoveGroupCommand implements Command {
    private final Map<DiagramElement, Point> before;
    private final Map<DiagramElement, Point> after;

    public MoveGroupCommand(Map<DiagramElement, Point> before, Map<DiagramElement, Point> after) {
        this.before = before;
        this.after = after;
    }

    @Override
    public void execute() {
        for (Map.Entry<DiagramElement, Point> entry : after.entrySet()) {
            entry.getKey().setPosition(entry.getValue().x, entry.getValue().y);
        }
    }

    @Override
    public void undo() {
        for (Map.Entry<DiagramElement, Point> entry : before.entrySet()) {
            entry.getKey().setPosition(entry.getValue().x, entry.getValue().y);
        }
    }
}
