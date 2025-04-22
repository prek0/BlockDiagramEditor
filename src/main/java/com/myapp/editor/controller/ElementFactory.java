package com.myapp.editor.controller;

import com.myapp.editor.model.Connector;
import com.myapp.editor.model.DiagramElement;
import com.myapp.editor.model.DiagramModel;
import com.myapp.editor.model.TextElement;
import com.myapp.editor.model.classDia.AbstractClassBox;
import com.myapp.editor.model.classDia.ClassBox;
import com.myapp.editor.model.classDia.EnumBox;
import com.myapp.editor.model.classDia.InterfaceBox;
import com.myapp.editor.model.connector.AggregationConnector;
import com.myapp.editor.model.connector.AssociationConnector;
import com.myapp.editor.model.connector.CompositionConnector;
import com.myapp.editor.model.connector.DashedLineConnector;
import com.myapp.editor.model.connector.GeneralisationConnector;
import com.myapp.editor.model.connector.RealisationConnector;
import com.myapp.editor.model.general.CylinderElement;
import com.myapp.editor.model.general.EllipseElement;
import com.myapp.editor.model.general.HexagonElement;
import com.myapp.editor.model.general.ParallelogramElement;
import com.myapp.editor.model.general.RectangleElement;
import com.myapp.editor.model.general.TriangleElement;
import com.myapp.editor.model.state.DecisionStateElement;
import com.myapp.editor.model.state.FinalStateElement;
import com.myapp.editor.model.state.InitialStateElement;
import com.myapp.editor.model.state.StateElement;
import com.myapp.editor.model.usecase.ActorElement;
import com.myapp.editor.model.usecase.SystemBoundary;
import com.myapp.editor.model.usecase.UseCaseElement;

public class ElementFactory {
    private DiagramModel model;

    public ElementFactory(DiagramModel model) {
        this.model = model;
    }

    // Method to create elements
    public DiagramElement createElement(String type, int x, int y, String text) {
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
            case "InitialState":  
                return new InitialStateElement(x, y, id);
            case "FinalState":    
                return new FinalStateElement(x, y, id);
            case "State":         
                return new StateElement(x, y, "State", 150, 80, id);
            case "Decision":      
                return new DecisionStateElement(x, y, 80, id);
            
            // Add case for Text element
            case "Text":
                return new TextElement(x, y, text, id);
            default:
                throw new IllegalArgumentException("Unknown element type: " + type);
        }
    }

     // Create connectors (wires, arrows, etc.)
    public Connector createConnector(String type, DiagramElement source, DiagramElement target) {
        switch (type) {
            case "Association":
                return new AssociationConnector(source, target);
            case "Generalization":
                return new GeneralisationConnector(source, target);
            case "Realization":
                return new RealisationConnector(source, target);
            case "Aggregation":
                return new AggregationConnector(source, target);
            case "Composition":
                return new CompositionConnector(source, target);
            case "DashedLine":
                return new DashedLineConnector(source, target);
            default:
                throw new IllegalArgumentException("Unknown connector type: " + type);
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
    
        DiagramElement clone = createElement(type, newX, newY, original.getText());
        clone.setText(original.getText());
        return clone;
    }

}
