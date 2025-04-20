
// DiagramGroup.java
package com.myapp.editor.model;

import java.awt.Rectangle;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class DiagramGroup {
    private final Set<DiagramElement> members = new HashSet<>();

    public DiagramGroup(Set<DiagramElement> els) {
        members.addAll(els);
    }

    public Rectangle getBounds() {
        int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE;
        for (DiagramElement el : members) {
            minX = Math.min(minX, el.getX());
            minY = Math.min(minY, el.getY());
            maxX = Math.max(maxX, el.getX() + el.width);
            maxY = Math.max(maxY, el.getY() + el.height);
        }
        return new Rectangle(minX - 10, minY - 10, (maxX - minX) + 20, (maxY - minY) + 20);
    }

    public void move(int dx, int dy) {
        for (DiagramElement el : members) {
            el.setX(el.getX() + dx);
            el.setY(el.getY() + dy);
        }
    }

    public boolean containsAll(Set<DiagramElement> sels) {
        return members.containsAll(sels);
    }

    public boolean contains(int x, int y) {
        return getBounds().contains(x, y);
    }

    public Set<DiagramElement> getMembers() {
        return Collections.unmodifiableSet(members);
    }
}
