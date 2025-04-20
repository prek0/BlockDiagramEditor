package com.myapp.editor.model;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.io.Serializable;
import com.myapp.editor.model.usecase.*;

public class Connector implements Serializable{
    public DiagramElement source;
    public DiagramElement target;
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