package com.myapp.editor.model.general;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

import com.myapp.editor.model.DiagramElement;

public class EllipseElement extends DiagramElement {

    public EllipseElement(int x, int y, String text, int width, int height, int id) {
        super(x, y, text, width, height, id);
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(selected ? Color.BLUE : Color.BLACK);
        g.drawOval(x, y, width, height);
        g.drawString(text, x + 5, y + height / 2);

        if (resizing) {
            g.setColor(Color.RED);
            g.fillRect(x + width - 10, y + height - 10, 10, 10);
        }
        if (selected) {
            g.setColor(Color.RED);
            g.drawRect(x - 2, y - 2, width + 4, height + 4);
            g.setColor(Color.BLACK);

            // Draw resize handle
            g.setColor(Color.RED);
            g.fillRect(x + width - 10, y + height - 10, 10, 10);
            g.setColor(Color.BLACK);
        }
    }
    

    @Override
    public Shape getShape() {
        return new Ellipse2D.Double(x, y, width, height);
    }

    @Override
    public String getType() {
        return "Ellipse";
    }
}