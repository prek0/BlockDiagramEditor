package com.myapp.editor.controller;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import com.myapp.editor.controller.command.AddConnectorCommand;
import com.myapp.editor.controller.command.AddElementCommand;
import com.myapp.editor.controller.command.Command;
import com.myapp.editor.controller.command.CommandManager;
import com.myapp.editor.controller.command.LoadCommand;
import com.myapp.editor.controller.command.MoveElementCommand;
import com.myapp.editor.controller.command.MoveGroupCommand;
import com.myapp.editor.controller.command.PasteElementCommand;
import com.myapp.editor.controller.command.SaveCommand;
import com.myapp.editor.model.Connector;
import com.myapp.editor.model.DiagramElement;
import com.myapp.editor.model.DiagramModel;
import com.myapp.editor.view.DiagramView;

public class DiagramController {
    private DiagramModel model;
    private DiagramView view;
    private ElementFactory elementFactory;
    private CommandManager commandManager; 
    private DiagramElement selectedElement;
    private DiagramElement copiedElement;
    private String text = "";

    private String selectedConnectorType = "Association"; 


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public DiagramController(DiagramModel model, DiagramView view) {
        this.model = model;
        this.view = view;
        this.elementFactory = new ElementFactory(model);  

        view.setSaveAction(e -> new SaveCommand(model, view).execute());
        view.setLoadAction(e -> new LoadCommand(model, view).execute());

        // Initialize CommandManager here
        this.commandManager = new CommandManager();  // Initialize the CommandManager

        // Link back controller to view
        view.setController(this);
        view.getDiagramPanel().setController(this);


        // Setup keyboard shortcuts 
        view.setupKeyboardShortcuts(
        new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                // commandManager.undo();
                // view.getDiagramPanel().repaint();
                commandManager.undo();
                view.getDiagramPanel().getSelectionManager().clear(); 
                view.getDiagramPanel().revalidate(); 
                view.getDiagramPanel().repaint();
            }
        },
        new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                commandManager.redo();
                view.getDiagramPanel().repaint();
            }
        },
        new AbstractAction() { // Ctrl+C
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedElement != null) {
                    copiedElement = selectedElement;
                    System.out.println("Copied: " + copiedElement.getText());
                }
            }
        },
        new AbstractAction() { // Ctrl+V
            @Override
            public void actionPerformed(ActionEvent e) {
                if (copiedElement != null) {
                    DiagramElement cloned = elementFactory.cloneElement(copiedElement);
                    Command pasteCommand = new PasteElementCommand(model, cloned, view);
                    commandManager.executeCommand(pasteCommand);
                }
            }
        }
    );


        // Attach action to Use Case button
        view.getUseCaseButton().addActionListener(e -> addElement("Use Case"));
        view.getActorButton().addActionListener(e -> addElement("Actor"));
        view.getSystemBoundaryButton().addActionListener(e -> addElement("System Boundary"));

        // Attach action to General shape buttons
        view.getCylinderButton().addActionListener(e -> addElement("Cylinder"));
        view.getEllipseButton().addActionListener(e -> addElement("Ellipse"));
        view.getHexagonButton().addActionListener(e -> addElement("Hexagon"));
        view.getParallelogramButton().addActionListener(e -> addElement("Parallelogram"));
        view.getRectangleButton().addActionListener(e -> addElement("Rectangle"));
        view.getTriangleButton().addActionListener(e -> addElement("Triangle"));

        view.getInitialStateButton().addActionListener(e -> addElement("InitialState"));
        view.getFinalStateButton().addActionListener(e -> addElement("FinalState"));
        view.getStateElementButton().addActionListener(e -> addElement("State"));
        view.getDecisionButton().addActionListener(e -> addElement("Decision"));

        view.getClassButton().addActionListener(e -> addElement("ClassBox"));
        view.getInterfaceButton().addActionListener(e -> addElement("InterfaceBox"));
        view.getAbstractClassButton().addActionListener(e -> addElement("AbstractBox"));
        view.getEnumButton().addActionListener(e -> addElement("Enum"));
        view.getPackageButton().addActionListener(e -> addElement("packageBox"));

        view.getAssociationButton().addActionListener(e -> setSelectedConnectorType("Association"));
        view.getGeneralizationButton().addActionListener(e -> setSelectedConnectorType("Generalization"));
        view.getRealizationButton().addActionListener(e ->  setSelectedConnectorType("Realization"));
        view.getAggregationButton().addActionListener(e -> setSelectedConnectorType("Aggregation"));
        view.getCompositionButton().addActionListener(e -> setSelectedConnectorType("Composition"));
        view.getDashedLineButton().addActionListener(e -> setSelectedConnectorType("DashedLine"));


        // Handle the "Text" button click (Predefined text placement)
        view.getTextButton().addActionListener(e -> {
            // Prompt user for the text input
            String labelText = JOptionPane.showInputDialog(view, "Enter text for the element:", "Text Element", JOptionPane.PLAIN_MESSAGE);

            if (labelText != null && !labelText.trim().isEmpty()) {
                labelText = labelText.trim();

                // Default position or allow custom position
                int x = 50; // default X position
                int y = 50; // default Y position

                // Create a text element at the predefined position
                DiagramElement textElement = elementFactory.createElement("Text", x, y, labelText);  

                // Add the text element to the model and update the view
                model.addElement(textElement);
                view.getDiagramPanel().repaint();
            }
        });


        view.getDiagramPanel().addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                DiagramElement clicked = getElementAt(e.getPoint());
                if (clicked != null) {
                    selectedElement = clicked;
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                // Check if it's a double-click
                if (e.getClickCount() == 2) {
                    // Iterate over all elements to find the one at the clicked position
                    for (DiagramElement element : model.getElements()) {
                        if (element.containsPoint(e.getX(), e.getY())) { // Check if the click is inside the element's bounds
                            // Prompt user to enter new text for the element
                            String newText = JOptionPane.showInputDialog(view, "Edit text for the element:", element.getText());

                            if (newText != null && !newText.trim().isEmpty()) {
                                // Trim and update the text of the element
                                element.setText(newText.trim());
                                model.updateElement(element); // Ensure the model is updated
                                view.getDiagramPanel().repaint(); // Repaint the diagram to reflect changes
                            }
                            break; // Exit the loop after finding the element
                        }
                    }
                }
            }
        });
        
    }
    
    public DiagramElement getElementAt(Point point) {
        for (DiagramElement element : model.getElements()) {
            if (element.contains(point)) {
                return element;
            }
        }
        return null;
    }

    public void elementMoved(DiagramElement element, int oldX, int oldY, int newX, int newY) {
        Command moveCommand = new MoveElementCommand(element, oldX, oldY, newX, newY);
        commandManager.executeCommand(moveCommand);
    }

    public void addConnector(String connectorType, DiagramElement source, DiagramElement destination) {
        Connector connector = elementFactory.createConnector(connectorType, source, destination);
        Command addConnectorCommand = new AddConnectorCommand(model, connector, view);
        commandManager.executeCommand(addConnectorCommand);
    }

    public AddConnectorCommand createConnectorCommand(DiagramElement source, DiagramElement destination) {
        Connector connector = new Connector(source, destination);
        return new AddConnectorCommand(model, connector, view);
    }

    public void handleConnectorCreation(DiagramElement source, DiagramElement destination) {
        AddConnectorCommand cmd = createConnectorCommand(source, destination);
        commandManager.executeCommand(cmd); 
    }

    public void setSelectedConnectorType(String type) {
        this.selectedConnectorType = type;
    }
    
    public String getSelectedConnectorType() {
        return selectedConnectorType;
    }
    
    
    // Method to add elements
    public void addElement(String type) {
        int x = elementFactory.randomX();
        int y = elementFactory.randomY();
    
        String labelText = JOptionPane.showInputDialog(view, "Enter text for the element:", "Element Label", JOptionPane.PLAIN_MESSAGE);
    
        // Cancel clicked: labelText will be null, so do nothing
        if (labelText == null) {
            return;
        }
    
        // If text is empty or just spaces, assign default label
        if (labelText.trim().isEmpty()) {
            labelText = type; // Default label is the type name
        } else {
            labelText = labelText.trim(); // Clean up whitespace
        }
    
        // Create element and execute add command
        DiagramElement element = elementFactory.createElement(type, x, y, labelText);
        Command addElementCommand = new AddElementCommand(model, element, view);
        commandManager.executeCommand(addElementCommand);
    
        view.getDiagramPanel().repaint();
    }
    
    


    public void groupMoved(Map<DiagramElement, Point> originalPositions, Map<DiagramElement, Point> newPositions) {
        commandManager.executeCommand(new MoveGroupCommand(originalPositions, newPositions));
    }

}    