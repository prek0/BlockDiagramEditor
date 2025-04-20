package com.myapp.editor.model;

import java.util.ArrayList;
import java.util.List;

public class DiagramModel {
    private List<DiagramElement> elements = new ArrayList<>();
    private List<Connector> connectors = new ArrayList<>();
    private int nextId = 1;  

    public DiagramModel() {
        elements = new ArrayList<>();
        connectors = new ArrayList<>();
    }

    public void addElement(DiagramElement element) {
        elements.add(element);
    }

    public void addConnector(Connector connector) {
        connectors.add(connector);
    }

    public List<DiagramElement> getElements() {
        return elements;
    }

    public List<Connector> getConnectors() {
        return connectors;
    }

    public void setElements(List<DiagramElement> elements) {
        this.elements = elements;
    }

    public void setConnectors(List<Connector> connectors) {
        this.connectors = connectors;
    }

    public int getNextId() {
        return nextId++;
    }

     public void removeElement(DiagramElement element) {
        elements.remove(element);
    }

    public void removeConnector(Connector connector) {
        connectors.remove(connector);
    }
}
