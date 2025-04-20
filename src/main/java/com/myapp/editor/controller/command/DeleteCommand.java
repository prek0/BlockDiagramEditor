package com.myapp.editor.controller.command;

import com.myapp.editor.model.*;

import java.util.*;

public class DeleteCommand implements Command {
    private DiagramModel model;
    private Set<DiagramElement> elementsToDelete;
    private Set<Connector> connectorsToDelete;
    private GroupManager groupManager;
    private List<DiagramGroup> affectedGroups;

    public DeleteCommand(DiagramModel model,
                         Set<DiagramElement> elementsToDelete,
                         Set<Connector> connectorsToDelete,
                         GroupManager groupManager) {
        this.model = model;
        this.elementsToDelete = new HashSet<>(elementsToDelete);
        this.connectorsToDelete = new HashSet<>(connectorsToDelete);
        this.groupManager = groupManager;
        this.affectedGroups = new ArrayList<>();

        // Save affected groups
        for (DiagramElement element : elementsToDelete) {
            DiagramGroup group = groupManager.findGroup(Set.of(element));
            if (group != null && !affectedGroups.contains(group)) {
                affectedGroups.add(group);
            }
        }
    }

    @Override
    public void execute() {
        model.getConnectors().removeAll(connectorsToDelete);
        model.getElements().removeAll(elementsToDelete);
        for (DiagramGroup group : affectedGroups) {
            groupManager.remove(group);
        }
    }

    @Override
    public void undo() {
        model.getElements().addAll(elementsToDelete);
        model.getConnectors().addAll(connectorsToDelete);
        for (DiagramGroup group : affectedGroups) {
            groupManager.add(group); // Make sure this method exists in your GroupManager
        }
    }
}
