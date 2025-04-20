package com.myapp.editor;

import com.myapp.editor.model.DiagramElement;
import com.myapp.editor.model.DiagramModel;
import com.myapp.editor.view.*;
import com.myapp.editor.controller.*;
import com.myapp.editor.model.Connector;  

import java.util.List;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Runnable onLoginSuccess = () -> {
                DiagramModel model = new DiagramModel();
                DiagramView view = new DiagramView(model);
                List<DiagramElement> elements = model.getElements();
                List<Connector> connectors = model.getConnectors();
                DiagramController controller = new DiagramController(model, view, elements, connectors);

                JFrame frame = new JFrame("Block Diagram Editor");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.add(view);
                frame.pack();
                frame.setVisible(true);
            };

            new LoginView(onLoginSuccess);
        });
    }
}

