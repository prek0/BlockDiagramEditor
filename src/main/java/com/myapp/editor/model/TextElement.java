package model;

import java.awt.Color;
import java.awt.Graphics;

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
}
