package view;

import model.DiagramElement;
import model.Connector;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class DiagramPanel extends JPanel {
    private List<DiagramElement> elements;
    private List<Connector> connectors;

    public DiagramPanel(List<DiagramElement> elements, List<Connector> connectors) {
        this.elements = elements;
        this.connectors = connectors;
        setPreferredSize(new Dimension(800, 600));  // Set panel size
        setBackground(Color.WHITE);  // Set background color
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (Connector connector : connectors) {
            connector.draw(g);  // Drawing the connectors
        }

        for (DiagramElement element : elements) {
            element.draw(g);  // Drawing the elements
        }
    }
}
