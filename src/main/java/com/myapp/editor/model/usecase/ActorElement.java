package com.myapp.editor.model.usecase;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;

import com.myapp.editor.model.DiagramElement;

public class ActorElement extends DiagramElement {
    // Base dimensions used for scaling actor parts
    private static final int BASE_WIDTH = 50;
    private static final int BASE_HEIGHT = 130;
    
    public ActorElement(int x, int y, String text, int id) {
        super(x, y, text, BASE_WIDTH, BASE_HEIGHT, id); 
    }
    
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        
        // Draw a transparent bounding box
        g2.setColor(Color.LIGHT_GRAY);
        g2.drawRect(x, y, width, height);
        g2.setColor(Color.BLACK);
        
        // Compute scaling factors based on the current bounding box size
        double scaleX = (double) width / BASE_WIDTH;
        double scaleY = (double) height / BASE_HEIGHT;
        
        // Draw head (oval) scaled within the bounding box.
        int headWidth = (int)(50 * scaleX);
        int headHeight = (int)(50 * scaleY);
        int headX = x + (int)(((BASE_WIDTH - 50) / 2.0) * scaleX);
        int headY = y;
        g2.drawOval(headX, headY, headWidth, headHeight);
        
        // Draw body: vertical line from center of head to (center, 100 in base)
        int bodyX = x + (int)(25 * scaleX);
        int bodyY1 = y + (int)(50 * scaleY);
        int bodyY2 = y + (int)(100 * scaleY);
        g2.drawLine(bodyX, bodyY1, bodyX, bodyY2);
        
        // Draw arms: left and right from near the top of the body
        int armY = y + (int)(60 * scaleY);
        int leftArmX = x;
        int rightArmX = x + (int)(50 * scaleX);
        int armEndY = y + (int)(80 * scaleY);
        g2.drawLine(bodyX, armY, leftArmX, armEndY);
        g2.drawLine(bodyX, armY, rightArmX, armEndY);
        
        // Draw legs: from bottom of body to base of bounding box
        int legY1 = y + (int)(100 * scaleY);
        int leftLegX = x;
        int rightLegX = x + (int)(50 * scaleX);
        int legY2 = y + (int)(130 * scaleY);
        g2.drawLine(bodyX, legY1, leftLegX, legY2);
        g2.drawLine(bodyX, legY1, rightLegX, legY2);
        
        // Draw text below the bounding box
        g2.drawString(text, x, y + height + 15);
        
        if (selected) {
            g2.setColor(Color.RED);
            g2.drawRect(x - 2, y - 2, width + 4, height + 4);
            g2.setColor(Color.BLACK);

            // Draw resize handle
            g2.setColor(Color.RED);
            g2.fillRect(x + width - 10, y + height - 10, 10, 10);
            g2.setColor(Color.BLACK);
        }
        
    }
    
    @Override
    public boolean onResizeHandle(int px, int py) {
        int handleSize = 10;
        return px >= x + width - handleSize && px <= x + width &&
               py >= y + height - handleSize && py <= y + height;
    }

    @Override
    public Shape getShape() {
        return new Rectangle2D.Double(x, y, width, height); // Or adjust to match visual bounds
    }

    @Override
    public String getType() {
        return "Actor";
    }
}