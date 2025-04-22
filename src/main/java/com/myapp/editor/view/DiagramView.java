package com.myapp.editor.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import com.myapp.editor.controller.DiagramController;
import com.myapp.editor.controller.command.CommandManager;
import com.myapp.editor.model.DiagramModel;

public class DiagramView extends JPanel {
    private DiagramModel model;
    private JMenuItem saveMenuItem;
    private JMenuItem loadMenuItem;
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
    private JButton textButton;

    // State Diagram
    private JPanel stateDiagramOptionsPanel;
    private JButton initialStateButton;
    private JButton finalStateButton;
    private JButton stateElementButton;
    private JButton decisionButton;

    private JPanel classDiagramOptionsPanel;
    private JButton classButton;
    private JButton interfaceButton;
    private JButton abstractClassButton;
    private JButton enumButton;
    private JButton packageButton;

    //connector 
    private JPanel connectorPanel;
    private JButton connectorButton; 
    private JButton associationButton;
    private JButton generalizationButton;
    private JButton realizationButton;
    private JButton aggregationButton;
    private JButton compositionButton;
    private JButton dashedLineButton; 

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
            "Select Model", "Use Case Diagram", "Class Diagram", "State Diagram"
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
        stateDiagramOptionsPanel.setVisible("State Diagram".equals(selected));
        connectorPanel.setVisible("Connector".equals(selected));

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
    

    classDiagramOptionsPanel.add(classButton);
    classDiagramOptionsPanel.add(interfaceButton);
    classDiagramOptionsPanel.add(abstractClassButton);
    classDiagramOptionsPanel.add(enumButton);
    

    classDiagramOptionsPanel.setVisible(false);
    leftPanel.add(classDiagramOptionsPanel);

    // State Diagram Options
    stateDiagramOptionsPanel = new JPanel();
    stateDiagramOptionsPanel.setLayout(new BoxLayout(stateDiagramOptionsPanel, BoxLayout.Y_AXIS));
    stateDiagramOptionsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
    stateDiagramOptionsPanel.setBorder(BorderFactory.createTitledBorder("State Diagram Elements"));
    initialStateButton = new JButton("Initial State");
    finalStateButton = new JButton("Final State");
    stateElementButton = new JButton("State");
    decisionButton = new JButton("Decision");
    stateDiagramOptionsPanel.add(initialStateButton);
    stateDiagramOptionsPanel.add(finalStateButton);
    stateDiagramOptionsPanel.add(stateElementButton);
    stateDiagramOptionsPanel.add(decisionButton);
    stateDiagramOptionsPanel.setVisible(false);
    leftPanel.add(stateDiagramOptionsPanel);


    // -------- Connector Button --------
    connectorButton = new JButton("Connector");
    connectorButton.setAlignmentX(Component.LEFT_ALIGNMENT);
    leftPanel.add(connectorButton);

    // -------- Connector Panel --------
    connectorPanel = new JPanel();
    connectorPanel.setLayout(new BoxLayout(connectorPanel, BoxLayout.Y_AXIS));
    connectorPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
    connectorPanel.setBorder(BorderFactory.createTitledBorder("Connectors"));

    // Create connector buttons
    associationButton = new JButton("Association");
    generalizationButton = new JButton("Generalization");
    realizationButton = new JButton("Realization");
    aggregationButton = new JButton("Aggregation");
    compositionButton = new JButton("Composition");
    dashedLineButton = new JButton("Dashed Line");

    // Add connector buttons to the panel
    connectorPanel.add(associationButton);
    connectorPanel.add(generalizationButton);
    connectorPanel.add(realizationButton);
    connectorPanel.add(aggregationButton);
    connectorPanel.add(compositionButton);
    connectorPanel.add(dashedLineButton);

    // Set connectorPanel visibility to false initially
    connectorPanel.setVisible(false);

    // Add connectorPanel to leftPanel
    leftPanel.add(connectorPanel);

    // Toggle visibility of connectorPanel when connectorButton is clicked
    connectorButton.addActionListener(e -> {
        connectorPanel.setVisible(!connectorPanel.isVisible());
        revalidate();
        repaint();
    });

    // Make sure the leftPanel is added to the main layout
    add(leftPanel, BorderLayout.WEST);



    // Show Use Case panel on model selection
    modelSelector.addActionListener(e -> {
        String selected = (String) modelSelector.getSelectedItem();
        revalidate();
        repaint();
    });
    
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
    textButton = new JButton("Text");

    // Add to panel
    generalOptionsPanel.add(cylinderButton);
    generalOptionsPanel.add(ellipseButton);
    generalOptionsPanel.add(hexagonButton);
    generalOptionsPanel.add(parallelogramButton);
    generalOptionsPanel.add(rectangleButton);
    generalOptionsPanel.add(triangleButton);
    generalOptionsPanel.add(textButton);

    generalOptionsPanel.setVisible(false);
    leftPanel.add(generalOptionsPanel);

    generalButton.addActionListener(e -> {
        generalOptionsPanel.setVisible(!generalOptionsPanel.isVisible());
        revalidate();
        repaint();
    });

    add(leftPanel, BorderLayout.WEST);

    // -------- Diagram Panel --------
    CommandManager commandManager = new CommandManager();
    diagramPanel = new DiagramPanel(model, commandManager);
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

    public JButton getInitialStateButton() { return initialStateButton; }
    public JButton getFinalStateButton() { return finalStateButton; }
    public JButton getStateElementButton() { return stateElementButton; }
    public JButton getDecisionButton() { return decisionButton; }

    
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

    public JButton getTextButton() { return textButton; }

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
    
    

    //connectors
    public JButton getAssociationButton() { return associationButton; }
    public JButton getGeneralizationButton() { return generalizationButton; }
    public JButton getRealizationButton() { return realizationButton; }
    public JButton getAggregationButton() { return aggregationButton; }
    public JButton getCompositionButton() { return compositionButton; }
    public JButton getDashedLineButton() { return dashedLineButton; }

    
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
