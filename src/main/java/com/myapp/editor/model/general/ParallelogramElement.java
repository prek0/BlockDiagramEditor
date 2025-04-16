package model.general;

import model.DiagramElement;
import java.awt.*;
import java.awt.geom.Rectangle2D;

public class ParallelogramElement extends DiagramElement {
    public ParallelogramElement (int x, int y, String text, int width, int height, int id) {
        super(x, y, text, width, height, id);
    }

    @Override
    public void draw(Graphics g) {
        int offset = width / 5;
        int[] xPoints = { x + offset, x + width, x + width - offset, x };
        int[] yPoints = { y, y, y + height, y + height };

        g.setColor(selected ? Color.BLUE : Color.BLACK);
        g.drawPolygon(xPoints, yPoints, 4);
        g.drawString(text, x + offset / 2, y + height / 2);

        if (resizing) {
            g.setColor(Color.RED);
            g.fillRect(x + width - 10, y + height - 10, 10, 10);
        }
    }

    @Override
    public Shape getShape() {
        return new Rectangle2D.Double(x, y, width, height); 
    }
}