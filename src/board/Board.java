package board;

import cell.Cell;

public class Board {

    int HEIGHT = 100, WIDTH = 100;
    private Cell[][] cells;

    public Cell[][] getCells() {
        return cells;
    }

    public Board() {


        cells = new Cell[HEIGHT][WIDTH];

//        for (CellInterface[] cTable: cells) {
//            for (CellInterface c:cTable) {
//                c = new Cell(0);
//            }
//
//        }
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                cells[i][j] = new Cell(0);
            }
        }
        cells[HEIGHT/2][WIDTH/2].setOilHeight(10000f);

    }

        //public float[][] currentState() { return new float[HEIGHT][WIDTH]; }



    public void print() {
        for (Cell[] cTable : cells) {
            for (Cell c : cTable) {
                System.out.print("|" + c.getOilHeight());

            }
            System.out.println("|");
        }
    }


    public int getHeight() {
        return HEIGHT;
    }

    public int getWidth() {
        return WIDTH;
    }
}
