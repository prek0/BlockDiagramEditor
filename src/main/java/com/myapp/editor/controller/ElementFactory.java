package com.myapp.editor.controller;

import com.myapp.editor.model.*;
import com.myapp.editor.model.general.CylinderElement;
import com.myapp.editor.model.general.EllipseElement;
import com.myapp.editor.model.general.HexagonElement;
import com.myapp.editor.model.general.ParallelogramElement;
import com.myapp.editor.model.general.RectangleElement;
import com.myapp.editor.model.general.TriangleElement;
import com.myapp.editor.model.usecase.UseCaseElement;
import com.myapp.editor.model.usecase.ActorElement;
import com.myapp.editor.model.usecase.SystemBoundary;
import com.myapp.editor.model.classDia.*;

public class ElementFactory {
    private DiagramModel model;

    public ElementFactory(DiagramModel model) {
        this.model = model;
    }

    // Method to create elements
    public DiagramElement createElement(String type, int x, int y) {
        int id = model.getNextId(); // Generate id once for consistency

        switch (type) {
            case "Use Case":
                return new UseCaseElement(x, y, "Use Case", id);
            case "Actor":
                return new ActorElement(x, y, "Actor", id);
            case "System Boundary":
                return new SystemBoundary(x, y, "Boundary", 150, 200, id);
            case "Hexagon":
                return new HexagonElement(x, y, "Hexagon", 150, 200, id);
            case "Triangle":
                return new TriangleElement(x, y, "Triangle", 150, 200, id);
            case "Parallelogram":
                return new ParallelogramElement(x, y,"Parallelogram", 150, 200, id);
            case "Ellipse":
                return new EllipseElement(x, y, "Ellipse",  150, 200, id);
            case "Cylinder":
                return new CylinderElement(x, y, "Cylinder", 150, 200, id);
            case "Rectangle":
                return new RectangleElement(x, y, "Rectangle",  150, 200, id);
            case "ClassBox":
                return new ClassBox(x, y, "Class", 150, 200, id);
            case "InterfaceBox":
                return new InterfaceBox(x, y, "Interface", 150, 200, id);
            case "AbstractBox":
                return new AbstractClassBox(x, y, "Abstract", 150, 200, id);
            case "Enum":
                return new EnumBox(x, y, "Enum", 150, 200, id);
            case "packageBox":
                return new PackageBox(x, y, "Package", 150, 200, id);
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

    // NEW: Clone element and shift position
    public DiagramElement cloneElement(DiagramElement original) {
        String type = original.getType(); // e.g., "Use Case", "Actor", etc.
        int newX = original.getX() + 20;
        int newY = original.getY() + 20;
    
        DiagramElement clone = createElement(type, newX, newY);
        clone.setText(original.getText() + " Copy");
        return clone;
    }

}
