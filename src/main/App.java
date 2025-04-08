package main;

import controller.DiagramController;
import model.DiagramModel;
import view.DiagramView;

import javax.swing.*;

public class App {
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
