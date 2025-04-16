package model;

import java.awt.*;
import java.io.Serializable;

public abstract class DiagramElement implements Serializable {
    public int x, y, width, height;
    public String text;
    public boolean selected;
    public boolean resizing;
    public int id;  // Add this field to uniquely identify elements

    public DiagramElement(int x, int y, String text, int width, int height, int id) {
        this.x = x;
        this.y = y;
        this.text = text;
        this.width = width;
        this.height = height;
        this.id = id;
    }

    public abstract void draw(Graphics g);
    
    public boolean contains(int px, int py) {
        return px >= x && px <= x + width && py >= y && py <= y + height;
    }

    public boolean onResizeHandle(int px, int py) {
        int handleSize = 10;
        return px >= x + width - handleSize && px <= x + width &&
               py >= y + height - handleSize && py <= y + height;
    }
}
