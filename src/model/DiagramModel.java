package model;

import java.util.*;

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
}
