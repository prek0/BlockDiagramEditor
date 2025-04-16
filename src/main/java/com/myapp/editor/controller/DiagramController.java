package controller;

import model.*;
import model.usecase.*;
import view.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.Graphics2D;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

public class DiagramController {
    private List<DiagramElement> elements;
    private List<Connector> connectors;
    private DiagramElement selectedElement = null;
    private boolean resizing = false;
    private int offsetX, offsetY;
    private DiagramElement connectorSource = null;
    private DiagramView diagramView;  // Reference to the DiagramView
    private DiagramModel model;
    
        public DiagramController(DiagramModel model, DiagramView diagramView, List<DiagramElement> elements, List<Connector> connectors) {
            this.model = model; // Set the model
        this.diagramView = diagramView;
        this.elements = elements;
        this.connectors = connectors;

        diagramView.setSaveAction(e -> saveDiagram());
        diagramView.setLoadAction(e -> loadDiagram());

        diagramView.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                handleMousePressed(e);
            }

            public void mouseReleased(MouseEvent e) {
                if (selectedElement != null) {
                    selectedElement.selected = false;
                    selectedElement = null;
                    resizing = false;
                    diagramView.repaint();
                }
            }
        });

        diagramView.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                handleMouseDragged(e);
            }
        });

        diagramView.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (selectedElement != null && e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String newText = JOptionPane.showInputDialog("Edit Text:", selectedElement.text);
                    if (newText != null) {
                        selectedElement.text = newText;
                        diagramView.repaint();
                    }
                }
            }
        });
    }

    private void handleMousePressed(MouseEvent e) {
        if (e.isControlDown()) {
            for (DiagramElement element : elements) {
                if (element.contains(e.getX(), e.getY())) {
                    if (connectorSource == null) {
                        connectorSource = element;
                        element.selected = true;
                        diagramView.repaint();
                    } else if (connectorSource != element) {
                        String[] types = (connectorSource instanceof UseCaseElement && element instanceof UseCaseElement)
                                ? new String[]{"<<include>>", "<<extend>>"} : new String[]{"normal"};
                        String type = (String) JOptionPane.showInputDialog(
                                null, "Select connector type:", "Connector Type",
                                JOptionPane.QUESTION_MESSAGE, null, types, types[0]);
                        if (type != null) {
                            connectors.add(new Connector(connectorSource, element, type));
                        }
                        connectorSource.selected = false;
                        connectorSource = null;
                        diagramView.repaint();
                    }
                    return;
                }
            }
        } else {
            for (DiagramElement element : elements) {
                if (element.onResizeHandle(e.getX(), e.getY())) {
                    selectedElement = element;
                    resizing = true;
                    return;
                }
                if (element.contains(e.getX(), e.getY())) {
                    selectedElement = element;
                    offsetX = e.getX() - element.x;
                    offsetY = e.getY() - element.y;
                    element.selected = true;
                    diagramView.repaint();
                    return;
                }
            }

            String[] options = {"Actor", "Use Case", "System Boundary", "Text"};
            String choice = (String) JOptionPane.showInputDialog(null, "Select Element:",
                    "Add Element", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            if (choice != null) {
                String name = JOptionPane.showInputDialog("Enter Name:");
                if (name != null) {
                    switch (choice) {
                        case "Actor": 
                            elements.add(new ActorElement(e.getX(), e.getY(), name, elements.size() + 1)); 
                            break;
                        case "Use Case": 
                            elements.add(new UseCaseElement(e.getX(), e.getY(), name, elements.size() + 1)); 
                            break;
                        case "System Boundary": 
                            elements.add(new SystemBoundary(e.getX(), e.getY(), name, 200, 150, elements.size() + 1)); 
                            break;
                        case "Text": 
                            elements.add(new TextElement(e.getX(), e.getY(), name, elements.size() + 1)); 
                            break;
                    }
                    
                    diagramView.repaint();
                }
            }
        }
    }

    private void handleMouseDragged(MouseEvent e) {
        if (selectedElement != null) {
            if (resizing) {
                int newWidth = e.getX() - selectedElement.x;
                int newHeight = e.getY() - selectedElement.y;

                if (selectedElement instanceof UseCaseElement) {
                    if(newWidth < 60) newWidth = 60;
                    newHeight = (int)(newWidth / 1.5);
                    if(newHeight < 40) newHeight = 40;
                }

                selectedElement.width = newWidth;
                selectedElement.height = newHeight;
            } else {
                selectedElement.x = e.getX() - offsetX;
                selectedElement.y = e.getY() - offsetY;
            }
            diagramView.repaint();
        }
    }

    public void saveDiagram() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Diagram");
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("PNG Image", "png"));
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("JPEG Image", "jpeg", "jpg"));
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Diagram File", "diag"));
        fileChooser.setAcceptAllFileFilterUsed(false);

        int userSelection = fileChooser.showSaveDialog(diagramView);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            String extension = ((FileNameExtensionFilter) fileChooser.getFileFilter()).getExtensions()[0];
            String fileName = file.getAbsolutePath();
            if (!fileName.endsWith("." + extension)) {
                file = new File(fileName + "." + extension);
            }

            try {
                if (extension.equals("png") || extension.equals("jpeg") || extension.equals("jpg")) {
                    BufferedImage image = new BufferedImage(diagramView.getWidth(), diagramView.getHeight(), BufferedImage.TYPE_INT_ARGB);
                    Graphics2D g2d = image.createGraphics();
                    diagramView.paint(g2d);
                    g2d.dispose();
                    ImageIO.write(image, extension, file);
                    JOptionPane.showMessageDialog(diagramView, "Image saved successfully!");
                } else {
                    try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
                        oos.writeObject(model.getElements());
                        oos.writeObject(model.getConnectors());
                        JOptionPane.showMessageDialog(diagramView, "Diagram saved successfully!");
                    }
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(diagramView, "Error saving file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    @SuppressWarnings("unchecked")
    public void loadDiagram() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Load Diagram");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Diagram Files", "diag"));
    
        int userSelection = fileChooser.showOpenDialog(diagramView);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                List<DiagramElement> loadedElements = (List<DiagramElement>) ois.readObject();
                List<Connector> loadedConnectors = (List<Connector>) ois.readObject();
    
                elements.clear();
                connectors.clear();
                elements.addAll(loadedElements);
                connectors.addAll(loadedConnectors);
    
                diagramView.repaint();
                JOptionPane.showMessageDialog(diagramView, "Diagram loaded successfully!");
            } catch (IOException | ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(diagramView, "Error loading diagram: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}