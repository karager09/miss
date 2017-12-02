package cell;



public class Cell {
    public enum directions{N,NE,E,SE,S,SW,W,NW}
    public enum type{WATER,LAND,BEACH}


    private type typeOfLand;
    private float oilHeight;
    private float oilBelowSurface;
    private float currentSpeed;
    private float windSpeed;
    private directions windDirection;
    private directions currentDirection;
    private int temperature;



    public Cell(Cell oldCell){
        this.typeOfLand = oldCell.typeOfLand;
        this.oilHeight = oldCell.oilHeight;

    }


    public Cell(float oilHeight, type typeOfLand){
        this.oilHeight = oilHeight;
        this.typeOfLand = typeOfLand;
    }


    public Cell(type typeOfLand, float currentSpeed, float windSpeed, directions windDirection, directions currentDirection, int temperature) {
        this.typeOfLand = typeOfLand;
        this.currentSpeed = currentSpeed;
        this.windSpeed = windSpeed;
        this.windDirection = windDirection;
        this.currentDirection = currentDirection;
        this.temperature = temperature;
    }

    public void setOilHeight(float height) {
        oilHeight = height;
    }

    public float getOilHeight() {
        return oilHeight;
    }


    public boolean isLand(){
        if (typeOfLand == type.WATER) return false;
        return true;
    }

}
