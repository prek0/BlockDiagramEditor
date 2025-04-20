package com.myapp.editor.controller;

import com.myapp.editor.model.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import com.myapp.editor.controller.command.*;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import com.myapp.editor.view.DiagramView;

public class DiagramController {
    private DiagramModel model;
    private DiagramView view;
    private ElementFactory elementFactory;
    private CommandManager commandManager; 
    private DiagramElement selectedElement;
    private DiagramElement copiedElement;
    private String text = "";

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

        view.setSaveAction(e -> saveDiagram());
        view.setLoadAction(e -> loadDiagram());

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

    public AddConnectorCommand createConnectorCommand(DiagramElement source, DiagramElement destination) {
        Connector connector = new Connector(source, destination);
        return new AddConnectorCommand(model, connector, view);
    }

    public void handleConnectorCreation(DiagramElement source, DiagramElement destination) {
        AddConnectorCommand cmd = createConnectorCommand(source, destination);
        commandManager.executeCommand(cmd); 
    }
    
    
    // Method to add elements
    public void addElement(String type) {
        int x = elementFactory.randomX();
        int y = elementFactory.randomY();

        String labelText = JOptionPane.showInputDialog(view, "Enter text for the element:", "Element Label", JOptionPane.PLAIN_MESSAGE);     
        DiagramElement element = elementFactory.createElement(type, x, y, labelText);

        // Create the AddElementCommand and execute it using CommandManager
        Command addElementCommand = new AddElementCommand(model, element, view);
        commandManager.executeCommand(addElementCommand);  

        view.getDiagramPanel().repaint();
    }


   public void saveDiagram() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Diagram");
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("PNG Image", "png"));
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("JPEG Image", "jpeg"));
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Diagram File", "diag"));
        fileChooser.setAcceptAllFileFilterUsed(false);
    
        int userSelection = fileChooser.showSaveDialog(view);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            String extension = ((FileNameExtensionFilter) fileChooser.getFileFilter()).getExtensions()[0];
            String fileName = file.getAbsolutePath();
    
            // Make sure the file name ends with the correct extension
            if (!fileName.endsWith("." + extension)) {
                file = new File(fileName + "." + extension);
            }
    
            try {
                if (extension.equals("png") || extension.equals("jpeg")) {
                    // Create the image based on the view's dimensions
                    BufferedImage image = new BufferedImage(view.getWidth(), view.getHeight(), BufferedImage.TYPE_INT_ARGB);
                    Graphics2D g2d = image.createGraphics();
                    view.paint(g2d);
                    g2d.dispose();
    
                    // Check if the extension is JPEG/JPG, and write accordingly
                    if (extension.equals("jpeg")) {
                        // Convert ARGB to RGB for JPEG compatibility
                        BufferedImage rgbImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
                        Graphics2D g2dRgb = rgbImage.createGraphics();
                        g2dRgb.drawImage(image, 0, 0, null);
                        g2dRgb.dispose();
    
                        // Write as JPEG
                        boolean saved = ImageIO.write(rgbImage, "JPEG", file);
                        if (!saved) {
                            JOptionPane.showMessageDialog(view, "Error saving JPEG image. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        // Write as PNG
                        ImageIO.write(image, "PNG", file);
                    }
    
                    JOptionPane.showMessageDialog(view, "Image saved successfully!");
                } else {
                    // Saving diagram as a .diag file
                    try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
                        oos.writeObject(model.getElements());
                        oos.writeObject(model.getConnectors());
                        JOptionPane.showMessageDialog(view, "Diagram saved successfully!");
                    }
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(view, "Error saving file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    
    
    
    @SuppressWarnings("unchecked")
    public void loadDiagram() {
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
}    