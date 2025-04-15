package com.myapp.editor;

import com.myapp.editor.model.*;
import com.myapp.editor.view.*;
import com.myapp.editor.controller.*;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DiagramModel model = new DiagramModel();
            DiagramView view = new DiagramView(model);
            new DiagramController(model, view);

            JFrame frame = new JFrame("Block Diagram Editor");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(view);
            frame.pack();
            frame.setVisible(true);
        });
    }
}
