package root.mcts;

import java.util.ArrayList;
import java.util.List;

class Node {
    int value;
    int visits;
    List<Node> children;

    public Node(int value) {
        this.value = value;
        this.visits = 0;
        this.children = new ArrayList<>();
    }
}