package com.myapp.editor.model;

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
    public abstract Shape getShape();

    public boolean contains(Point p) {
        return new Rectangle(x, y, width, height).contains(p);
    }     

    // public boolean contains(int px, int py) {
    //     return px >= x && px <= x + width && py >= y && py <= y + height;
    // }

    public boolean containsPoint(int mouseX, int mouseY) {
        return getShape().contains(mouseX, mouseY);
    }

    public boolean onResizeHandle(int px, int py) {
        int handleSize = 10;
        return px >= x + width - handleSize && px <= x + width &&
               py >= y + height - handleSize && py <= y + height;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public void setX(int x) {
        this.x = x;
    }
    
    public void setY(int y) {
        this.y = y;
    }
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void setWidth(int width) {
        this.width = width;
    }
    
    public void setHeight(int height) {
        this.height = height;
    }
    public int getWidth() { return width; }
    public int getHeight() { return height; }

    public boolean isSelected() {
        return selected;
    }
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public abstract String getType();

    public boolean isOnResizeHandle(Point p) {
        int handleSize = 10;
        int handleX = x + width - handleSize;
        int handleY = y + height - handleSize;
        return new Rectangle(handleX, handleY, handleSize, handleSize).contains(p);
    }
    @Override
    public DiagramElement clone() {
        try {
            return (DiagramElement) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("Cloning not supported", e);
        }
    }
}
