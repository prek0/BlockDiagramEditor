package com.myapp.editor.model.classDia;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;

import com.myapp.editor.model.DiagramElement;

public class ClassBox extends DiagramElement {

    public ClassBox(int x, int y, String text, int width, int height, int id) {
        super(x, y, text, width, height, id);
    }

    @Override
    public void draw(Graphics g) {
        // Fill background
        g.setColor(Color.WHITE);
        g.fillRect(x, y, width, height);

        // Draw outer border
        g.setColor(Color.BLACK);
        g.drawRect(x, y, width, height);

        // Draw text (class name) in top section
        g.drawString(text, x + 10, y + 20);

        // Calculate section heights
        int titleHeight = 30;
        int middleLineY = y + titleHeight;
        int bottomLineY = y + (height + titleHeight) / 2;

        // Draw section dividers
        g.drawLine(x, middleLineY, x + width, middleLineY); // Between title and attributes
        g.drawLine(x, bottomLineY, x + width, bottomLineY); // Between attributes and methods

        // Highlight if selected
        if (selected) {
            g.setColor(Color.RED);
            g.drawRect(x - 2, y - 2, width + 4, height + 4);

            // Draw resize handle
            g.fillRect(x + width - 10, y + height - 10, 10, 10);

            g.setColor(Color.BLACK); // Reset color
        }
    }

    @Override
    public Shape getShape() {
        return new Rectangle(x, y, width, height);
    }

    @Override
    public String getType() {
        return "ClassBox";
    }
}
