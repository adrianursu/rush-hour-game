package root;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    @Test
    void getNumberOfObstacleVehiclesFromLeftHeroToGoalTest1() {
        Board board = new Board();
        board.addVehicle(new Vehicle(true, true, 2, 1 + Board.PART_MAX_OFFSET_ABS, 0, false, '>'));
        board.addVehicle(new Vehicle(false, false, 3, 1 + Board.PART_MAX_OFFSET_ABS, 3, true, 'a'));
        board.addVehicle(new Vehicle(false, false, 3, 1 + Board.PART_MAX_OFFSET_ABS, 5, true, 'b'));
        board.addVehicle(new Vehicle(false, false, 2, 1 + Board.PART_MAX_OFFSET_ABS, 11, false, 'c'));

        Board.printBoard(board);

        assertEquals(3, board.getNumberOfObstacleVehiclesFromHeroToGoal(true));
    }

    @Test
    void getNumberOfObstacleVehiclesFromLeftHeroToGoalTest2() throws Exception {
        Board board = new Board();
        board.addVehicle(new Vehicle(true, true, 2, 1 + Board.PART_MAX_OFFSET_ABS, 0, false, '>'));
        board.addVehicle(new Vehicle(false, false, 3, 1 + Board.PART_MAX_OFFSET_ABS, 3, true, 'a'));
        board.addVehicle(new Vehicle(false, false, 3, 1 + Board.PART_MAX_OFFSET_ABS, 5, true, 'b'));
        board.addVehicle(new Vehicle(false, false, 2, 1 + Board.PART_MAX_OFFSET_ABS, 11, false, 'c'));
        board.moveLeftPart(-2);
        board.moveRightPart(-2);
        Board.printBoard(board);

        assertEquals(2, board.getNumberOfObstacleVehiclesFromHeroToGoal(true));
    }

    @Test
    void getNumberOfObstacleVehiclesFromLeftHeroToGoalTest3() {
        Board board = new Board();
        board.addVehicle(new Vehicle(true, true, 2, 1 + Board.PART_MAX_OFFSET_ABS, 6, false, '>'));
        board.addVehicle(new Vehicle(false, false, 3, 1 + Board.PART_MAX_OFFSET_ABS, 3, true, 'a'));
        board.addVehicle(new Vehicle(false, false, 3, 1 + Board.PART_MAX_OFFSET_ABS, 5, true, 'b'));
        board.addVehicle(new Vehicle(false, false, 2, 1 + Board.PART_MAX_OFFSET_ABS, 11, false, 'c'));

        Board.printBoard(board);

        assertEquals(1, board.getNumberOfObstacleVehiclesFromHeroToGoal(true));
    }

    @Test
    void getNumberOfObstacleVehiclesFromLeftHeroToGoalTest4() {
        Board board = new Board();
        board.addVehicle(new Vehicle(true, false, 2, 1 + Board.PART_MAX_OFFSET_ABS, 6, false, '<'));
        board.addVehicle(new Vehicle(false, false, 3, 1 + Board.PART_MAX_OFFSET_ABS, 3, true, 'a'));
        board.addVehicle(new Vehicle(false, false, 3, 1 + Board.PART_MAX_OFFSET_ABS, 5, true, 'b'));
        board.addVehicle(new Vehicle(false, false, 2, 1 + Board.PART_MAX_OFFSET_ABS, 11, false, 'c'));

        Board.printBoard(board);

        assertEquals(2, board.getNumberOfObstacleVehiclesFromHeroToGoal(false));
    }

    @Test
    void getNumberOfPotentialBlockingPartsFromHeroToGoalTest1() throws Exception {
        Board board = new Board();
        board.addVehicle(new Vehicle(true, true, 2, 1 + Board.PART_MAX_OFFSET_ABS, 0, false, '>'));

        board.moveVehicle(board.getVehicleById(">"), 3);
        Board.printBoard(board);
        assertEquals(board.getNumberOfPotentialObstacleBoardPartsFromHeroToGoal(true), 2);
    }

    @Test
    void getNumberOfPotentialBlockingPartsFromHeroToGoalTest2() throws Exception {
        Board board = new Board();
        board.addVehicle(new Vehicle(true, true, 2, 1 + Board.PART_MAX_OFFSET_ABS, 0, false, '>'));

        board.moveVehicle(board.getVehicleById(">"), 4);
        Board.printBoard(board);
        assertEquals(board.getNumberOfPotentialObstacleBoardPartsFromHeroToGoal(true), 1);
    }

    @Test
    void getNumberOfPotentialBlockingPartsFromHeroToGoalTest3() throws Exception {
        Board board = new Board();
        board.addVehicle(new Vehicle(true, true, 2, 1 + Board.PART_MAX_OFFSET_ABS, 0, false, '>'));

        board.moveVehicle(board.getVehicleById(">"), 7);
        Board.printBoard(board);
        assertEquals(board.getNumberOfPotentialObstacleBoardPartsFromHeroToGoal(true), 1);
    }

    @Test
    void getNumberOfPotentialBlockingPartsFromHeroToGoalTest4() throws Exception {
        Board board = new Board();
        board.addVehicle(new Vehicle(true, true, 2, 1 + Board.PART_MAX_OFFSET_ABS, 0, false, '>'));

        board.moveVehicle(board.getVehicleById(">"), 8);
        Board.printBoard(board);
        assertEquals(board.getNumberOfPotentialObstacleBoardPartsFromHeroToGoal(true), 0);
    }

    @Test
    void getNumberOfPotentialBlockingPartsFromHeroToGoalTest5() throws Exception {
        Board board = new Board();
        board.addVehicle(new Vehicle(true, false, 2, 1 + Board.PART_MAX_OFFSET_ABS, 12, false, '<'));

        board.moveVehicle(board.getVehicleById("<"), -3);
        Board.printBoard(board);
        assertEquals(board.getNumberOfPotentialObstacleBoardPartsFromHeroToGoal(false), 2);
    }

    @Test
    void getNumberOfPotentialBlockingPartsFromHeroToGoalTest6() throws Exception {
        Board board = new Board();
        board.addVehicle(new Vehicle(true, false, 2, 1 + Board.PART_MAX_OFFSET_ABS, 12, false, '<'));

        board.moveVehicle(board.getVehicleById("<"), -4);
        Board.printBoard(board);
        assertEquals(board.getNumberOfPotentialObstacleBoardPartsFromHeroToGoal(false), 1);
    }

    @Test
    void getNumberOfObstacleBoardPartsFromHeroToGoal1() throws Exception {
        Board board = new Board();
        board.addVehicle(new Vehicle(true, true, 2, 1 + Board.PART_MAX_OFFSET_ABS, 0, false, '>'));

        Board.printBoard(board);
        assertEquals(board.getNumberOfObstacleBoardPartsFromHeroToGoal(true), 0);
    }

    @Test
    void getNumberOfObstacleBoardPartsFromHeroToGoal2() throws Exception {
        Board board = new Board();
        board.addVehicle(new Vehicle(true, true, 2, 1 + Board.PART_MAX_OFFSET_ABS, 0, false, '>'));
        board.moveLeftPart(-2);
        board.moveRightPart(-2);

        Board.printBoard(board);
        assertEquals(board.getNumberOfObstacleBoardPartsFromHeroToGoal(true), 1);
    }

    @Test
    void getNumberOfObstacleBoardPartsFromHeroToGoal3() throws Exception {
        Board board = new Board();
        board.addVehicle(new Vehicle(true, true, 2, 1 + Board.PART_MAX_OFFSET_ABS, 0, false, '>'));
        board.moveLeftPart(-2);
        board.moveRightPart(2);

        Board.printBoard(board);
        assertEquals(board.getNumberOfObstacleBoardPartsFromHeroToGoal(true), 2);
    }

    @Test
    void getNumberOfObstacleBoardPartsFromHeroToGoal4() throws Exception {
        Board board = new Board();
        board.addVehicle(new Vehicle(true, true, 2, 1 + Board.PART_MAX_OFFSET_ABS, 0, false, '>'));
        board.moveVehicle(board.getVehicleById(">"), 6);
        board.moveLeftPart(2);
        board.moveRightPart(2);

        Board.printBoard(board);
        assertEquals(board.getNumberOfObstacleBoardPartsFromHeroToGoal(true), 1);
    }

    @Test
    void getDistanceToGoal1() throws Exception {
        Board board = new Board();
        board.addVehicle(new Vehicle(true, true, 2, 1 + Board.PART_MAX_OFFSET_ABS, 0, false, '>'));

        Board.printBoard(board);
        assertEquals(board.getDistanceToGoal(true), 12);
    }

    @Test
    void getDistanceToGoal2() throws Exception {
        Board board = new Board();
        board.addVehicle(new Vehicle(true, false, 2, 1 + Board.PART_MAX_OFFSET_ABS, 12, false, '<'));

        Board.printBoard(board);
        assertEquals(board.getDistanceToGoal(false), 12);
    }


}