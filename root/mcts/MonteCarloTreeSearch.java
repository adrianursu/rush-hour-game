//package root.mcts;
//
//import java.util.Collections;
//import java.util.Comparator;
//
//public class MonteCarloTreeSearch {
//    Node root;
//
//    public MonteCarloTreeSearch(Node root) {
//        this.root = root;
//    }
//
//    public Node select(Node node) {
//        Node selectedChild = null;
//        int maxValue = Integer.MIN_VALUE;
//        for (Node child : node.children) {
//            double ucb1 = (double) child.value / child.visits + Math.sqrt(2 * Math.log(node.visits) / child.visits);
//            if (ucb1 > maxValue) {
//                maxValue = (int) ucb1;
//                selectedChild = child;
//            }
//        }
//        return selectedChild;
//    }
//
//    public void expand(Node node) {
//        // For this example, let's expand all children of the selected node
//        for (int i = 0; i < 3; i++) {
//            Node newNode = new Node((int) (Math.random() * 10)); // Generate random value for new node
//            node.children.add(newNode);
//        }
//    }
//
//    public int simulate(Node node) {
//        // Simulate the game from the selected node to a terminal state
//        // For simplicity, let's just return a random value as the result
//        return (int) (Math.random() * 100);
//    }
//
//    public void backpropagate(Node node, int value) {
//        // Update the statistics of nodes in the path from the selected node to the root
//        while (node != null) {
//            node.visits++;
//            node.value += value;
//            node = node.parent;
//        }
//    }
//
//    public Node search(int iterations) {
//        for (int i = 0; i < iterations; i++) {
//            Node selectedNode = select(root);
//            expand(selectedNode);
//            int value = simulate(selectedNode);
//            backpropagate(selectedNode, value);
//        }
//        // Select the best child of the root node based on statistics
//        Node bestChild = Collections.max(root.children, Comparator.comparingDouble(c -> (double) c.value / c.visits));
//        return bestChild;
//    }
//}
