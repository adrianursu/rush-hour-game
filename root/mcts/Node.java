package root.mcts;

import root.Game;

import java.util.ArrayList;
import java.util.List;

public class Node {
    Game state;
    int v; //value
    int n; //times sampled  (n>=v)
    List<Node> children;
    Node parent;
    String fromAction; //the action that led to this node generation

    public Node(Game state) {
        this.state = state;
        this.n = 0;
        this.v = 0;
        this.children = new ArrayList<>();
        this.parent = null;
    }

    public Game getState() {
        return state;
    }

    public Node getParent() {
        return parent;
    }

    public int getN() {
        return n;
    }

    public int getV() {
        return v;
    }

    public void setV(int v) {
        this.v = v;
    }

    public void setN(int n) {
        this.n = n;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public List<Node> getChildren() {
        return children;
    }

    public boolean isLeaf() {
        return children.isEmpty();
    }

    public void addChild(Node child) {
        child.setParent(this);
        children.add(child);
    }

    public void printTree() {
        printSubtree(this, "", true);
    }

    // Helper method for printing the tree structure
    private void printSubtree(Node node, String prefix, boolean isTail) {
        System.out.println(prefix + (isTail ? "└── " : "├── ") + node.v + " " + node.n + " " + node.fromAction);
        for (int i = 0; i < node.children.size() - 1; i++) {
            printSubtree(node.children.get(i), prefix + (isTail ? "    " : "│   "), false);
        }
        if (node.children.size() > 0) {
            printSubtree(node.children.get(node.children.size() - 1),
                    prefix + (isTail ?"    " : "│   "), true);
        }
    }
}