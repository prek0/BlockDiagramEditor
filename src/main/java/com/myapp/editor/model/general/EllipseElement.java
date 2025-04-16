package model.general;

import model.DiagramElement;
import java.awt.*;
import java.awt.geom.Ellipse2D;

public class EllipseElement extends DiagramElement {

    public EllipseElement(int x, int y, String text, int width, int height, int id) {
        super(x, y, text, width, height, id);
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(selected ? Color.BLUE : Color.BLACK);
        g.drawOval(x, y, width, height);
        g.drawString(text, x + 5, y + height / 2);

        if (resizing) {
            g.setColor(Color.RED);
            g.fillRect(x + width - 10, y + height - 10, 10, 10);
        }
    }

    @Override
    public Shape getShape() {
        return new Ellipse2D.Double(x, y, width, height);
    }
}