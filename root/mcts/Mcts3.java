package root.mcts;

import root.Board;
import root.Game;

import java.util.*;

public class Mcts3 extends Mcst2 {
    public static double rollout(Node node, boolean isAiLeft) throws Exception {
        Game gameCopy = node.getState().copy();
        int nodeDepth = node.getDepth();

        int iterations = 0;
//        long startTime = System.currentTimeMillis();

        while (!Game.terminalTest(gameCopy)  && iterations < 120) {
            List<String> actions = Game.actions(gameCopy);
            Collections.shuffle(actions);

            List<Game> resultingGames = new ArrayList<>();

            for (String a : actions) {
                Game gameCopyCopy = gameCopy.copy();
                gameCopyCopy = Game.result(gameCopyCopy, a);

                resultingGames.add(gameCopyCopy);
            }

            List<Pair<Game, Double>> resultingGamesWithScores = new ArrayList<>();

            for (Game g : resultingGames) {
                if (isAiLeft && (nodeDepth + iterations) % 2 == 0) {
                    resultingGamesWithScores.add(new Pair<>(g, Game.evaluate(g, true)));
                } else if (isAiLeft && (nodeDepth + iterations) % 2 == 1) {
                    resultingGamesWithScores.add(new Pair<>(g, Game.evaluate(g, false)));
                } else if (!isAiLeft && (nodeDepth + iterations) % 2 == 0) {
                    resultingGamesWithScores.add(new Pair<>(g, Game.evaluate(g, false)));
                } else if (!isAiLeft && (nodeDepth + iterations) % 2 == 1) {
                    resultingGamesWithScores.add(new Pair<>(g, Game.evaluate(g, true)));
                }
            }

            resultingGamesWithScores.sort((pair1, pair2) -> Double.compare(pair2.getR(), pair1.getR()));

            int halfSize = resultingGamesWithScores.size() / 5;
            Random rand = new Random();
            int randomIndex = rand.nextInt(halfSize);
            gameCopy = resultingGamesWithScores.get(randomIndex).getL();

//            gameCopy = resultingGamesWithScores.get(0).getL();
//            Board.printBoard(gameCopy.getBoard());

            iterations++;
        }

        if (!Game.terminalTest(gameCopy)) {
            //after an even nr of moves, it will be the move of the same player is in node for method
            double evalForLeft = Game.evaluate(gameCopy, true);
            double evalForRight = Game.evaluate(gameCopy, false);

            if (evalForLeft == evalForRight) return 0.5;

//            if (evalForLeft > evalForRight && isAiLeft) return 1;
//            if (evalForLeft > evalForRight && !isAiLeft) return 0;
//            if (evalForLeft < evalForRight && isAiLeft) return 0;
//            if (evalForLeft < evalForRight && !isAiLeft) return 1;

            if (isAiLeft) return evalForLeft;
            else return evalForRight;

//            System.out.println(gameCopy.isLeftPlayerMove());
        }

        long endTime = System.currentTimeMillis();

//        System.out.println(iterations + " " + (endTime - startTime));
//        System.out.println(iterations);

        return Game.utility(gameCopy, isAiLeft);
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

            double value = rollout(selectedNode, isAiLeft); //we value of nodes is how many times right won (AI is always right)

            backpropagate(selectedNode, value);
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
                System.out.println("AI thinking mcst3 ...");
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


    public static double rollout1(Node node, boolean isAiLeft) throws Exception {
        Game gameCopy = node.getState().copy();

        int iterations = 0;
//        long startTime = System.currentTimeMillis();

        while (!Game.terminalTest(gameCopy) && iterations < 1000) {
            List<String> actions = Game.actions(gameCopy);
            Random random = new Random();
            int randomIndex = random.nextInt(actions.size());
            String randomAction = actions.get(randomIndex);

            gameCopy = Game.result(gameCopy, randomAction);

            iterations++;
        }

        long endTime = System.currentTimeMillis();

//        System.out.println(iterations + " " + (endTime - startTime));
        System.out.println(iterations);

        if (!Game.terminalTest(gameCopy)) {
            return 0.5;
        }

        return Game.utility(gameCopy, isAiLeft);
    }

    public static double rollout2(Node node, boolean isAiLeft) throws Exception {
        Game gameCopy = node.getState().copy();

        int iterations = 0;
//        long startTime = System.currentTimeMillis();

        while (!Game.terminalTest(gameCopy) && iterations < 1000) {
            List<String> actions = Game.actions(gameCopy);
            Random random = new Random();
            int randomIndex = random.nextInt(actions.size());
            String randomAction = actions.get(randomIndex);

            gameCopy = Game.result(gameCopy, randomAction);

            iterations++;
        }

        long endTime = System.currentTimeMillis();

//        System.out.println(iterations + " " + (endTime - startTime));
        System.out.println(iterations);

        if (!Game.terminalTest(gameCopy)) {
            double evalForLeft = Game.evaluate(gameCopy, true);
            double evalForRight = Game.evaluate(gameCopy, false);

            if (evalForLeft == evalForRight) return 0.5;

            if (evalForLeft > evalForRight && isAiLeft) return 1;
            if (evalForLeft > evalForRight && !isAiLeft) return 0;
            if (evalForLeft < evalForRight && isAiLeft) return 0;
            if (evalForLeft < evalForRight && !isAiLeft) return 1;
        }

        return Game.utility(gameCopy, isAiLeft);
    }

    public static double rollout3(Node node, boolean isAiLeft) throws Exception {
        Game gameCopy = node.getState().copy();
        int nodeDepth = node.getDepth();

        int iterations = 0;
//        long startTime = System.currentTimeMillis();

        while (!Game.terminalTest(gameCopy)) {
            List<String> actions = Game.actions(gameCopy);
            Collections.shuffle(actions);

            List<Game> resultingGames = new ArrayList<>();

            for (String a : actions) {
                Game gameCopyCopy = gameCopy.copy();
                gameCopyCopy = Game.result(gameCopyCopy, a);

                resultingGames.add(gameCopyCopy);
            }

            List<Pair<Game, Double>> resultingGamesWithScores = new ArrayList<>();

            for (Game g : resultingGames) {
                if (isAiLeft && (nodeDepth + iterations) % 2 == 0) {
                    resultingGamesWithScores.add(new Pair<>(g, Game.evaluate(g, true)));
                } else if (isAiLeft && (nodeDepth + iterations) % 2 == 1) {
                    resultingGamesWithScores.add(new Pair<>(g, Game.evaluate(g, false)));
                } else if (!isAiLeft && (nodeDepth + iterations) % 2 == 0) {
                    resultingGamesWithScores.add(new Pair<>(g, Game.evaluate(g, false)));
                } else if (!isAiLeft && (nodeDepth + iterations) % 2 == 1) {
                    resultingGamesWithScores.add(new Pair<>(g, Game.evaluate(g, true)));
                }
            }

            resultingGamesWithScores.sort((pair1, pair2) -> Double.compare(pair2.getR(), pair1.getR()));


            gameCopy = resultingGamesWithScores.get(0).getL();
            Board.printBoard(resultingGamesWithScores.get(0).getL().getBoard());

            iterations++;
        }

        long endTime = System.currentTimeMillis();

//        System.out.println(iterations + " " + (endTime - startTime));
        System.out.println(iterations);

        return Game.utility(gameCopy, isAiLeft);
    }
}