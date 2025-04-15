package controller;

import model.Block;
import model.Connection;
import model.DiagramModel;
import view.DiagramView;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class DiagramController extends MouseAdapter {
    private DiagramModel model;
    private DiagramView view;
    private AtomicInteger idGenerator = new AtomicInteger(1);

    public DiagramController(DiagramModel model, DiagramView view) {
        this.model = model;
        this.view = view;

        view.addMouseListener(this);

        view.setSaveAction(e -> saveDiagram());
        view.setLoadAction(e -> loadDiagram());
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Block newBlock = new Block(idGenerator.getAndIncrement(), e.getX(), e.getY(), 80, 40, "Block");
        model.addBlock(newBlock);
        view.repaint();
    }

    private void saveDiagram() {
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
                        oos.writeObject(model.getBlocks());
                        oos.writeObject(model.getConnections());
                        JOptionPane.showMessageDialog(view, "Diagram saved successfully!");
                    }
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(view, "Error saving file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void loadDiagram() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Load Diagram");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Diagram File", "diag"));

        int userSelection = fileChooser.showOpenDialog(view);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                List<Block> loadedBlocks = (List<Block>) ois.readObject();
                List<Connection> loadedConnections = (List<Connection>) ois.readObject();
                model.setBlocks(loadedBlocks);
                model.setConnections(loadedConnections);

                // Reset ID generator to avoid duplicate IDs
                int maxId = loadedBlocks.stream().mapToInt(b -> b.id).max().orElse(0);
                idGenerator.set(maxId + 1);

                view.repaint();
                JOptionPane.showMessageDialog(view, "Diagram loaded successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(view, "Error loading file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
