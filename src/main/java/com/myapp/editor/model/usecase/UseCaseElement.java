package model.usecase;

import model.*;

import java.awt.Color;
import java.awt.Graphics;

public class UseCaseElement extends DiagramElement {
    public UseCaseElement(int x, int y, String text, int id) {
        super(x, y, text, 120, 80, id); // Use the constructor with width and height (120, 80)
    }

    @Override
    public void draw(Graphics g) {
        g.drawOval(x, y, width, height);
        g.drawString(text, x + width / 4, y + height / 2);

        if (selected) {
            g.setColor(Color.RED);
            g.drawRect(x - 2, y - 2, width + 4, height + 4);
            g.setColor(Color.BLACK);
        }

        g.setColor(Color.RED);
        g.fillRect(x + width - 10, y + height - 10, 10, 10); // Resize handle
        g.setColor(Color.BLACK);
    }
}
