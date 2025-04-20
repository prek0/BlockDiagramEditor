package com.myapp.editor.model.general;

import com.myapp.editor.model.DiagramElement;
import java.awt.*;
import java.awt.geom.Rectangle2D;

public class CylinderElement extends DiagramElement {
    public CylinderElement(int x, int y, String text, int width, int height, int id) {
        super(x, y, text, width, height, id);
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(selected ? Color.BLUE : Color.BLACK);
        g.drawOval(x, y, width, 20); // top ellipse
        g.drawLine(x, y + 10, x, y + height - 10);
        g.drawLine(x + width, y + 10, x + width, y + height - 10);
        g.drawOval(x, y + height - 20, width, 20); // bottom ellipse
        g.drawString(text, x + 5, y + height / 2);

        if (resizing) {
            g.setColor(Color.RED);
            g.fillRect(x + width - 10, y + height - 10, 10, 10);
        }
    }

    @Override
    public Shape getShape() {
        return new Rectangle2D.Double(x, y, width, height); // Or adjust to match visual bounds
    }

    @Override
    public String getType() {
        return "Cylinder";
    }
}