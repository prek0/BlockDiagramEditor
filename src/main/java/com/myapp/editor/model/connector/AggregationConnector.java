package com.myapp.editor.model.connector;

import com.myapp.editor.model.Connector;
import com.myapp.editor.model.DiagramElement;

import java.awt.*;

public class AggregationConnector extends Connector {

    public AggregationConnector(DiagramElement source, DiagramElement target) {
        super(source, target);
    }

    @Override
    public void draw(Graphics g) {
        super.draw(g);  // Draw solid line
        // Custom drawing for the hollow diamond head
        int x1 = getSource().getX() + getSource().getWidth() / 2;
        int y1 = getSource().getY() + getSource().getHeight() / 2;
        int x2 = getTarget().getX() + getTarget().getWidth() / 2;
        int y2 = getTarget().getY() + getTarget().getHeight() / 2;

        Graphics2D g2d = (Graphics2D) g.create();
        drawDiamondHead(g2d, x2, y2);  // Drawing hollow diamond at the target end
        g2d.dispose();
    }

    private void drawDiamondHead(Graphics2D g2d, int x, int y) {
        int size = 10;
        int[] xPoints = {x, x + size, x, x - size};
        int[] yPoints = {y - size, y, y + size, y};
        g2d.setColor(Color.BLACK);
        g2d.drawPolygon(xPoints, yPoints, 4);
    }
}
