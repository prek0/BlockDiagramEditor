package com.myapp.editor.model.connector;

import com.myapp.editor.model.Connector;
import com.myapp.editor.model.DiagramElement;
import java.awt.*;

public class RealisationConnector extends Connector {

    public RealisationConnector(DiagramElement source, DiagramElement target) {
        super(source, target);
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10, new float[]{9}, 0));
        
        // Draw the line from center of source to target (instead of using super.draw(g2d))
        int x1 = getSource().getX() + getSource().getWidth() / 2;
        int y1 = getSource().getY() + getSource().getHeight() / 2;
        int x2 = getTarget().getX() + getTarget().getWidth() / 2;
        int y2 = getTarget().getY() + getTarget().getHeight() / 2;

        g2d.drawLine(x1, y1, x2, y2);

        // Draw the arrowhead
        drawArrow(g2d, x1, y1, x2, y2);

        g2d.dispose();
    }
}
