package com.myapp.editor.model.connector;

import com.myapp.editor.model.Connector;
import com.myapp.editor.model.DiagramElement;

import java.awt.*;

public class DashedLineConnector extends Connector {

    public DashedLineConnector(DiagramElement source, DiagramElement target) {
        super(source, target);
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10, new float[]{9}, 0)); // Dotted line
        super.draw(g2d);  // Draw dashed line
        g2d.dispose();
    }
}
