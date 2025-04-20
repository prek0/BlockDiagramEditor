package com.myapp.editor.model.state;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.geom.Rectangle2D;
import com.myapp.editor.model.DiagramElement;

public class StateElement extends DiagramElement {
    private static final int ARC_RADIUS = 20;

    public StateElement(int x, int y, String text, int width, int height, int id) {
        super(x, y, text, width, height, id);
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        // shape
        RoundRectangle2D rect = new RoundRectangle2D.Double(x, y, width, height, ARC_RADIUS, ARC_RADIUS);
        g2.setColor(Color.WHITE);
        g2.fill(rect);
        g2.setColor(Color.BLACK);
        g2.draw(rect);

        // text centered
        if (text != null && !text.isEmpty()) {
            FontMetrics fm = g2.getFontMetrics();
            Rectangle2D tb = fm.getStringBounds(text, g2);
            int tx = x + (int)((width - tb.getWidth()) / 2);
            int ty = y + (int)((height - tb.getHeight()) / 2) + fm.getAscent();
            g2.drawString(text, tx, ty);
        }

        if (selected) {
            g2.setStroke(new BasicStroke(2));
            g2.setColor(Color.BLUE);
            g2.draw(new RoundRectangle2D.Double(x - 2, y - 2, width + 4, height + 4, ARC_RADIUS, ARC_RADIUS));
        }

        g.setColor(Color.RED);
        g.fillRect(x + width - 10, y + height - 10, 10, 10); 
        g.setColor(Color.BLACK);
    }

    @Override
    public Shape getShape() {
        return new RoundRectangle2D.Double(x, y, width, height, ARC_RADIUS, ARC_RADIUS);
    }

    @Override
    public String getType() {
        return "StateElement";
    }
}
