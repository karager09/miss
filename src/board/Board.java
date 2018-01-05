package board;

import Rules.Rules;
import app.Controller;
import cell.Cell;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Board {

    private int nn;

    public static final float[] whenWindAndCurrentChanges = new float[]{};
    public static final int[] temperature = new int[]{273,273};
    public static int HEIGHT = 400, WIDTH = 400;
    private Cell[][] cells;

    public Cell[][] getCells() {
        return cells;
    }


    public static Board getNewBoard(){
        int n = 0;
        while(n<whenWindAndCurrentChanges.length && Rules.timePassed >= whenWindAndCurrentChanges[n] && n < BoardFromFile.cellsWithCurrentSpeed.length) ++n;

        if(Controller.board_tmp.nn != n){
            return new Board();
        } else {
            return Controller.board_tmp;
        }

    }


    public Board() {

        cells = new Cell[HEIGHT][WIDTH];

        int n = 0;
        while(n<whenWindAndCurrentChanges.length && Rules.timePassed >= whenWindAndCurrentChanges[n] && n < BoardFromFile.cellsWithCurrentSpeed.length) ++n;

        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                cells[i][j] = new Cell(BoardFromFile.cellsWithLandType[i][j], BoardFromFile.cellsWithCurrentSpeed[n][i][j], BoardFromFile.cellsWithWindSpeed[n][i][j],BoardFromFile.cellsWithWindDirection[n][i][j],BoardFromFile.cellsWithCurrentDirection[n][i][j],temperature[n]);
            }
        }

        this.nn = n;

        cells[HEIGHT/4][WIDTH/2].setOilHeight(100000f);

    }

/*
    public void print() {
        for (Cell[] cTable : cells) {
            for (Cell c : cTable) {
                System.out.print("|" + c.getOilBelowSurface());

            }
            System.out.println("|");
        }
    }*/


/*
    public float getMaxValueSurface(){
        float maxValue = 0;
        int amountOfThreads = 20;

        ExecutorService executor = Executors.newFixedThreadPool(amountOfThreads);
        List<Future<Float>> list = new ArrayList<Future<Float>>();

        for (int i = 0; i < amountOfThreads; ++i){
            Future<Float> future = executor.submit(new MyCallable((i / amountOfThreads) * getHeight(), (((i+1)/ amountOfThreads) * getHeight() - 1), true));
            list.add(future);
        }

        for(Future<Float> fut : list){
            try {
                if(maxValue < fut.get()) maxValue = fut.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        executor.shutdown();

        return maxValue;
    }



    class MyCallable implements Callable<Float> {
        private int begin,end;
        boolean isSurface;

        public MyCallable(int begin, int end, boolean isSurface) {
            this.begin = begin;
            this.end = end;
            this.isSurface = isSurface;
        }

        @Override
        public Float call() {
            float maxValue = 0;

            if(isSurface == true) {
                for (int i = begin; i < end; i++) {
                    for (int j = 0; j < getWidth(); j++) {
                        if (maxValue < getCells()[i][j].getOilHeight())
                            maxValue = getCells()[i][j].getOilHeight();
                    }
                }
            }
            else {
                for (int i = begin; i < end; i++) {
                    for (int j = 0; j < getWidth(); j++) {
                        if (maxValue < getCells()[i][j].getOilBelowSurface())
                            maxValue = getCells()[i][j].getOilBelowSurface();
                    }
                }
            }

            return maxValue;
        }
    }



    public float getMaxValueSubsurface(){

        float maxValue = 0;
        int amountOfThreads = 20;

        ExecutorService executor = Executors.newFixedThreadPool(amountOfThreads);
        List<Future<Float>> list = new ArrayList<Future<Float>>();

        for (int i = 0; i < amountOfThreads; ++i){
            Future<Float> future = executor.submit(new MyCallable((i / amountOfThreads) * getHeight(), (((i+1)/ amountOfThreads) * getHeight() - 1), false));
            list.add(future);
        }

        for(Future<Float> fut : list){
            try {
                if(maxValue < fut.get()) maxValue = fut.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        executor.shutdown();
        return maxValue;

    }*/

    public int getHeight() {
        return HEIGHT;
    }

    public int getWidth() {
        return WIDTH;
    }
}
