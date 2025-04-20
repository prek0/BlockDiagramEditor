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

    //grouping
    private Point dragStart = null;
    private Rectangle selectionRect = null;
    private boolean rubberBandSelecting = false;
    private DiagramGroup activeGroup = null;
    private boolean draggingGroup = false;
    private final SelectionManager selectionManager = new SelectionManager();
    private final GroupManager groupManager = new GroupManager();

    public DiagramPanel(List<DiagramElement> elements, List<Connector> connectors) {
        this.elements = elements;
        this.connectors = connectors;

        setBackground(Color.WHITE);

        // Mouse press
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                requestFocusInWindow();
                selectedElement = null;
                resizing = false;

                // 1) group hit-test
                activeGroup = groupManager.findGroupAt(e.getX(), e.getY());
                if (activeGroup != null && SwingUtilities.isLeftMouseButton(e)) {
                    draggingGroup = true;
                    dragStart = e.getPoint();
                    // select members
                    selectionManager.clear();
                    for (DiagramElement m : activeGroup.getMembers()) {
                        selectionManager.select(m);
                    }
                    repaint();
                    return;
                }

                // 2) rubber-band select (double-click left button)
                if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e)) {
                    rubberBandSelecting = true;
                    dragStart = e.getPoint();
                    selectionRect = new Rectangle();
                    repaint();
                    return;
                }

                // 3) element click (left button)
                if (SwingUtilities.isLeftMouseButton(e)) {
                    boolean hit = false;
                    for (DiagramElement el : elements) {
                        if (el.isOnResizeHandle(e.getPoint())) {
                            selectedElement = el;
                            resizing = true;
                            dragOffset = e.getPoint();
                            el.setSelected(true);
                            selectionManager.clear();
                            selectionManager.select(el);
                            hit = true;
                            break;
                        } else if (el.contains(e.getPoint())) {
                            selectedElement = el;
                            resizing = false;
                            dragOffset = new Point(e.getX() - el.getX(), e.getY() - el.getY());

                            originalX = el.getX();
                            originalY = el.getY();

                            selectionManager.clear();
                            selectionManager.select(el);
                            el.setSelected(true);

                            // double-click to edit text
                            if (el instanceof TextElement && e.getClickCount() == 2) {
                                TextElement te = (TextElement) el;
                                String newText = JOptionPane.showInputDialog(DiagramPanel.this, "Edit Text:", te.getText());
                                if (newText != null) {
                                    te.setText(newText);
                                }
                            }

                            hit = true;
                            break;
                        } else {
                            el.setSelected(false);
                        }
                    }
                    if (!hit) {
                        selectionManager.clear();
                    }
                    dragStart = e.getPoint();
                }

                // 4) right-click to ungroup
                if (SwingUtilities.isRightMouseButton(e)) {
                    groupManager.ungroupAt(e.getX(), e.getY());
                }

                repaint();
            }


            @Override
            public void mouseReleased(MouseEvent e) {
                // 1) End group drag
                if (draggingGroup) {
                    draggingGroup = false;
                    activeGroup = null;
                    repaint();
                    return;
                }

                // 2) Finalize rubber-band selection
                if (rubberBandSelecting) {
                    selectionManager.clear();
                    for (DiagramElement el : elements) {
                        Rectangle eb = new Rectangle(el.getX(), el.getY(), el.getWidth(), el.getHeight());
                        if (selectionRect.intersects(eb)) {
                            selectionManager.select(el);
                        }
                    }
                }
                rubberBandSelecting = false;
                selectionRect = null;

                // 3) Handle individual element move
                if (selectedElement != null && dragOffset != null && !resizing && controller != null) {
                    int newX = selectedElement.getX();
                    int newY = selectedElement.getY();

                    if (originalX != newX || originalY != newY) {
                        controller.elementMoved(selectedElement, originalX, originalY, newX, newY);
                    }
                }

                // 4) Clear states
                selectedElement = null;
                dragOffset = null;
                resizing = false;
                dragStart = null;

                repaint();
            }

        });

        // Mouse drag
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
           
            public void mouseDragged(MouseEvent e) {
                // rubber-band dragging
                if (rubberBandSelecting && dragStart != null) {
                    int x = Math.min(dragStart.x, e.getX());
                    int y = Math.min(dragStart.y, e.getY());
                    int w = Math.abs(e.getX() - dragStart.x);
                    int h = Math.abs(e.getY() - dragStart.y);
                    selectionRect = new Rectangle(x, y, w, h);

                // dragging a grouped box
                } else if (draggingGroup) {
                    int dx = e.getX() - dragStart.x;
                    int dy = e.getY() - dragStart.y;
                    activeGroup.move(dx, dy);
                    dragStart = e.getPoint();
                    repaint();
                    return;

                // dragging individual selection
                } else if (selectionManager.hasSelection() && dragStart != null) {
                    int dx = e.getX() - dragStart.x;
                    int dy = e.getY() - dragStart.y;
                    for (DiagramElement el : selectionManager.getSelected()) {
                        el.setX(el.getX() + dx);
                        el.setY(el.getY() + dy);
                    }
                    DiagramGroup grp = groupManager.findGroup(selectionManager.getSelected());
                    if (grp != null) grp.move(dx, dy);
                    dragStart = e.getPoint();
                }
                repaint();
            }

        });

        // group/ungroup shortcuts
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control G"), "group");
        getActionMap().put("group", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectionManager.hasSelection()) {
                    groupManager.group(selectionManager.getSelected());
                    repaint();
                }
            }
        });
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control U"), "ungroup");
        getActionMap().put("ungroup", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DiagramGroup g = groupManager.findGroup(selectionManager.getSelected());
                if (g != null) {
                    groupManager.remove(g);
                    selectionManager.clear();
                    repaint();
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        // draw elements
        for (DiagramElement el : elements) el.draw(g2);
        // draw connectors
        for (Connector c : connectors) c.draw(g2);
        // draw rubber-band
        if (selectionRect != null) {
            g2.setColor(new Color(0, 0, 255, 50));
            g2.fill(selectionRect);
            g2.setColor(Color.BLUE);
            g2.draw(selectionRect);
        }
        // // draw groups
        // groupManager.drawAll(g2);
    }

    public void setController(DiagramController controller) {
        this.controller = controller;
    }
}
