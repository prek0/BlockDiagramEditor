package controller;

import model.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import view.DiagramView;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;


public class DiagramController {
    private DiagramModel model;
    private DiagramView view;
    private ElementFactory elementFactory;
    private List<DiagramElement> elements;
    private List<Connector> connectors;

    public DiagramController(DiagramModel model, DiagramView view, List<DiagramElement> elements, List<Connector> connectors) {
        this.model = model;
        this.view = view;
        this.elementFactory = new ElementFactory(model); // Initialize element factory

        // Link back controller to view
        view.setController(this);

        // Attach action to Use Case button
        view.getUseCaseButton().addActionListener(e -> addElement("Use Case"));
        view.getActorButton().addActionListener(e -> addElement("Actor"));
        view.getSystemBoundaryButton().addActionListener(e -> addElement("System Boundary"));
    }


    // Method to add elements
    public void addElement(String type) {
        int x = elementFactory.randomX();
        int y = elementFactory.randomY();
        
        DiagramElement element = elementFactory.createElement(type, x, y);
        model.addElement(element);
        view.getDiagramPanel().repaint();
    }
    

    public void saveDiagram() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Diagram");
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("PNG Image", "png"));
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("JPEG Image", "jpeg", "jpg"));
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Diagram File", "diag"));
        fileChooser.setAcceptAllFileFilterUsed(false);

        int userSelection = fileChooser.showSaveDialog(view);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            String extension = ((FileNameExtensionFilter) fileChooser.getFileFilter()).getExtensions()[0];
            String fileName = file.getAbsolutePath();
            if (!fileName.endsWith("." + extension)) {
                file = new File(fileName + "." + extension);
            }

            try {
                if (extension.equals("png") || extension.equals("jpeg") || extension.equals("jpg")) {
                    BufferedImage image = new BufferedImage(view.getWidth(), view.getHeight(), BufferedImage.TYPE_INT_ARGB);
                    Graphics2D g2d = image.createGraphics();
                    view.paint(g2d);
                    g2d.dispose();
                    ImageIO.write(image, extension, file);
                    JOptionPane.showMessageDialog(view, "Image saved successfully!");
                } else {
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
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Diagram Files", "diag"));
    
        int userSelection = fileChooser.showOpenDialog(view);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                List<DiagramElement> loadedElements = (List<DiagramElement>) ois.readObject();
                List<Connector> loadedConnectors = (List<Connector>) ois.readObject();
    
                elements.clear();
                connectors.clear();
                elements.addAll(loadedElements);
                connectors.addAll(loadedConnectors);
    
                view.repaint();
                JOptionPane.showMessageDialog(view, "Diagram loaded successfully!");
            } catch (IOException | ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(view, "Error loading diagram: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}