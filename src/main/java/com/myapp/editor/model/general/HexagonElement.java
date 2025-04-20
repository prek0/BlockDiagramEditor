package com.myapp.editor.model.general;

import com.myapp.editor.model.DiagramElement;
import java.awt.*;
import java.awt.geom.Rectangle2D;

public class HexagonElement extends DiagramElement {
    public HexagonElement (int x, int y, String text, int width, int height, int id) {
        super(x, y, text, width, height, id);
    }

    @Override
    public void draw(Graphics g) {
        int w6 = width / 6;
        int[] xPoints = { x + w6, x + 5 * w6, x + width, x + 5 * w6, x + w6, x };
        int[] yPoints = { y, y, y + height / 2, y + height, y + height, y + height / 2 };

        g.setColor(selected ? Color.BLUE : Color.BLACK);
        g.drawPolygon(xPoints, yPoints, 6);
        g.drawString(text, x + width / 4, y + height / 2);

        if (resizing) {
            g.setColor(Color.RED);
            g.fillRect(x + width - 10, y + height - 10, 10, 10);
        }
    }

    @Override
    public Shape getShape() {
        return new Rectangle2D.Double(x, y, width, height); 
    }

    @Override
    public String getType() {
        return "Hexagon";
    }
}