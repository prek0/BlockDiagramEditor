package com.myapp.editor.model.classDia;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;

import com.myapp.editor.model.DiagramElement;

public class InterfaceBox extends DiagramElement {

    public InterfaceBox(int x, int y, String text, int width, int height, int id) {
        super(x, y, text, width, height, id);
    }

    @Override
    public void draw(Graphics g) {
        // Background
        g.setColor(Color.WHITE);
        g.fillRect(x, y, width, height);

        // Border
        g.setColor(Color.BLACK);
        g.drawRect(x, y, width, height);

        // Header: <<interface>> and name
        Font original = g.getFont();
        Font italic = original.deriveFont(Font.ITALIC);
        g.setFont(italic);
        g.drawString("<<interface>>", x + 10, y + 20); 
        g.drawString(text, x + 10, y + 40); 
        g.setFont(original);

        // Section lines
        int titleHeight = 50;
        int middleLineY = y + titleHeight;
        int bottomLineY = y + (height + titleHeight) / 2;

        g.drawLine(x, middleLineY, x + width, middleLineY);       // Line below title
        g.drawLine(x, bottomLineY, x + width, bottomLineY);       // Line below attributes

        // Placeholder section text
        g.drawString("", x + 10, middleLineY + 20);
        g.drawString("", x + 10, bottomLineY + 20);

        // Selection visual
        if (selected) {
            g.setColor(Color.RED);
            g.drawRect(x - 2, y - 2, width + 4, height + 4);

            // Resize handle
            g.fillRect(x + width - 10, y + height - 10, 10, 10);
            g.setColor(Color.BLACK);
        }
    }

    @Override
    public Shape getShape() {
        return new Rectangle(x, y, width, height);
    }

    @Override
    public String getType() {
        return "InterfaceBox";
    }
}
