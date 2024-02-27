public class Main {
    public static void main(String[] args) {
        Board b = new Board();

        Vehicle v1 = Vehicle.createMainVehicle();
        v1.setRow(2);

        b.addVehicle(v1);

        b.printBoard();



        b.moveVehicle(v1, 1);

        b.printBoard();
    }

    private Board getRandomInitialState() {
        return new Board();
    }
}
