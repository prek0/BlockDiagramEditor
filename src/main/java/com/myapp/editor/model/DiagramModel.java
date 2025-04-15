package com.myapp.editor.model;

import java.util.ArrayList;
import java.util.List;

public class DiagramModel {
    private List<Block> blocks = new ArrayList<>();
    private List<Connection> connections = new ArrayList<>();

    public void addBlock(Block block) {
        blocks.add(block);
    }

    public void addConnection(Connection connection) {
        connections.add(connection);
    }

    public List<Block> getBlocks() {
        return blocks;
    }

    public List<Connection> getConnections() {
        return connections;
    }

    public void setBlocks(List<Block> blocks) {
        this.blocks = blocks;
    }

    public void setConnections(List<Connection> connections) {
        this.connections = connections;
    }
}
