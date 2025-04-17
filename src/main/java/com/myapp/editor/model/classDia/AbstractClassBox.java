package model.classDia;

import java.awt.*;
import model.DiagramElement;

public class AbstractClassBox extends DiagramElement {

    public AbstractClassBox(int x, int y, String text, int width, int height, int id) {
        super(x, y, text, width, height, id);
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(new Color(245, 245, 220));
        g.fillRect(x, y, width, height);
        g.setColor(Color.BLACK);
        g.drawRect(x, y, width, height);
        
        Font original = g.getFont();
        Font italic = original.deriveFont(Font.ITALIC);
        g.setFont(italic);
        g.drawString(text, x + 10, y + 20);
        g.setFont(original);
    }

    @Override
    public Shape getShape() {
        return new Rectangle(x, y, width, height);
    }

    @Override
    public String getType() {
        return "AbstractBox";
    }
}
