package com.myapp.editor.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import com.myapp.editor.controller.DiagramController;
import com.myapp.editor.controller.command.CommandManager;
import com.myapp.editor.controller.command.DeleteCommand;
import com.myapp.editor.model.Connector;
import com.myapp.editor.model.DiagramElement;
import com.myapp.editor.model.DiagramGroup;
import com.myapp.editor.model.DiagramModel;
import com.myapp.editor.model.GroupManager;
import com.myapp.editor.model.SelectionManager;
import com.myapp.editor.model.TextElement;

public class DiagramPanel extends JPanel {

    private DiagramElement selectedElement = null;
    private Point dragOffset = null;
    private boolean resizing = false;
    private DiagramController controller;
    private int originalX, originalY;
    private CommandManager commandManager;
    private DiagramModel model;


    //grouping
    private Point dragStart = null;
    private Rectangle selectionRect = null;
    private boolean rubberBandSelecting = false;
    private DiagramGroup activeGroup = null;
    private boolean draggingGroup = false;
    private final SelectionManager selectionManager = new SelectionManager();
    private final GroupManager groupManager = new GroupManager();
    private Map<DiagramElement, Point> originalGroupPositions;

    //connector
    private DiagramElement connectorSource = null;
    private boolean drawingConnector = false;
    private Point currentMousePoint = null;
    // private List<Connector> connectors = new ArrayList<>();

    // zoom factor
    private double zoomFactor = 1.0;

    public DiagramPanel(DiagramModel model, CommandManager commandManager) {
        this.model = model;
        this.commandManager = commandManager;

        setBackground(Color.WHITE);

        // Add key listener for delete
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_DELETE) {
                    deleteSelectedElements();
                }
                // Zoom In (Ctrl + '+')
                if (e.isControlDown() && 
                    (e.getKeyCode() == KeyEvent.VK_PLUS || 
                    (e.getKeyCode() == KeyEvent.VK_EQUALS && e.isShiftDown()))) {
                        zoomIn();
                }

