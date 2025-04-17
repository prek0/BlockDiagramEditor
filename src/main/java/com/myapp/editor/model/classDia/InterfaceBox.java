package model.classDia;

import java.awt.*;
import model.DiagramElement;

public class InterfaceBox extends DiagramElement {

    public InterfaceBox(int x, int y, String text, int width, int height, int id) {
        super(x, y, "<<interface>> " + text, width, height, id);
    }

    public void draw(Graphics g) {
        g.setColor(new Color(224, 255, 255));
        g.fillRect(x, y, width, height);
        g.setColor(Color.BLACK);
        g.drawRect(x, y, width, height);
        g.drawString(text, x + 10, y + 20);
    }

    @Override
    public Shape getShape() {
        return new Rectangle(x, y, width, height);
    }

    @Override
    public String getType() {
        return "InterfaceBox";
    }
}
