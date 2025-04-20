package com.myapp.editor.model.state;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import com.myapp.editor.model.DiagramElement;

public class FinalStateElement extends DiagramElement {
    private static final int DIAMETER = 30;

    public FinalStateElement(int x, int y, int id) {
        super(x, y, "", DIAMETER, DIAMETER, id);
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        double outer = DIAMETER;
        double inner = DIAMETER * 0.6;
        double offset = (outer - inner) / 2;

        // outer circle
        g2.setColor(Color.BLACK);
        g2.fill(new Ellipse2D.Double(x, y, outer, outer));
        // inner circle
        g2.setColor(Color.WHITE);
        g2.fill(new Ellipse2D.Double(x + offset, y + offset, inner, inner));

        if (selected) {
            g2.setStroke(new BasicStroke(2));
            g2.setColor(Color.BLUE);
            g2.draw(new Ellipse2D.Double(x - 2, y - 2, outer + 4, outer + 4));
        }

        g.setColor(Color.RED);
        g.fillRect(x + width - 10, y + height - 10, 10, 10); 
        g.setColor(Color.BLACK);
    }

    @Override
    public Shape getShape() {
        return new Ellipse2D.Double(x, y, width, height);
    }

    @Override
    public String getType() {
        return "FinalState";
    }
}