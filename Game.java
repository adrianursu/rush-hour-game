import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class Game {
    private final Board board;
    private boolean isLeftPlayerMove;

    public Game(Board board) {
        this.board = board;
        this.isLeftPlayerMove = true;
    }

    public boolean isLeftPlayerMove() {
        return isLeftPlayerMove;
    }

    public void setLeftPlayerMove(boolean leftPlayerMove) {
        isLeftPlayerMove = leftPlayerMove;
    }

    public Board getBoard() {
        return board;
    }

    public static boolean player(Game game) {
        return game.isLeftPlayerMove();
    }

    public static List<String> actions(Game game) {
        return new ArrayList<>(); //todo impl
    }

    public static Game result(Game game, String action) throws Exception {
        String str = action.substring(0, 1);
        int offset = Integer.parseInt(action.substring(1));

        if (str.equals("L")) {
            game.checkIfMoveIsNotZero(offset);
            game.getBoard().moveBoardPart(true, offset);
        } else if (str.equals("R")) {
            game.checkIfMoveIsNotZero(offset);
            game.getBoard().moveBoardPart(false, offset);
        } else {
            Vehicle vehicle = game.getBoard().getVehicles().stream().filter(veh -> veh.getId().equals(str)).findFirst().orElseThrow(() -> new NoSuchElementException("Vehicle with Id " + str + " not found"));
            game.checkIfPlayerIsAllowedToMoveVehicle(vehicle);
            game.checkIfMoveIsNotZero(offset);
            game.getBoard().moveVehicle(vehicle, offset);
        }

        game.setLeftPlayerMove(!game.isLeftPlayerMove());

        return game;
    }

    public static boolean terminalTest(Game game) {
        Vehicle leftHero = game.getBoard().getVehicles().stream().filter(veh -> veh.isHero() && veh.isLeft()).findFirst().orElseThrow(() -> new NoSuchElementException("Left hero not found"));
        Vehicle rightHero = game.getBoard().getVehicles().stream().filter(veh -> veh.isHero() && !veh.isLeft()).findFirst().orElseThrow(() -> new NoSuchElementException("Right hero not found"));

        return (leftHero.getColEnd() == Board.TRUE_WIDTH - 1) || (rightHero.getColStart() == 0);
    }

    public static int utility(Game game, boolean isLeftPlayer) {
        return -1; //todo impl
    }

    private void checkIfPlayerIsAllowedToMoveVehicle(Vehicle vehicle) throws Exception {
        if (isLeftPlayerMove() && !vehicle.isLeft() && vehicle.isHero())
            throw new IllegalArgumentException("Player is not allowed to move this vehicle");
        if (!isLeftPlayerMove() && vehicle.isLeft() && vehicle.isHero())
            throw new IllegalArgumentException("Player is not allowed to move this vehicle");
    }

    private void checkIfMoveIsNotZero(int offset) throws Exception {
        if (offset == 0) throw new Exception("0 move does not make sense");
    }
}
