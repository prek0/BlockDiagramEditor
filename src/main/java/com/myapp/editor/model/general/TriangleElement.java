package com.myapp.editor.model.general;

import com.myapp.editor.model.DiagramElement;
import java.awt.*;
import java.awt.geom.Rectangle2D;

public class TriangleElement extends DiagramElement {
    public TriangleElement (int x, int y, String text, int width, int height, int id) {
        super(x, y, text, width, height, id);
    }

    @Override
    public void draw(Graphics g) {
        int[] xPoints = { x + width / 2, x + width, x };
        int[] yPoints = { y, y + height, y + height };

        g.setColor(selected ? Color.BLUE : Color.BLACK);
        g.drawPolygon(xPoints, yPoints, 3);
        g.drawString(text, x + width / 4, y + height / 2);

        if (resizing) {
            g.setColor(Color.RED);
            g.fillRect(x + width - 10, y + height - 10, 10, 10);
        }
    }

    @Override
    public Shape getShape() {
        return new Rectangle2D.Double(x, y, width, height); 
    }

    @Override
    public String getType() {
        return "Triangle";
    }
}