package com.myapp.editor.model;

import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class GroupManager {
    private final List<DiagramGroup> groups = new ArrayList<>();

    public void group(Set<DiagramElement> selection) {
        if (selection.isEmpty()) return;
        groups.add(new DiagramGroup(selection));
    }

    public void remove(DiagramGroup g) {
        groups.remove(g);
    }

    public void ungroupAt(int x, int y) {
        Iterator<DiagramGroup> it = groups.iterator();
        while (it.hasNext()) {
            DiagramGroup g = it.next();
            if (g.contains(x, y)) {
                it.remove();
                break;
            }
        }
    }

    public DiagramGroup findGroupAt(int x, int y) {
        for (DiagramGroup g : groups) {
            if (g.contains(x, y)) return g;
        }
        return null;
    }

    public DiagramGroup findGroup(Set<DiagramElement> selection) {
        for (DiagramGroup g : groups) {
            if (g.containsAll(selection)) return g;
        }
        return null;
    }

    public void drawAll(Graphics2D g2) {
        g2.setColor(Color.RED);
        var oldStroke = g2.getStroke();
        g2.setStroke(new BasicStroke(2));
        for (DiagramGroup g : groups) {
            g2.draw(g.getBounds());
        }
        g2.setStroke(oldStroke);
    }

    public void add(DiagramGroup group) {
        if (!groups.contains(group)) {
            groups.add(group);
        }
    }
    
    public List<DiagramGroup> getGroups() {
        return groups;
    }
    
}
