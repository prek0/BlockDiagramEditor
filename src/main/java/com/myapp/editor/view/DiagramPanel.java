package com.myapp.editor.view;

import com.myapp.editor.model.*;

import javax.swing.*;

import com.myapp.editor.controller.DiagramController;

import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class DiagramPanel extends JPanel {
    private List<DiagramElement> elements;
    private List<Connector> connectors;

    private DiagramElement selectedElement = null;
    private Point dragOffset = null;
    private boolean resizing = false;
    private DiagramController controller;
    private int originalX, originalY;

    public DiagramPanel(List<DiagramElement> elements, List<Connector> connectors) {
        this.elements = elements;
        this.connectors = connectors;

        setBackground(Color.WHITE);

        // Mouse press
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                selectedElement = null;
                for (DiagramElement el : elements) {
                    if (el.isOnResizeHandle(e.getPoint())) {
                        selectedElement = el;
                        resizing = true;
                        dragOffset = e.getPoint();
                        el.setSelected(true);
                        break;
                    } else if (el.contains(e.getPoint())) {
                        selectedElement = el;
                        resizing = false;
                        dragOffset = new Point(e.getX() - el.getX(), e.getY() - el.getY());
            
                        originalX = el.getX();
                        originalY = el.getY();
            
                        el.setSelected(true);
                    } else {
                        el.setSelected(false);
                    }
                }
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (selectedElement != null && dragOffset != null && !resizing && controller != null) {
                    int newX = selectedElement.getX();
                    int newY = selectedElement.getY();
            
                    if (originalX != newX || originalY != newY) {
                        controller.elementMoved(selectedElement, originalX, originalY, newX, newY);
                    }
                }
            
                selectedElement = null;
                dragOffset = null;
                resizing = false;
            }

        });

        // Mouse drag
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (selectedElement != null) {
                    if (resizing) {
                        int newWidth = e.getX() - selectedElement.getX();
                        int newHeight = e.getY() - selectedElement.getY();
                        selectedElement.setWidth(Math.max(newWidth, 40)); // Minimum size
                        selectedElement.setHeight(Math.max(newHeight, 30));
                    } else if (dragOffset != null) {
                        selectedElement.setX(e.getX() - dragOffset.x);
                        selectedElement.setY(e.getY() - dragOffset.y);
                    }
                    repaint();
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw connectors first
        for (Connector connector : connectors) {
            connector.draw(g);
        }

        // Draw elements
        for (DiagramElement element : elements) {
            element.draw(g);
        }
    }

    public void setController(DiagramController controller) {
        this.controller = controller;
    }
}
