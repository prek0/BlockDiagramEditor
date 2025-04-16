package view;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import controller.DiagramController;
import model.*;

public class DiagramView extends JPanel {
    private DiagramModel model;
    private JMenuItem saveMenuItem;
    private JMenuItem loadMenuItem;
    private JMenuItem undoMenuItem;
    private JMenuItem redoMenuItem;
    private DiagramPanel diagramPanel;
    private DiagramController controller;

    private JPanel leftPanel;
    private JComboBox<String> modelSelector;
    private JPanel useCaseOptionsPanel;

    private JButton useCaseButton;
    private JButton actorButton;
    private JButton systemBoundaryButton;

    public DiagramView(DiagramModel model) {
        this.model = model;
        setPreferredSize(new Dimension(800, 600));
        setLayout(new BorderLayout());

        // ------- Menu Bar --------
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        saveMenuItem = new JMenuItem("Save");
        loadMenuItem = new JMenuItem("Load");
        fileMenu.add(saveMenuItem);
        fileMenu.add(loadMenuItem);
        menuBar.add(fileMenu);

        JMenu editMenu = new JMenu("Edit");
        undoMenuItem = new JMenuItem("Undo");
        redoMenuItem = new JMenuItem("Redo");
        editMenu.add(undoMenuItem);
        editMenu.add(redoMenuItem);
        menuBar.add(editMenu);

        JPanel menuPanel = new JPanel(new BorderLayout());
        menuPanel.add(menuBar, BorderLayout.NORTH);
        add(menuPanel, BorderLayout.NORTH);

        // ------- Left Panel --------
        leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setPreferredSize(new Dimension(200, 600));
        leftPanel.setBorder(BorderFactory.createTitledBorder("Options"));

        // Model Selector ComboBox
        modelSelector = new JComboBox<>(new String[] { "Select Model", "Use Case Diagram", "Class Diagram", "Sequence Diagram" });
        modelSelector.setAlignmentX(Component.LEFT_ALIGNMENT);
        leftPanel.add(modelSelector);

        // Use Case Options Panel (hidden initially)
        useCaseOptionsPanel = new JPanel();
        useCaseOptionsPanel.setLayout(new BoxLayout(useCaseOptionsPanel, BoxLayout.Y_AXIS));
        useCaseOptionsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        useCaseOptionsPanel.setBorder(BorderFactory.createTitledBorder("Use Case Elements"));

        // Buttons for creating elements
        useCaseButton = new JButton("Use Case");
        actorButton = new JButton("Actor");
        systemBoundaryButton = new JButton("System Boundary");

        // Adding buttons to the panel
        useCaseOptionsPanel.add(useCaseButton);
        useCaseOptionsPanel.add(actorButton);
        useCaseOptionsPanel.add(systemBoundaryButton);

        // Initially hide the options panel
        useCaseOptionsPanel.setVisible(false);
        leftPanel.add(useCaseOptionsPanel);

        // Handle model selection changes
        modelSelector.addActionListener(e -> {
            String selected = (String) modelSelector.getSelectedItem();
            // Show use case buttons only when "Use Case Diagram" is selected
            useCaseOptionsPanel.setVisible("Use Case Diagram".equals(selected));
            revalidate();
            repaint();
        });

        add(leftPanel, BorderLayout.WEST);

        // -------- Diagram Panel --------
        diagramPanel = new DiagramPanel(model.getElements(), model.getConnectors());
        add(diagramPanel, BorderLayout.CENTER);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        diagramPanel.repaint();
    }

    // Controller hooks
    public void setSaveAction(ActionListener l) {
        saveMenuItem.addActionListener(l);
    }

    public void setLoadAction(ActionListener l) {
        loadMenuItem.addActionListener(l);
    }

    public void setController(DiagramController controller) {
        this.controller = controller;
    }

    public void setUndoAction(ActionListener l) {
        undoMenuItem.addActionListener(l);
    }

    public void setRedoAction(ActionListener l) {
        redoMenuItem.addActionListener(l);
    }

    public DiagramPanel getDiagramPanel() {
        return diagramPanel;
    }

    // Getter methods for buttons
    public JButton getUseCaseButton() {
        return useCaseButton;
    }

    public JButton getActorButton() {
        return actorButton;
    }

    public JButton getSystemBoundaryButton() {
        return systemBoundaryButton;
    }
}
