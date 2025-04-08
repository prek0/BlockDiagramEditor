package view;

import model.Block;
import model.Connection;
import model.DiagramModel;

import javax.swing.*;
import java.awt.*;

public class DiagramView extends JPanel {
    private DiagramModel model;

    public DiagramView(DiagramModel model) {
        this.model = model;
        setPreferredSize(new Dimension(800, 600));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (Connection conn : model.getConnections()) {
            Block from = getBlockById(conn.fromId);
            Block to = getBlockById(conn.toId);
            if (from != null && to != null) {
                g.drawLine(from.x + from.width / 2, from.y + from.height / 2,
                           to.x + to.width / 2, to.y + to.height / 2);
            }
        }

        for (Block block : model.getBlocks()) {
            g.drawRect(block.x, block.y, block.width, block.height);
            g.drawString(block.label, block.x + 5, block.y + 15);
        }
    }

    private Block getBlockById(int id) {
        return model.getBlocks().stream().filter(b -> b.id == id).findFirst().orElse(null);
    }
}
