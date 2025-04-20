package com.myapp.editor.controller.command;

import com.myapp.editor.model.DiagramElement;
import com.myapp.editor.model.DiagramModel;
import com.myapp.editor.view.DiagramView;

public class PasteElementCommand implements Command {
    private DiagramModel model;
    private DiagramElement element;
    private DiagramView view;

    public PasteElementCommand(DiagramModel model, DiagramElement element, DiagramView view) {
        this.model = model;
        this.element = element;
        this.view = view;
    }

    @Override
    public void execute() {
        model.addElement(element);
        view.getDiagramPanel().repaint();
    }

    @Override
    public void undo() {
        model.removeElement(element);
        view.getDiagramPanel().repaint();
    }
}
