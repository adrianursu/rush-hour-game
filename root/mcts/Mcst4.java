package root.mcts;

import root.Board;
import root.Game;

import java.util.*;

public class Mcst4 extends Mcts3{
    public static Node search(Node initialState, int iterations, boolean isAiLeft) throws Exception {
        List<Integer> prunedAtThisDepths = new ArrayList<>();

        for (int i = 0; i < iterations; i++) {
            Node selectedNode = initialState;

            while (!selectedNode.isLeaf()) {
                selectedNode = select(selectedNode);
            }

            if (selectedNode.n != 0) {
                expand(selectedNode, isAiLeft);
                selectedNode = selectedNode.children.get(0);
            }

            double value = rollout(selectedNode, isAiLeft); //we value of nodes is how many times right won (AI is always right)

            backpropagate(selectedNode, value);

            prunedAtThisDepths = pruneTreeIfNecessary(initialState, selectedNode.getDepth(), prunedAtThisDepths);
        }

//        initialState.printTree();


        return Collections.max(initialState.children, Comparator.comparingDouble(c -> {
            if (c.n == 0) {
                return 0.0;
            } else {
                return (double) c.v / c.n;
            }
        }));
    }

    public static void play(Game g) {
        Board.printBoard(g.getBoard());

        Scanner scanner = new Scanner(System.in);

        while (true) {
            if (Game.player(g)) {
                System.out.println("LEFT PLAYER MOVE: ");

                String input = scanner.nextLine();
                if ("-1".equals(input)) break;

                try {
                    g = Game.result(g, input);

                    Board.printBoard(g.getBoard());

                    if (Game.terminalTest(g)) {
                        System.out.println("TERMINAL STATE");
                        break;
                    }
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }

            } else {
                System.out.println("RIGHT (AI) PLAYER MOVE: ");
                System.out.println("AI thinking mcst4 ...");
                try {
                    Node bestNode = search(new Node(g), 1000, false);
                    System.out.println("Best node: " + bestNode.v + "/" + bestNode.n + " " + bestNode.fromAction);

                    g = Game.result(g, bestNode.fromAction);

                    Board.printBoard(g.getBoard());

                    if (Game.terminalTest(g)) {
                        System.out.println("TERMINAL STATE");
                        break;
                    }
                } catch (Exception e) {
                    throw new RuntimeException("AI from ohio", e);
                }
            }
        }

        scanner.close();
    }

    private static List<Integer> pruneTreeIfNecessary(Node root, int depthOfLastEncounteredNode, List<Integer> prunedAtDepthsSoFar) {
        if (depthOfLastEncounteredNode >= 3) {
            if (!prunedAtDepthsSoFar.contains(depthOfLastEncounteredNode - 3)) {
//                System.out.println("prun");
                pruneTreeAtDepth(root, depthOfLastEncounteredNode - 3);
                prunedAtDepthsSoFar.add(depthOfLastEncounteredNode - 3);
            }
        }

        return prunedAtDepthsSoFar;
    }


    private static void pruneTreeAtDepth(Node root, int depth) {
        if (depth == 0) {
            // Assuming the node has children and a method to get them.
            List<Node> children = root.getChildren();

            if (!children.isEmpty()) {
                // Find the child with the lowest value. Assuming Node has a method to get its value.

                Node lowestValueChild = Collections.min(children, Comparator.comparing(Node::getVDividedByN));

                // Assuming Node has a method to remove a child.
                root.removeChild(lowestValueChild);
            }
        } else {
            // Recursively call this method for each child, decreasing depth by 1.
            for (Node child : root.getChildren()) {
                pruneTreeAtDepth(child, depth - 1);
            }
        }
    }
}
