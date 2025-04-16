package controller;

import model.*;
import model.usecase.UseCaseElement;
import model.usecase.ActorElement;
import model.usecase.SystemBoundary;

public class ElementFactory {
    private DiagramModel model;

    public ElementFactory(DiagramModel model) {
        this.model = model;
    }

    // Method to create elements
    public DiagramElement createElement(String type, int x, int y) {
        switch (type) {
            case "Use Case":
                return new UseCaseElement(x, y, "Use Case", model.getNextId());
            case "Actor":
                return new ActorElement(x, y, "Actor", model.getNextId());
            case "System Boundary":
                return new SystemBoundary(x, y, "Boundary",150,200, model.getNextId());
            default:
                throw new IllegalArgumentException("Unknown element type: " + type);
        }
    }

    // Random placement methods
    public int randomX() {
        return 100 + (int)(Math.random() * 300);
    }

    public int randomY() {
        return 100 + (int)(Math.random() * 300);
    }
}
