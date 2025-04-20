package com.myapp.editor.controller.command;

import  com.myapp.editor.model.*;
import com.myapp.editor.view.DiagramView;

public class AddConnectorCommand implements Command {
    private DiagramModel model;
    private Connector connector;
    private DiagramView view;

    public AddConnectorCommand(DiagramModel model, Connector connector, DiagramView view) {
        this.model = model;
        this.connector = connector;
        this.view = view;
    }

    @Override
    public void execute() {
        model.addConnector(connector);
        view.getDiagramPanel().repaint();
    }

    @Override
    public void undo() {
        model.removeConnector(connector);
        view.getDiagramPanel().repaint();
    }
}
