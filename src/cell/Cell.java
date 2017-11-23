package cell;

public class Cell implements CellInterface {
    float oilHeight;


    @Override
    public void setOilHeight(float height) {
        oilHeight = height;
    }

    public Cell(float oilHeight){
        this.oilHeight = oilHeight;
    }

    @Override
    public float getOilHeight() {
        return oilHeight;
    }
}
