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


        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                cells[i][j] = new Cell(0,false);
            }
        }
        cells[HEIGHT/2][WIDTH/2].setOilHeight(10000f);

        //dla proby ustawiamy jakis lad
        for (int i = HEIGHT/5; i < HEIGHT/5*2; i++) {
            for (int j = WIDTH/5; j < WIDTH/5*2; j++) {
                cells[i][j] = new Cell(0,true);
            }
        }

    }


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
