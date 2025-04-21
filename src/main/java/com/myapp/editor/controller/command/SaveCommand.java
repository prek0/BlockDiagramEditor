package com.myapp.editor.controller.command;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.myapp.editor.model.DiagramModel;
import com.myapp.editor.view.DiagramView;

public class SaveCommand implements Command {
    private DiagramModel model;
    private DiagramView view;

    public SaveCommand(DiagramModel model, DiagramView view) {
        this.model = model;
        this.view = view;
    }

    @Override
    public void execute() {
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
    
    @Override
    public void undo() {
        // No undo action for load
    }


    }

