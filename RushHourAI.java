public class RushHourAI {
    
    public int evaluate(Board board, boolean isMaximisingPlayer){
        int score = 0;
        int distanceToGoal = distanceToGoal(board, isMaximisingPlayer);
        int numOfBlockingVehicles = numOfBlockingVehicles(board, isMaximisingPlayer);
        int distanceToGoalOpponent = distanceToGoal(board, !isMaximisingPlayer);
        int numOfBlockingVehiclesOpponent = numOfBlockingVehicles(board, !isMaximisingPlayer);
        // current player's distance to the goal and number of blocking vehicles is more important than the opponent's - that's why they are multiplied by 2
        score = 2 * (Board.TRUE_WIDTH - distanceToGoal) - (Board.TRUE_WIDTH - distanceToGoalOpponent) + 
                2 * (board.getVehicles().size() - 1 - numOfBlockingVehiclesOpponent) - (board.getVehicles().size() - 1 - numOfBlockingVehicles);
        return score;
    }

    private int distanceToGoal(Board board, boolean isMaximisingPlayer) {
        // maximising player is the left hero vehicle
        int distance = 0;
        if (isMaximisingPlayer) {
            // goal is made when a vehicle is completely out of the board
            distance = Board.TRUE_WIDTH - board.getVehicleById(">").getColStart();
        } else {
            distance = board.getVehicleById("<").getColEnd() + 1;
        }
        return distance;
    }

    private int numOfBlockingVehicles(Board board, boolean isMaximisingPlayer) {
        // maximising player is the left hero vehicle
        if(isMaximisingPlayer) {
            return board.getNumOfBlockingVehicles(">");
        }
        else {
            return board.getNumOfBlockingVehicles("<");
        }
    }
}