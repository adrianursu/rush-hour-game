public class Vehicle {
    private final boolean isHero;
    private final boolean isLeft; //this only matters if isHero
    private final int length;
    private int rowStart;
    private int colStart;
    private final boolean isVertical;
    private final String id;

    public int getRowStart() {
        return rowStart;
    }

    public void setRowStart(int rowStart) {
        this.rowStart = rowStart;
    }

    public int getRowEnd() {
        return rowStart + length - 1;
    }

    public int getColStart() {
        return colStart;
    }

    public int getColEnd() {
        return colStart + length - 1;
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

    public boolean isHero() {
        return isHero;
    }

    public boolean isLeft() {
        return isLeft; //this only matters if isHero
    }

    public void move(int offset) {
        if (isVertical) {
            rowStart += offset;
        } else {
            colStart += offset;
        }
    }

    public Vehicle(boolean isHero, boolean isLeft, int length, int rowStart, int colStart, boolean isVertical, char id) {
        this.isHero = isHero;
        this.isLeft = isLeft;
        this.length = length;
        this.rowStart = rowStart;
        this.colStart = colStart;
        this.isVertical = isVertical;
        this.id = id + "";
    }

    public Vehicle copy() {
        return new Vehicle(isHero, isLeft, length, rowStart, colStart, isVertical, id.charAt(0));
    }
}