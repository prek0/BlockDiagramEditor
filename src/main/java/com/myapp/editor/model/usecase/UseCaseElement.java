package com.myapp.editor.model.usecase;

import com.myapp.editor.model.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

public class UseCaseElement extends DiagramElement {
    public UseCaseElement(int x, int y, String text, int id) {
        super(x, y, text, 120, 80, id);
    }

    @Override
    public void draw(Graphics g) {
        g.drawOval(x, y, width, height);
        g.drawString(text, x + width / 4, y + height / 2);

        if (selected) {
            g.setColor(Color.RED);
            g.drawRect(x - 2, y - 2, width + 4, height + 4);
            g.setColor(Color.BLACK);
        }

        g.setColor(Color.RED);
        g.fillRect(x + width - 10, y + height - 10, 10, 10); 
        g.setColor(Color.BLACK);
    }

    @Override
    public Shape getShape() {
        return new Ellipse2D.Double(x, y, width, height);
    }

    @Override
    public String getType() {
        return "Use Case";
    }
}
