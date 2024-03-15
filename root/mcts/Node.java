package root.mcts;

import root.Game;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class Node {
    Game state;
    double v; //value
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

    public double getV() {
        return v;
    }

    public void setV(double v) {
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
                    prefix + (isTail ? "    " : "│   "), true);
        }
    }

    public double calculateAvgBranchingFactorWithoutLeafs() {
        Queue<Node> queue = new ArrayDeque<>();
        queue.add(this);

        int totalBranchingFactor = 0;
        int totalNodes = 0;

        while (!queue.isEmpty()) {
            Node currentNode = queue.poll();

            // Skip leaf nodes
            if (!currentNode.isLeaf()) {
                totalBranchingFactor += currentNode.children.size();
                totalNodes++;
                for (Node child : currentNode.children) {
                    queue.add(child);
                }
            }
        }

        // Avoid division by zero
        if (totalNodes == 0) {
            return 0;
        }

        return (double) totalBranchingFactor / totalNodes;
    }

    public int getTotalNumberOfNodes() {
        int total = 1; // Start by counting the current node
        for (Node child : children) {
            total += child.getTotalNumberOfNodes(); // Add the count of nodes in the subtree
        }
        return total;
    }

    public int getNumberOfNonLeafNodes() {
        int count = isLeaf() ? 0 : 1;
        for (Node child : children) {
            count += child.getNumberOfNonLeafNodes();
        }
        return count;
    }

    public int getDepth() {
        int depth = 0;
        Node current = this;
        while (current.parent != null) {
            depth++;
            current = current.parent;
        }
        return depth;
    }

    public double getVDividedByN() {
        if (n == 0) return 0;
        return v / n;
    }

    public void removeChild(Node child) {
        children.remove(child);
    }
}