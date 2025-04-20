// SelectionManager.java
package com.myapp.editor.view;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import com.myapp.editor.model.DiagramElement;

public class SelectionManager {
    private final Set<DiagramElement> selected = new HashSet<>();

    public void clear() {
        for (DiagramElement e : selected) e.setSelected(false);
        selected.clear();
    }

    public void select(DiagramElement e) {
        selected.add(e);
        e.setSelected(true);
    }

    public void toggle(DiagramElement e) {
        if (selected.contains(e)) deselect(e);
        else select(e);
    }

    public void deselect(DiagramElement e) {
        selected.remove(e);
        e.setSelected(false);
    }

    public Set<DiagramElement> getSelected() {
        return Collections.unmodifiableSet(selected);
    }

    public boolean hasSelection() {
        return !selected.isEmpty();
    }
}
