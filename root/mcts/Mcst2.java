package root.mcts;

import root.Board;
import root.Game;

import java.util.*;

public class Mcst2 extends Mcst1 {
    public static void expand(Node node, boolean isAiLeft) {
        Game game = node.getState();
        List<String> actions = Game.actions(game);
        Collections.shuffle(actions);

        int nodeDepth = node.getDepth();

        List<Pair<Node, Double>> childNodesWithScores = new ArrayList<>();

        actions.forEach(a -> {
            try {
                Game gameCopy = game.copy();
                gameCopy = Game.result(gameCopy, a);

                double score = 0;

                if (isAiLeft && nodeDepth % 2 == 0) {
                    score = Game.evaluate(gameCopy, true);
                } else if (isAiLeft && nodeDepth % 2 == 1) {
                    score = Game.evaluate(gameCopy, false);
                } else if (!isAiLeft && nodeDepth % 2 == 0) {
                    score = Game.evaluate(gameCopy, false);
                } else if (!isAiLeft && nodeDepth % 2 == 1) {
                    score = Game.evaluate(gameCopy, true);
                }

                Node n = new Node(gameCopy);
                n.fromAction = a;
                childNodesWithScores.add(new Pair<>(n, score));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        childNodesWithScores.sort((pair1, pair2) -> Double.compare(pair2.getR(), pair1.getR()));

        Optional<Pair<Node, Double>> moveLeadingToWin = childNodesWithScores.stream().findFirst()
                .filter(pair -> pair.getR() == 1.0);

        if (moveLeadingToWin.isPresent()) {
            node.addChild(moveLeadingToWin.get().getL());
            node.addChild(moveLeadingToWin.get().getL());
            node.addChild(moveLeadingToWin.get().getL());
            node.addChild(moveLeadingToWin.get().getL());
        } else {
            childNodesWithScores.stream()
                    .limit(4)
                    .forEach(pair -> node.addChild(pair.getL()));
        }
    }

    public static Node search(Node initialState, int iterations, boolean isAiLeft) throws Exception {
        for (int i = 0; i < iterations; i++) {
            Node selectedNode = initialState;

            while (!selectedNode.isLeaf()) {
                selectedNode = select(selectedNode);
            }

            if (selectedNode.n != 0) {
                expand(selectedNode, isAiLeft);
                selectedNode = selectedNode.children.get(0);
            }

            double value = rollout(selectedNode, isAiLeft); // we value of nodes is how many times right won (AI is
                                                            // always right)

            backpropagate(selectedNode, value);
        }

        // initialState.printTree();

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
                if ("-1".equals(input))
                    break;

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
                System.out.println("AI thinking mcst2 ...");
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
}