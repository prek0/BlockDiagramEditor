package com.myapp.editor.model.connector;

import com.myapp.editor.model.Connector;
import com.myapp.editor.model.DiagramElement;

import java.awt.*;

public class GeneralisationConnector extends Connector {

    public GeneralisationConnector(DiagramElement source, DiagramElement target) {
        super(source, target);
    }

    @Override
    public void draw(Graphics g) {
        super.draw(g);  // Draw solid line
        // Draw hollow arrowhead
        int x1 = getSource().getX() + getSource().getWidth() / 2;
        int y1 = getSource().getY() + getSource().getHeight() / 2;
        int x2 = getTarget().getX() + getTarget().getWidth() / 2;
        int y2 = getTarget().getY() + getTarget().getHeight() / 2;

        // Custom drawing for the hollow arrowhead (as per your requirements)
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(Color.BLACK);
        g2d.drawLine(x1, y1, x2, y2);
        g2d.setStroke(new BasicStroke(2));
        drawArrow(g2d, x1, y1, x2, y2);
        g2d.dispose();
    }
}
