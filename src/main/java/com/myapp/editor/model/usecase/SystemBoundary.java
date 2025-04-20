package com.myapp.editor.model.usecase;

import com.myapp.editor.model.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;

public class SystemBoundary extends DiagramElement {
    public SystemBoundary(int x, int y, String text, int width, int height, int id) {
        super(x, y, text, width, height, id);  // Add id to the constructor call
    }
    
    @Override
    public void draw(Graphics g) {
        g.drawRect(x, y, width, height);
        g.drawString(text, x + 5, y + 15);

        g.setColor(Color.RED);
        g.fillRect(x + width - 10, y + height - 10, 10, 10); // Resize handle
        g.setColor(Color.BLACK);
    }

    @Override
    public Shape getShape() {
        return new Rectangle2D.Double(x, y, width, height); 
    }

    @Override
    public String getType() {
        return "System Boundary";
    }
}
