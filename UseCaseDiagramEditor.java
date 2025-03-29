import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

abstract class DiagramElement {
    int x, y, width, height;
    String text;
    boolean selected;
    boolean resizing;

    public DiagramElement(int x, int y, String text, int width, int height) {
        this.x = x;
        this.y = y;
        this.text = text;
        this.width = width;
        this.height = height;
    }

    abstract void draw(Graphics g);

    public boolean contains(int px, int py) {
        return px >= x && px <= x + width && py >= y && py <= y + height;
    }

    public boolean onResizeHandle(int px, int py) {
        int handleSize = 10; 
        return px >= x + width - handleSize && px <= x + width &&
               py >= y + height - handleSize && py <= y + height;
    }
}

class UseCaseElement extends DiagramElement {
    public UseCaseElement(int x, int y, String text) {
        super(x, y, text, 120, 80); // Oval shape: width != height
    }

    public void draw(Graphics g) {
        g.drawOval(x, y, width, height);
        g.drawString(text, x + width / 4, y + height / 2);

        if (selected) {
            g.setColor(Color.RED);
            g.drawRect(x - 2, y - 2, width + 4, height + 4);
            g.setColor(Color.BLACK);
        }

        g.setColor(Color.RED);
        g.fillRect(x + width - 10, y + height - 10, 10, 10); // Resize handle
        g.setColor(Color.BLACK);
    }
}

class ActorElement extends DiagramElement {
    // Base dimensions used for scaling actor parts
    private static final int BASE_WIDTH = 50;
    private static final int BASE_HEIGHT = 130;
    
    public ActorElement(int x, int y, String text) {
        super(x, y, text, BASE_WIDTH, BASE_HEIGHT);
    }
    
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        
        // Draw a transparent bounding box
        g2.setColor(Color.LIGHT_GRAY);
        g2.drawRect(x, y, width, height);
        g2.setColor(Color.BLACK);
        
        // Compute scaling factors based on the current bounding box size
        double scaleX = (double) width / BASE_WIDTH;
        double scaleY = (double) height / BASE_HEIGHT;
        
        // Draw head (oval) scaled within the bounding box.
        int headWidth = (int)(50 * scaleX);
        int headHeight = (int)(50 * scaleY);
        int headX = x + (int)(((BASE_WIDTH - 50) / 2.0) * scaleX);
        int headY = y;
        g2.drawOval(headX, headY, headWidth, headHeight);
        
        // Draw body: vertical line from center of head to (center, 100 in base)
        int bodyX = x + (int)(25 * scaleX);
        int bodyY1 = y + (int)(50 * scaleY);
        int bodyY2 = y + (int)(100 * scaleY);
        g2.drawLine(bodyX, bodyY1, bodyX, bodyY2);
        
        // Draw arms: left and right from near the top of the body
        int armY = y + (int)(60 * scaleY);
        int leftArmX = x;
        int rightArmX = x + (int)(50 * scaleX);
        int armEndY = y + (int)(80 * scaleY);
        g2.drawLine(bodyX, armY, leftArmX, armEndY);
        g2.drawLine(bodyX, armY, rightArmX, armEndY);
        
        // Draw legs: from bottom of body to base of bounding box
        int legY1 = y + (int)(100 * scaleY);
        int leftLegX = x;
        int rightLegX = x + (int)(50 * scaleX);
        int legY2 = y + (int)(130 * scaleY);
        g2.drawLine(bodyX, legY1, leftLegX, legY2);
        g2.drawLine(bodyX, legY1, rightLegX, legY2);
        
        // Draw text below the bounding box
        g2.drawString(text, x, y + height + 15);
        
        if (selected) {
            g2.setColor(Color.RED);
            g2.drawRect(x - 2, y - 2, width + 4, height + 4);
            g2.setColor(Color.BLACK);
        }
        
        // Draw resize handle
        g2.setColor(Color.RED);
        g2.fillRect(x + width - 10, y + height - 10, 10, 10);
        g2.setColor(Color.BLACK);
    }
    
    @Override
    public boolean onResizeHandle(int px, int py) {
        int handleSize = 10;
        return px >= x + width - handleSize && px <= x + width &&
               py >= y + height - handleSize && py <= y + height;
    }
}

class SystemBoundary extends DiagramElement {
    public SystemBoundary(int x, int y, String text, int width, int height) {
        super(x, y, text, width, height);
    }
    
