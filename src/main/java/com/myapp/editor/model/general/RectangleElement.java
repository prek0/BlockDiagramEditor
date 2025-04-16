package model.general;

import model.DiagramElement;
import java.awt.*;
import java.awt.geom.Rectangle2D;

public class RectangleElement extends DiagramElement {

    public RectangleElement(int x, int y, String text, int width, int height, int id) {
        super(x, y, text, width, height, id);
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(selected ? Color.BLUE : Color.BLACK);
        g.drawRect(x, y, width, height);
        g.drawString(text, x + 5, y + 15);

        if (resizing) {
            g.setColor(Color.RED);
            g.fillRect(x + width - 10, y + height - 10, 10, 10); // Resize handle
        }
    }

    @Override
    public Shape getShape() {
        return new Rectangle2D.Double(x, y, width, height); 
    }
}