package com.myapp.editor.model;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;

public class TextElement extends DiagramElement {
    public TextElement(int x, int y, String text, int id) {
        super(x, y, text, 100, 20, id);  // Add id to the constructor call
    }
    
    @Override
    public void draw(Graphics g) {
        g.drawString(text, x, y);
        if (selected) {
            g.setColor(Color.RED);
            g.drawRect(x - 2, y - 2, width + 4, height + 4);
            g.setColor(Color.BLACK);
        }
    }

    @Override
    public Shape getShape() {
        return new Rectangle2D.Double(x, y, width, height); 
    }

    @Override
    public String getType() {
        return "Text label";
    }
}
