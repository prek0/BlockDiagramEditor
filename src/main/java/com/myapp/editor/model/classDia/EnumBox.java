package com.myapp.editor.model.classDia;

import com.myapp.editor.model.DiagramElement;
import java.awt.*;

public class EnumBox extends DiagramElement {

    public EnumBox(int x, int y, String text, int width, int height, int id) {
        super(x, y, text, width, height, id);
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(new Color(255, 248, 220));
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
        return "Enum";
    }
}
