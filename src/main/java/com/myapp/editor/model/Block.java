package model;

public class Block {
    public int id;
    public int x, y, width, height;
    public String label;

    public Block(int id, int x, int y, int width, int height, String label) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.label = label;
    }
}