    public void draw(Graphics g) {
        g.drawRect(x, y, width, height);
        g.drawString(text, x + 5, y + 15);

        g.setColor(Color.RED);
        g.fillRect(x + width - 10, y + height - 10, 10, 10); // Resize handle
        g.setColor(Color.BLACK);
    }
}

class TextElement extends DiagramElement {
    public TextElement(int x, int y, String text) {
        super(x, y, text, 100, 20);
    }
    
    public void draw(Graphics g) {
        g.drawString(text, x, y);
        if (selected) {
            g.setColor(Color.RED);
            g.drawRect(x - 2, y - 2, width + 4, height + 4);
            g.setColor(Color.BLACK);
        }
    }
}

class Connector {
    DiagramElement source;
    DiagramElement target;
    String type; // For connectors between use cases ("include"/"extend")

    public Connector(DiagramElement source, DiagramElement target, String type) {
        this.source = source;
        this.target = target;
        this.type = type;
    }

    // Helper: Get the connection point on an ellipse (UseCaseElement)
    private Point getEllipseConnectionPoint(DiagramElement element, int tx, int ty) {
        double cx = element.x + element.width / 2.0;
        double cy = element.y + element.height / 2.0;
        double dx = tx - cx;
        double dy = ty - cy;
        double a = element.width / 2.0;
        double b = element.height / 2.0;
        double t = 1.0 / Math.sqrt((dx * dx) / (a * a) + (dy * dy) / (b * b));
        int newX = (int)(cx + dx * t);
        int newY = (int)(cy + dy * t);
        return new Point(newX, newY);
    }

    // Helper: Get the connection point on a rectangle (for ActorElement, SystemBoundary, TextElement)
    private Point getRectConnectionPoint(DiagramElement element, int tx, int ty) {
        double cx = element.x + element.width / 2.0;
        double cy = element.y + element.height / 2.0;
        double dx = tx - cx;
        double dy = ty - cy;
        double a = element.width / 2.0;
        double b = element.height / 2.0;
        double t = Double.MAX_VALUE;
        if(dx != 0)
            t = Math.min(t, Math.abs(a / dx));
        if(dy != 0)
            t = Math.min(t, Math.abs(b / dy));
        int newX = (int)(cx + dx * t);
        int newY = (int)(cy + dy * t);
        return new Point(newX, newY);
    }
    
    // Get connection point depending on element type
    private Point getConnectionPoint(DiagramElement element, int tx, int ty) {
        if (element instanceof UseCaseElement) {
            return getEllipseConnectionPoint(element, tx, ty);
        } else {
            return getRectConnectionPoint(element, tx, ty);
        }
    }
    
    // Draw an arrow head at 'tip', coming from 'tail'
    private void drawArrowHead(Graphics2D g2, Point tip, Point tail) {
        double phi = Math.toRadians(30);
        int barb = 10;
        double dy = tip.y - tail.y;
        double dx = tip.x - tail.x;
        double theta = Math.atan2(dy, dx);
        for (int j = 0; j < 2; j++) {
            double rho = theta + (j == 0 ? phi : -phi);
            int x = (int)(tip.x - barb * Math.cos(rho));
            int y = (int)(tip.y - barb * Math.sin(rho));
            g2.drawLine(tip.x, tip.y, x, y);
        }
    }

public void draw(Graphics g) {
    Graphics2D g2 = (Graphics2D) g;
    // Determine style: if connecting two use cases then dotted, else solid
    boolean useDotted = (source instanceof UseCaseElement && target instanceof UseCaseElement);
    Stroke oldStroke = g2.getStroke();
    if (useDotted) {
        float[] dash = {5.0f};
        g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER, 5.0f, dash, 0.0f));
    }
    // Compute connection points (arrow from source edge to target edge)
    Point sourceCenter = new Point(source.x + source.width / 2, source.y + source.height / 2);
    Point targetCenter = new Point(target.x + target.width / 2, target.y + target.height / 2);
    Point sourcePt = getConnectionPoint(source, targetCenter.x, targetCenter.y);
    Point targetPt = getConnectionPoint(target, sourceCenter.x, sourceCenter.y);
    
    // Draw line from sourcePt to targetPt
    g2.drawLine(sourcePt.x, sourcePt.y, targetPt.x, targetPt.y);
    g2.setStroke(oldStroke);
    
    // Draw arrow head at target
    drawArrowHead(g2, targetPt, sourcePt);
    
    // **Do not draw any text here for connectors**
}

}

