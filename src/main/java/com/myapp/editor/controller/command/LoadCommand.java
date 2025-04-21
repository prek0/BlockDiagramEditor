package com.myapp.editor.controller.command;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.myapp.editor.model.Connector;
import com.myapp.editor.model.DiagramElement;
import com.myapp.editor.model.DiagramModel;
import com.myapp.editor.view.DiagramView;

public class LoadCommand implements Command {
    private DiagramModel model;
    private DiagramView view;

    public LoadCommand(DiagramModel model, DiagramView view) {
        this.model = model;
        this.view = view;
    }

    @Override
    public void execute() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Load Diagram");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Diagram Files", "diag"));
    
        int userSelection = fileChooser.showOpenDialog(view);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                List<DiagramElement> loadedElements = (List<DiagramElement>) ois.readObject();
                List<Connector> loadedConnectors = (List<Connector>) ois.readObject();
    
                model.getElements().clear();
                model.getElements().addAll(loadedElements);
                model.getConnectors().clear();
                model.getConnectors().addAll(loadedConnectors);
    
                view.repaint();
                JOptionPane.showMessageDialog(view, "Diagram loaded successfully!");
            } catch (IOException | ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(view, "Error loading diagram: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    public void undo() {
        // No undo action for load
    }
    
}
