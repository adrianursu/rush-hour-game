public class Vehicle {
    private boolean isMain;
    private final int length;
    private int row;
    private int col;
    private final boolean isVertical;
    private final String id;

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public int getLength() {
        return length;
    }

    public String getId() {
        return id;
    }

    public boolean isVertical() {
        return isVertical;
    }

    public boolean isMain() {
        return isMain;
    }

    public void move(int offset) {
        if (isVertical) {
            row += offset;
        } else {
            col += offset;
        }
    }

    public Vehicle(boolean isMain, int length, int row, int col, boolean isVertical, char id) {
        this.isMain = isMain;
        this.length = length;
        this.row = row;
        this.col = col;
        this.isVertical = isVertical;
        this.id = id + "";
    }

    public Vehicle copy()  {
        return new Vehicle(isMain, length, row, col, isVertical, id.charAt(0)); // Create a new Vehicle object with the same properties (copy constructor)
    }
}