package cell;



public class Cell {
    public enum directions{N,NE,E,SE,S,SW,W,NW}


    public boolean isLand;
    private float oilHeight;
    private int waterCurrent;
    private int windSpeed;
    private directions windDirection;
    private directions currentDirection;
    private int temperature;



    public Cell(Cell oldCell){
        this.isLand = oldCell.isLand;
        this.oilHeight = oldCell.oilHeight;
    }
    public void setOilHeight(float height) {
        oilHeight = height;
    }

    public Cell(float oilHeight, boolean isLand){
        this.oilHeight = oilHeight;
        this.isLand = isLand;
    }

    public float getOilHeight() {
        return oilHeight;
    }
}
