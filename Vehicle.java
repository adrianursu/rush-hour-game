public class Vehicle {
    private boolean isMain;
    private int length;
    private int row;
    private int col;
    private boolean isVertical;

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }


    public int getLength() {
        return length;
    }

    public void setRow(int row) {
        this.row = row;

    }

    public void setCol(int col) {
        this.col = col;
    }

    public boolean isVertical() {
        return isVertical;
    }

    public void move(int offset) {
        //if arg 0 => doesn't move
        //if arg positive => moves down/right
        //if arg negative => moves up/left

        if (isVertical) {
            row += offset;
        } else {
            col += offset;
        }
    }

    public Vehicle(boolean isMain, int length, int row, int col, boolean isVertical) {
        this.isMain = isMain;
        this.length = length;
        this.row = row;
        this.col = col;
        this.isVertical = isVertical;
    }

    public static Vehicle createMainVehicle() {
        return new Vehicle(true, 2, -1, 0, false);
    }
}