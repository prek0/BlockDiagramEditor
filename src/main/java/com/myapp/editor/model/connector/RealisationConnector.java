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
        super.draw(g2d);  // Draw dotted line
        drawArrow(g2d, getSource().getX(), getSource().getY(), getTarget().getX(), getTarget().getY());
        g2d.dispose();
    }
}
