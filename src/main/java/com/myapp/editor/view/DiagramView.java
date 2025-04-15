package com.myapp.editor.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import com.myapp.editor.model.Block;
import com.myapp.editor.model.Connection;
import com.myapp.editor.model.DiagramModel;

public class DiagramView extends JPanel {
    private DiagramModel model;
    private JButton saveButton;
    private JButton loadButton;

    public DiagramView(DiagramModel model) {
        this.model = model;
        setPreferredSize(new Dimension(800, 600));
        setLayout(new BorderLayout());

        // Add toolbar with Save and Load buttons
        JPanel toolbar = new JPanel();
        saveButton = new JButton("Save");
        loadButton = new JButton("Load");
        toolbar.add(saveButton);
        toolbar.add(loadButton);
        add(toolbar, BorderLayout.NORTH);
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

    // Hook external controller actions
    public void setSaveAction(ActionListener l) {
        saveButton.addActionListener(l);
    }

    public void setLoadAction(ActionListener l) {
        loadButton.addActionListener(l);
    }
}
