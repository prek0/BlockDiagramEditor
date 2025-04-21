package com.myapp.editor.model.classDia;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;

import com.myapp.editor.model.DiagramElement;

public class PackageBox extends DiagramElement {

    public PackageBox(int x, int y, String text, int width, int height, int id) {
        super(x, y, text, width, height, id);
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(new Color(245, 245, 220));
        g.fillRect(x, y, width, height);
        g.setColor(Color.BLACK);
        g.drawRect(x, y, width, height);
        
        Font original = g.getFont();
        Font italic = original.deriveFont(Font.ITALIC);
        g.setFont(italic);
        g.drawString("Package", x + 10, y + 20); 
        g.drawString(text, x + 10, y + 40); 
        g.setFont(original);
    
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
        return new Rectangle(x, y, width, height);
    }

    @Override
    public String getType() {
        return "PackageBox";
    }
}
