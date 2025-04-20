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
import java.util.ArrayList;
import java.util.List;

import com.myapp.editor.view.DiagramView;

public class DiagramController {
    private DiagramModel model;
    private DiagramView view;
    private List<DiagramElement> elements;
    private List<Connector> connectors;
    private ElementFactory elementFactory;
    private CommandManager commandManager; 
    private DiagramElement selectedElement;
    private DiagramElement copiedElement;

    public DiagramController(DiagramModel model, DiagramView view, List<DiagramElement> elements, List<Connector> connectors) {
        this.model = model;
        this.view = view;
        this.elementFactory = new ElementFactory(model);  
        this.elements = new ArrayList<>();
        this.connectors = new ArrayList<>();

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
                commandManager.undo();
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

        view.getClassButton().addActionListener(e -> addElement("ClassBox"));
        view.getInterfaceButton().addActionListener(e -> addElement("InterfaceBox"));
        view.getAbstractClassButton().addActionListener(e -> addElement("AbstractBox"));
        view.getEnumButton().addActionListener(e -> addElement("Enum"));
        view.getPackageButton().addActionListener(e -> addElement("packageBox"));


        view.getDiagramPanel().addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                DiagramElement clicked = getElementAt(e.getPoint());
                if (clicked != null) {
                    selectedElement = clicked;
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
    
    
    // Method to add elements
    public void addElement(String type) {
        int x = elementFactory.randomX();
        int y = elementFactory.randomY();
        
        DiagramElement element = elementFactory.createElement(type, x, y);

        // Create the AddElementCommand and execute it using CommandManager
        Command addElementCommand = new AddElementCommand(model, element, view);
        commandManager.executeCommand(addElementCommand);  // This should now work without the NullPointerException

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