                // Zoom Out (Ctrl + '-')
                if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_MINUS) {
                    zoomOut();
                }
            }
        });

        // Mouse press
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                requestFocusInWindow();
                selectedElement = null;
                resizing = false;

                // 1) group hit-test
                activeGroup = groupManager.findGroupAt(e.getX(), e.getY());
                // if (activeGroup != null && SwingUtilities.isLeftMouseButton(e)) {
                //     draggingGroup = true;
                //     dragStart = e.getPoint();
                //     // select members
                //     selectionManager.clear();
                //     for (DiagramElement m : activeGroup.getMembers()) {
                //         selectionManager.select(m);
                //     }
                //     repaint();
                //     return;
                // }

                if (activeGroup != null && SwingUtilities.isLeftMouseButton(e)) {
                    draggingGroup = true;
                    dragStart = e.getPoint();

                    // Store original positions for undo
                    originalGroupPositions = new HashMap<>();
                    for (DiagramElement m : activeGroup.getMembers()) {
                        originalGroupPositions.put(m, new Point(m.getX(), m.getY()));
                    }

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
                    for (DiagramElement el : model.getElements()) {
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

                // 4) start connector drawing (Shift + left click on element)
                if (SwingUtilities.isLeftMouseButton(e) && e.isShiftDown()) {
                    for (DiagramElement el : model.getElements()) {
                        if (el.contains(e.getPoint())) {
                            connectorSource = el;
                            drawingConnector = true;
                            dragStart = e.getPoint();
                            repaint();
                            return;
                        }
                    }
                }

                repaint();
            }


            @Override
            public void mouseReleased(MouseEvent e) {
                // 1) End group drag
                // if (draggingGroup) {
                //     draggingGroup = false;
                //     activeGroup = null;
                //     repaint();
                //     return;
                // }

                if (draggingGroup) {
                    draggingGroup = false;
                
                    if (controller != null && originalGroupPositions != null && !originalGroupPositions.isEmpty()) {
                        Map<DiagramElement, Point> newPositions = new HashMap<>();
                        for (Map.Entry<DiagramElement, Point> entry : originalGroupPositions.entrySet()) {
                            DiagramElement el = entry.getKey();
                            Point oldPos = entry.getValue();
                            Point newPos = new Point(el.getX(), el.getY());
                
                            // Only store elements that were actually moved
                            if (!oldPos.equals(newPos)) {
                                newPositions.put(el, newPos);
                            }
                        }
                
                        // Only execute command if something actually moved
                        if (!newPositions.isEmpty()) {
                            controller.groupMoved(originalGroupPositions, newPositions);
                        }
                    }
                
                    activeGroup = null;
                    originalGroupPositions = null;
                    repaint();
                    return;
                }
                

                // 2) Finalize rubber-band selection
                if (rubberBandSelecting) {
                    selectionManager.clear();
                    for (DiagramElement el : model.getElements()) {
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

                // 5) finish connector drawing
                if (drawingConnector && connectorSource != null) {
                    for (DiagramElement el : model.getElements()) {
                        if (el.contains(e.getPoint()) && el != connectorSource) {
                            // if (controller != null) {
                            //     AddConnectorCommand cmd = controller.createConnectorCommand(connectorSource, el);
                            //     commandManager.executeCommand(cmd); 
                            // }
                            // if (controller != null) {
                            //     controller.handleConnectorCreation(connectorSource, el);  // Pass to the controller
                            // }
                            if (controller != null) {
                                controller.addConnector(controller.getSelectedConnectorType(), connectorSource, el);
                            }
                            break;
                        }
                    }
                    drawingConnector = false;
                    connectorSource = null;
                    dragStart = null;
                    repaint();
                }



                repaint();
            }

        });

        // Mouse drag
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                // 1. drawing a connector should have highest priority
                if (drawingConnector && dragStart != null) {
                    currentMousePoint = e.getPoint(); 
                    repaint();
                    return;
                }

                // 2. resizing an element
                if (resizing && selectedElement != null && dragOffset != null) {
                    int newWidth = Math.max(10, e.getX() - selectedElement.getX()); 
                    int newHeight = Math.max(10, e.getY() - selectedElement.getY());
                    selectedElement.setWidth(newWidth);
                    selectedElement.setHeight(newHeight);
                    repaint();
                    return;
                }

                // 3. rubber-band selecting
                if (rubberBandSelecting && dragStart != null) {
                    int x = Math.min(dragStart.x, e.getX());
                    int y = Math.min(dragStart.y, e.getY());
                    int w = Math.abs(e.getX() - dragStart.x);
                    int h = Math.abs(e.getY() - dragStart.y);
                    selectionRect = new Rectangle(x, y, w, h);
                    repaint();
                    return;
                }

                // 4. dragging a grouped box
                if (draggingGroup && activeGroup != null && dragOffset != null) {
                    int dx = e.getX() - dragStart.x;
                    int dy = e.getY() - dragStart.y;
                    //activeGroup.move(dx, dy);

                    // Save original positions only once
                    if (originalGroupPositions == null) {
                        originalGroupPositions = new HashMap<>();
                        for (DiagramElement el : activeGroup.getMembers()) {
                            originalGroupPositions.put(el, new Point(el.getX(), el.getY()));
                        }
                    }
                
                    // Move each group member
                    for (DiagramElement el : activeGroup.getMembers()) {
                        el.setPosition(el.getX() + dx, el.getY() + dy);
                    }
                
                    dragOffset = e.getPoint();
                    dragStart = e.getPoint();
                    repaint();
                    return;
                }   
                

                // 5. dragging individual selection
                if (selectionManager.hasSelection() && dragStart != null) {
                    int dx = e.getX() - dragStart.x;
                    int dy = e.getY() - dragStart.y;
                    for (DiagramElement el : selectionManager.getSelected()) {
                        el.setX(el.getX() + dx);
                        el.setY(el.getY() + dy);
                    }
                    DiagramGroup grp = groupManager.findGroup(selectionManager.getSelected());
                    if (grp != null) grp.move(dx, dy);
                    dragStart = e.getPoint();
                    repaint();
                }
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

    public void addTextElement(String text, int x, int y) {
        int defaultFontSize = 14; // or whatever default size you want
        TextElement textElement = new TextElement(x, y, text, defaultFontSize);
        model.addElement(textElement);
        repaint();
    }

    // Zoom In method
    private void zoomIn() {
        zoomFactor *= 1.1;  // Increase zoom by 10%
        repaint();
    }

    // Zoom Out method
    private void zoomOut() {
        zoomFactor /= 1.1;  // Decrease zoom by 10%
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.scale(zoomFactor, zoomFactor);  

        // 1. draw all elements
        for (DiagramElement el : model.getElements()) {
            el.draw(g2d);
        }

        // 2. draw all connectors
        for (Connector c : model.getConnectors()) {
            c.draw(g2d);
        }

        // 3. draw selection rectangle
        if (rubberBandSelecting && selectionRect != null) {
            g2d.setColor(Color.BLUE);
            g2d.draw(selectionRect);
        }

        // 4. draw dragging connector if in progress
        if (drawingConnector && connectorSource != null && currentMousePoint != null) {
            g2d.setColor(Color.GRAY);
            g2d.setStroke(new BasicStroke(2));
            Point start = connectorSource.getCenter(); // your element should have a getCenter()
            g2d.drawLine(start.x, start.y, currentMousePoint.x, currentMousePoint.y);
        }
    }

    // Method to delete selected elements
    private void deleteSelectedElements() {
        if (!selectionManager.hasSelection()) {
            return;
        }

        // Expand to include full group if needed
        Set<DiagramElement> selectedElements = selectionManager.getSelected();
        for (DiagramElement element : selectedElements) {
            DiagramGroup group = groupManager.findGroup(Set.of(element));
            if (group != null) {
                for (DiagramElement member : group.getMembers()) {
                    selectionManager.select(member);
                }
            }
        }

        selectedElements = selectionManager.getSelected();

        // Gather connectors to delete
        Set<Connector> connectorsToRemove = new HashSet<>();
        for (Connector conn : model.getConnectors()) {
            if (selectionManager.getSelectedConnectors().contains(conn) ||
                selectedElements.contains(conn.getSource()) ||
                selectedElements.contains(conn.getTarget())) {
                connectorsToRemove.add(conn);
            }
        }

        // Create and execute delete command
        DeleteCommand cmd = new DeleteCommand(model, selectedElements, connectorsToRemove, groupManager);
        commandManager.executeCommand(cmd);

        selectionManager.clear();
        repaint();
    }

    public SelectionManager getSelectionManager() {
        return selectionManager;
    }

    public void setController(DiagramController controller) {
        this.controller = controller;
    }
}
