// SelectionManager.java
package com.myapp.editor.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class SelectionManager {
    private Set<DiagramElement> selected;
    private Set<Connector> selectedConnectors;

    public SelectionManager() {
        selected = new HashSet<>();
        selectedConnectors = new HashSet<>();
    }

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

    public Set<Connector> getSelectedConnectors() {
        return new HashSet<>(selectedConnectors);
    }

    public boolean hasSelectedElements() {
        return !selected.isEmpty();
    }
}
