package board;

import cell.Cell;

public class Board {

    public static int HEIGHT = 300, WIDTH = 300;
    private Cell[][] cells;

    public Cell[][] getCells() {
        return cells;
    }

    public Board(int n) {
        n = n-1;
        if (BoardFromFile.cellsWithCurrentSpeed.length - 1 < n) n = BoardFromFile.cellsWithCurrentSpeed.length - 1;

        cells = new Cell[HEIGHT][WIDTH];


        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                cells[i][j] = new Cell(BoardFromFile.cellsWithLandType[i][j], BoardFromFile.cellsWithCurrentSpeed[n][i][j], BoardFromFile.cellsWithWindSpeed[n][i][j],BoardFromFile.cellsWithWindDirection[n][i][j],BoardFromFile.cellsWithCurrentDirection[n][i][j],20);
            }
        }






        cells[HEIGHT/2][WIDTH/2].setOilHeight(10000f);

        //dla proby ustawiamy jakis lad
//        for (int i = HEIGHT/5; i < HEIGHT/5*2; i++) {
//            for (int j = WIDTH/5; j < WIDTH/5*2; j++) {
//                cells[i][j] = new Cell(0,true);
//            }
//        }

    }


    public void print() {
        for (Cell[] cTable : cells) {
            for (Cell c : cTable) {
                System.out.print("|" + c.getOilBelowSurface());

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
