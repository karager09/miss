package board;

import cell.Cell;
import cell.CellInterface;

public class Board implements BoardInterface{

    int HEIGHT = 100, WIDTH = 100;
    CellInterface[][] cells;

    public Board() {


        cells = new CellInterface[HEIGHT][WIDTH];

        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; WIDTH < 100; j++) {
                cells[HEIGHT][WIDTH] = new Cell(1.3f);
            }

        }

    }

    @Override
    public float[][] currentState() {
        return new float[HEIGHT][WIDTH];
    }

    @Override
    public int getHeight() {
        return HEIGHT;
    }

    @Override
    public int getWidth() {
        return WIDTH;
    }
}
