package root.mcts;

import root.Board;
import root.Game;

import java.util.*;

//todo do not generate repeated states (nodes)
//todo add a table for already explored states
//todo add initial move
//todo add time limitation

public class MonteCarloTreeSearch {
    private final Node initialState;

    public MonteCarloTreeSearch(Node initialState) {
        this.initialState = initialState;
    }

    public Node select(Node node) {
        List<Node> bestChildren = new ArrayList<>();
        double maxUcbValue = Double.NEGATIVE_INFINITY;

        for (Node child : node.getChildren()) {
            double ucbValue = ucb1Value(child);
            // Check if this child has a better UCB value
            if (ucbValue > maxUcbValue) {
                bestChildren.clear();
                maxUcbValue = ucbValue;
                bestChildren.add(child);
            } else if (ucbValue == maxUcbValue) {
                // If it's equal, it also could be the best child so far
                bestChildren.add(child);
            }
        }

        // Select a random child from the list of best children
        return bestChildren.isEmpty() ? null : bestChildren.get(new Random().nextInt(bestChildren.size()));
    }

    public double ucb1Value(Node node) {
        double c = Math.sqrt(2);

        if (node.getN() == 0) {
            return Double.MAX_VALUE;
        }

        double exploitation = (double) node.getV() / node.getN();
        double exploration = c * Math.sqrt(Math.log(node.parent.getN()) / node.getN());
        return exploitation + exploration;
    }

    public void expand(Node node) {
        Game game = node.getState();
        List<String> actions = Game.actions(game);
        Board.printBoard(game.getBoard());

        List<Game> resultingGamesAfterActions = actions.stream().map(a -> {
            try {
                Game gameCopy = game.copy();
                return Game.result(gameCopy, a);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).toList();

        resultingGamesAfterActions.forEach(g -> node.addChild(new Node(g)));
    }

    public int rollout(Node node) throws Exception {
        Game gameCopy = node.getState().copy();

        int iterations = 0;
        while (!Game.terminalTest(gameCopy)) {
            List<String> actions = Game.actions(gameCopy);
            Random random = new Random();
            int randomIndex = random.nextInt(actions.size());
            String randomAction = actions.get(randomIndex);

            gameCopy = Game.result(gameCopy, randomAction);

            iterations++;
        }

        System.out.println(iterations);

        return Game.utility(gameCopy, false);
    }

    public void backpropagate(Node node, int value) {
        while (node != null) {
            node.setV(node.getV() + value);
            node.setN(node.getN() + 1);
            node = node.getParent();
        }
    }

    public Node search(int iterations) throws Exception {
        for (int i = 0; i < iterations; i++) {
            Node selectedNode = initialState;

            while (!selectedNode.isLeaf()) {
                selectedNode = select(selectedNode);
            }

            if (selectedNode.n != 0) {
                expand(selectedNode);
                selectedNode = selectedNode.children.get(0);
            }

            int value = rollout(selectedNode); //we value of nodes is how many times right won (AI is always right)

            backpropagate(selectedNode, value);
        }


        return Collections.max(initialState.children, Comparator.comparingDouble(c -> {
            if (c.n == 0) {
                return 0.0;
            } else {
                return (double) c.v / c.n;
            }
        }));
    }

    public static void play(Game g) {
//        Board.printBoard(g.getBoard());
//
//        Scanner scanner = new Scanner(System.in);
//
//        while (true) {
//            if (Game.player(g)) {
//                System.out.println("LEFT PLAYER MOVE: ");
//            } else {
//                System.out.println("RIGHT PLAYER MOVE: ");
//            }
//
//            String input = scanner.nextLine();
//
//            if ("-1".equals(input)) break;
//
//            try {
//                g = Game.result(g, input);
//
//                Board.printBoard(g.getBoard());
//
//                if (Game.terminalTest(g)) {
//                    System.out.println("TERMINAL STATE");
//                    break;
//                }
//
//            } catch (Exception e) {
//                System.out.println("Error: " + e.getMessage());
//            }
//        }
//
//        scanner.close();
    }
}
