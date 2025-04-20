package com.myapp.editor.model.state;

import java.awt.*;
import java.awt.geom.Path2D;
import com.myapp.editor.model.DiagramElement;


public class DecisionStateElement extends DiagramElement {
    public DecisionStateElement(int x, int y, int size, int id) {
        super(x, y, "", size, size, id);
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        int half = width / 2;
        Path2D diamond = new Path2D.Double();
        diamond.moveTo(x + half, y);
        diamond.lineTo(x + width, y + half);
        diamond.lineTo(x + half, y + height);
        diamond.lineTo(x, y + half);
        diamond.closePath();

        g2.setColor(Color.WHITE);
        g2.fill(diamond);
        g2.setColor(Color.BLACK);
        g2.draw(diamond);

        if (selected) {
            g2.setStroke(new BasicStroke(2));
            g2.setColor(Color.BLUE);
            g2.draw(diamond.getBounds2D());
        }

        g.setColor(Color.RED);
        g.fillRect(x + width - 10, y + height - 10, 10, 10); 
        g.setColor(Color.BLACK);
    }

    @Override
    public Shape getShape() {
        int half = width / 2;
        Path2D diamond = new Path2D.Double();
        diamond.moveTo(x + half, y);
        diamond.lineTo(x + width, y + half);
        diamond.lineTo(x + half, y + height);
        diamond.lineTo(x, y + half);
        diamond.closePath();
        return diamond;
    }

    @Override
    public String getType() {
        return "Decision";
    }
}
