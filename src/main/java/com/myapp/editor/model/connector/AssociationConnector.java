package com.myapp.editor.model.connector;

import com.myapp.editor.model.Connector;
import com.myapp.editor.model.DiagramElement;
import java.awt.*;

public class AssociationConnector extends Connector {

    public AssociationConnector(DiagramElement source, DiagramElement target) {
        super(source, target);
    }

    @Override
    public void draw(Graphics g) {
        super.draw(g);  // Just draw the regular line (no arrowhead)
    }
}
