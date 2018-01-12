package cell;



public class Cell {
    public enum directions{N,NE,E,SE,S,SW,W,NW}
    public enum type{WATER,LAND,BEACH}


    private type typeOfLand;
    private float oilHeight;
    public float oilBelowSurface;
    public float currentSpeed;
    public float windSpeed;
    public directions windDirection;
    public directions currentDirection;
    public int temperature;


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

    public float getOilHeight() {return oilHeight; }

    public void setOilBelowSurface(float oilBelowSurface){ this.oilBelowSurface = oilBelowSurface; }

    public float getOilBelowSurface(){return oilBelowSurface;}


    public boolean isLand(){
        return typeOfLand == type.LAND;
    }

    public boolean isBeach(){
        return typeOfLand == type.BEACH;
    }

}
