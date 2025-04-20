package com.myapp.editor.view;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import com.myapp.editor.controller.DiagramController;
import com.myapp.editor.model.*;

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

    private JButton generalButton;
    private JPanel generalOptionsPanel;

    // Declare general shape buttons
    private JButton cylinderButton;
    private JButton ellipseButton;
    private JButton hexagonButton;
    private JButton parallelogramButton;
    private JButton rectangleButton;
    private JButton triangleButton;

    private JPanel classDiagramOptionsPanel;
    private JButton classButton;
    private JButton interfaceButton;
    private JButton abstractClassButton;
    private JButton enumButton;
    private JButton packageButton;

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
        modelSelector = new JComboBox<>(new String[] {
            "Select Model", "Use Case Diagram", "Class Diagram", "Sequence Diagram"
        });
        modelSelector.setAlignmentX(Component.LEFT_ALIGNMENT);
        leftPanel.add(modelSelector);

       // -------- Use Case Options --------
       useCaseOptionsPanel = new JPanel();
       useCaseOptionsPanel.setLayout(new BoxLayout(useCaseOptionsPanel, BoxLayout.Y_AXIS));
       useCaseOptionsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
       useCaseOptionsPanel.setBorder(BorderFactory.createTitledBorder("Use Case Elements"));

       useCaseButton = new JButton("Use Case");
       actorButton = new JButton("Actor");
       systemBoundaryButton = new JButton("System Boundary");

       useCaseOptionsPanel.add(useCaseButton);
       useCaseOptionsPanel.add(actorButton);
       useCaseOptionsPanel.add(systemBoundaryButton);

       useCaseOptionsPanel.setVisible(false);
       leftPanel.add(useCaseOptionsPanel);

       // Show Use Case panel on model selection
       modelSelector.addActionListener(e -> {
        String selected = (String) modelSelector.getSelectedItem();
        
        useCaseOptionsPanel.setVisible("Use Case Diagram".equals(selected));
        classDiagramOptionsPanel.setVisible("Class Diagram".equals(selected));
        
        revalidate();
        repaint();
    });

    // -------- Class Diagram Options --------
    classDiagramOptionsPanel = new JPanel();
    classDiagramOptionsPanel.setLayout(new BoxLayout(classDiagramOptionsPanel, BoxLayout.Y_AXIS));
    classDiagramOptionsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
    classDiagramOptionsPanel.setBorder(BorderFactory.createTitledBorder("Class Diagram Elements"));

    classButton = new JButton("Class");
    interfaceButton = new JButton("Interface");
    abstractClassButton = new JButton("Abstract Class");
    enumButton = new JButton("Enum");
    packageButton = new JButton("Package");

    classDiagramOptionsPanel.add(classButton);
    classDiagramOptionsPanel.add(interfaceButton);
    classDiagramOptionsPanel.add(abstractClassButton);
    classDiagramOptionsPanel.add(enumButton);
    classDiagramOptionsPanel.add(packageButton);

    classDiagramOptionsPanel.setVisible(false);
    leftPanel.add(classDiagramOptionsPanel);

    // -------- General Options --------
    generalButton = new JButton("General");
    generalButton.setAlignmentX(Component.LEFT_ALIGNMENT);
    leftPanel.add(generalButton);

    generalOptionsPanel = new JPanel();
    generalOptionsPanel.setLayout(new BoxLayout(generalOptionsPanel, BoxLayout.Y_AXIS));
    generalOptionsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
    generalOptionsPanel.setBorder(BorderFactory.createTitledBorder("General Elements"));

    // Create general buttons
    cylinderButton = new JButton("Cylinder");
    ellipseButton = new JButton("Ellipse");
    hexagonButton = new JButton("Hexagon");
    parallelogramButton = new JButton("Parallelogram");
    rectangleButton = new JButton("Rectangle");
    triangleButton = new JButton("Triangle");

    // Add to panel
    generalOptionsPanel.add(cylinderButton);
    generalOptionsPanel.add(ellipseButton);
    generalOptionsPanel.add(hexagonButton);
    generalOptionsPanel.add(parallelogramButton);
    generalOptionsPanel.add(rectangleButton);
    generalOptionsPanel.add(triangleButton);

    generalOptionsPanel.setVisible(false);
    leftPanel.add(generalOptionsPanel);

    generalButton.addActionListener(e -> {
        generalOptionsPanel.setVisible(!generalOptionsPanel.isVisible());
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

    
    // Getter methods for general shape buttons
    public JButton getCylinderButton() {
        return cylinderButton;
    }

    public JButton getEllipseButton() {
        return ellipseButton;
    }

    public JButton getHexagonButton() {
        return hexagonButton;
    }

    public JButton getParallelogramButton() {
        return parallelogramButton;
    }

    public JButton getRectangleButton() {
        return rectangleButton;
    }

    public JButton getTriangleButton() {
        return triangleButton;
    }

    public JButton getClassButton() {
        return classButton;
    }
    
    public JButton getInterfaceButton() {
        return interfaceButton;
    }
    
    public JButton getAbstractClassButton() {
        return abstractClassButton;
    }
    
    public JButton getEnumButton() {
        return enumButton;
    }
    
    public JButton getPackageButton() {
        return packageButton;
    }

    public void setupKeyboardShortcuts(Action undoAction, Action redoAction, Action copyAction, Action pasteAction) {
        InputMap inputMap = diagramPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = diagramPanel.getActionMap();
    
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK), "undo");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_DOWN_MASK), "redo");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK), "copy");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK), "paste");
    
        actionMap.put("undo", undoAction);
        actionMap.put("redo", redoAction);
        actionMap.put("copy", copyAction);
        actionMap.put("paste", pasteAction);
    }
    
}
