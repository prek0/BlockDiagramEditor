package view;

import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;
import model.*;

public class DiagramView extends JPanel {
    private DiagramModel model;
    private JMenuItem saveMenuItem;
    private JMenuItem loadMenuItem;
    private JMenuItem undoMenuItem;
    private JMenuItem redoMenuItem;
    private DiagramPanel diagramPanel;  

    public DiagramView(DiagramModel model) {
        this.model = model;
        setPreferredSize(new Dimension(800, 600));
        setLayout(new BorderLayout());

        // Create menu bar
        JMenuBar menuBar = new JMenuBar();

        // -------- File Menu --------
        JMenu fileMenu = new JMenu("File");
        saveMenuItem = new JMenuItem("Save");
        loadMenuItem = new JMenuItem("Load");
        fileMenu.add(saveMenuItem);
        fileMenu.add(loadMenuItem);
        menuBar.add(fileMenu);

        // -------- Edit Menu --------
        JMenu editMenu = new JMenu("Edit");
        undoMenuItem = new JMenuItem("Undo");
        redoMenuItem = new JMenuItem("Redo");
        editMenu.add(undoMenuItem);
        editMenu.add(redoMenuItem);
        menuBar.add(editMenu);

        // Wrap menu bar in a panel and add it to the top
        JPanel menuPanel = new JPanel(new BorderLayout());
        menuPanel.add(menuBar, BorderLayout.NORTH);
        add(menuPanel, BorderLayout.NORTH);

        // -------- Add DiagramPanel --------
        diagramPanel = new DiagramPanel(model.getElements(), model.getConnectors());  // Initialize DiagramPanel
        add(diagramPanel, BorderLayout.CENTER);  // Add DiagramPanel to the center of DiagramView
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // This will ensure the DiagramPanel handles drawing
        diagramPanel.repaint();  // Trigger repaint on DiagramPanel
    }

    // Hook external controller actions
    public void setSaveAction(ActionListener l) {
        saveMenuItem.addActionListener(l);
    }

    public void setLoadAction(ActionListener l) {
        loadMenuItem.addActionListener(l);
    }

    public void setUndoAction(ActionListener l) {
        undoMenuItem.addActionListener(l);
    }

    public void setRedoAction(ActionListener l) {
        redoMenuItem.addActionListener(l);
    }

    public DiagramPanel getDiagramPanel() {
        return diagramPanel;  // Getter for external access
    }
}
