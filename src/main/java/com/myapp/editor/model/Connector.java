package com.myapp.editor.model;

import java.awt.*;
import java.io.Serializable;

public class Connector implements Serializable {
    private DiagramElement source;
    private DiagramElement target;

    public Connector(DiagramElement source, DiagramElement target) {
        this.source = source;
        this.target = target;
    }

    public DiagramElement getSource() {
        return source;
    }

    public DiagramElement getTarget() {
        return target;
    }

    public void draw(Graphics g) {
        if (source != null && target != null) {
            int x1 = source.getX() + source.getWidth() / 2;
            int y1 = source.getY() + source.getHeight() / 2;
            int x2 = target.getX() + target.getWidth() / 2;
            int y2 = target.getY() + target.getHeight() / 2;

            g.setColor(Color.BLACK);
            g.drawLine(x1, y1, x2, y2);

            // Optional: draw arrowhead
            drawArrow(g, x1, y1, x2, y2);
        }
    }

    protected void drawArrow(Graphics g, int x1, int y1, int x2, int y2) {
        int ARR_SIZE = 8;
        Graphics2D g2d = (Graphics2D) g.create();
        double dx = x2 - x1, dy = y2 - y1;
        double angle = Math.atan2(dy, dx);
        int len = (int) Math.sqrt(dx * dx + dy * dy);
        g2d.translate(x1, y1);
        g2d.rotate(angle);
        g2d.drawLine(0, 0, len, 0);
        g2d.fillPolygon(new int[]{len, len - ARR_SIZE, len - ARR_SIZE, len},
                new int[]{0, -ARR_SIZE, ARR_SIZE, 0}, 4);
        g2d.dispose();
    }
}