public class UseCaseDiagramEditor extends JPanel {
    private ArrayList<DiagramElement> elements = new ArrayList<>();
    private ArrayList<Connector> connectors = new ArrayList<>();
    private DiagramElement selectedElement = null;
    private int offsetX, offsetY;
    private boolean resizing = false;
    
    // For creating connectors
    private DiagramElement connectorSource = null;
    
    public UseCaseDiagramEditor() {
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                // If Ctrl is held, use click for connector creation
                if (e.isControlDown()) {
                    for (DiagramElement element : elements) {
                        if (element.contains(e.getX(), e.getY())) {
                            if (connectorSource == null) {
                                connectorSource = element;
                                element.selected = true;
                                repaint();
                            } else if (connectorSource != element) {
                                // For connectors between use cases, use "include"/"extend" label (dotted)
                                // For connectors between use case and actor, use "normal" (solid)
                                String[] types;
                                if ((connectorSource instanceof UseCaseElement && element instanceof UseCaseElement)) {
                                    types = new String[]{"<<include>>", "<<extend>>"};
                                } else {
                                    types = new String[]{"normal"};
                                }
                                String type = (String) JOptionPane.showInputDialog(
                                    null, "Select connector type:", "Connector Type",
                                    JOptionPane.QUESTION_MESSAGE, null, types, types[0]);
                                if (type != null) {
                                    connectors.add(new Connector(connectorSource, element, type));
                                }
                                connectorSource.selected = false;
                                connectorSource = null;
                                repaint();
                            }
                            return;
                        }
                    }
                } else {
                    // Normal selection/resizing code:
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
                            repaint();
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
                                    elements.add(new ActorElement(e.getX(), e.getY(), name));
                                    break;
                                case "Use Case":
                                    elements.add(new UseCaseElement(e.getX(), e.getY(), name));
                                    break;
                                case "System Boundary":
                                    elements.add(new SystemBoundary(e.getX(), e.getY(), name, 200, 150));
                                    break;
                                case "Text":
                                    elements.add(new TextElement(e.getX(), e.getY(), name));
                                    break;
                            }
                            repaint();
                        }
                    }
                }
            }
            
            public void mouseReleased(MouseEvent e) {
                if (selectedElement != null) {
                    selectedElement.selected = false;
                    selectedElement = null;
                    resizing = false;
                    repaint();
                }
            }
        });
        
        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                if (selectedElement != null) {
                    if (resizing) {
                        int newWidth = e.getX() - selectedElement.x;
                        int newHeight = e.getY() - selectedElement.y;
                        
                        // Minimum size limits:
                        if (selectedElement instanceof UseCaseElement) {
                            if(newWidth < 60) newWidth = 60;
                            double aspectRatio = 1.5;
                            newHeight = (int)(newWidth / aspectRatio);
                            if(newHeight < 40) newHeight = 40;
                        } else if (selectedElement instanceof ActorElement) {
                            if(newWidth < 40) newWidth = 40;
                            if(newHeight < 80) newHeight = 80;
                        } else if (selectedElement instanceof SystemBoundary) {
                            if(newWidth < 100) newWidth = 100;
                            if(newHeight < 80) newHeight = 80;
                        } else {
                            if(newWidth < 30) newWidth = 30;
                            if(newHeight < 30) newHeight = 30;
                        }
                        
                        selectedElement.width = newWidth;
                        selectedElement.height = newHeight;
                    } else {
                        selectedElement.x = e.getX() - offsetX;
                        selectedElement.y = e.getY() - offsetY;
                    }
                    repaint();
                }
            }
        });
        
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (selectedElement != null && e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String newText = JOptionPane.showInputDialog("Edit Text:", selectedElement.text);
                    if (newText != null) {
                        selectedElement.text = newText;
                        repaint();
                    }
                }
            }
        });
        setFocusable(true);
    }
    
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw connectors first so they appear behind elements
        for (Connector conn : connectors) {
            conn.draw(g);
        }
        // Then draw diagram elements
        for (DiagramElement element : elements) {
            element.draw(g);
        }
    }
    
    public static void main(String[] args) {
        JFrame frame = new JFrame("Use Case Diagram Editor");
        UseCaseDiagramEditor editor = new UseCaseDiagramEditor();
        frame.add(editor);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
