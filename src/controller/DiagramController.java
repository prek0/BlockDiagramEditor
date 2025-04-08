package controller;

import model.Block;
import model.DiagramModel;
import view.DiagramView;

import java.awt.event.*;
import java.util.concurrent.atomic.AtomicInteger;

public class DiagramController extends MouseAdapter {
    private DiagramModel model;
    private DiagramView view;
    private AtomicInteger idGenerator = new AtomicInteger(1);

    public DiagramController(DiagramModel model, DiagramView view) {
        this.model = model;
        this.view = view;

        view.addMouseListener(this);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // On click, add a new block
        Block newBlock = new Block(idGenerator.getAndIncrement(), e.getX(), e.getY(), 80, 40, "Block");
        model.addBlock(newBlock);
        view.repaint();
    }
}
