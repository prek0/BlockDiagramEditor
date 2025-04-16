package com.myapp.editor;

import model.DiagramElement;
import model.DiagramModel;
import view.*;
import controller.*;
import model.Connector;  

import java.util.List;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Create the model
            DiagramModel model = new DiagramModel();

            // Create the view and pass the model to it
            DiagramView view = new DiagramView(model);

            // Get the elements and connectors from the model
            List<DiagramElement> elements = model.getElements();
            List<Connector> connectors = model.getConnectors();

            // Create the controller and pass the view and the elements/connectors
            DiagramController controller = new DiagramController(model, view, elements, connectors);

            // Create and set up the JFrame
            JFrame frame = new JFrame("Block Diagram Editor");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(view);
            frame.pack();
            frame.setVisible(true);
        });
    }
}
