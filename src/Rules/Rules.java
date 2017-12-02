package Rules;

import board.Board;
import cell.Cell;


public class Rules {
    final static float m = 0.14f; //from Cellular Automata Based Model for the Prediction of Oil Slicks Behavior 0.0034
    final static float d = 0.22f;
    final static float borderWanishRatio = 0.89f;//jesli natkiemy sie na granice to przyjmujemy ze ma 90% tyle ropy co nasza oryginalna komorka

    public static float rule(Board board, int i, int j){
        Cell oldCell = board.getCells()[i][j];
        float oldValue = oldCell.getOilHeight();
        float ifBorder = borderWanishRatio * oldValue;
        //Cell newCell = new Cell(oldCell);

        if(oldCell.isLand()) return 0;
        //jesli natykamy sie na lad, to traktujemy go jakby mial tyle ropy co dana komorka co zapobiega jej "gubienu" sie na ladzie
        float sumOfDiffrence = (i > 0?(board.getCells()[i-1][j].isLand() ? oldValue:board.getCells()[i-1][j].getOilHeight()) : ifBorder);
        sumOfDiffrence += (i < board.getHeight()-1? (board.getCells()[i+1][j].isLand() ? oldValue:board.getCells()[i+1][j].getOilHeight()):ifBorder);
        sumOfDiffrence += (j > 0? (board.getCells()[i][j-1].isLand() ? oldValue:board.getCells()[i][j-1].getOilHeight()):ifBorder);
        sumOfDiffrence += (j < board.getWidth()-1? (board.getCells()[i][j+1].isLand() ? oldValue:board.getCells()[i][j+1].getOilHeight()):ifBorder) - 4 * oldValue;
        sumOfDiffrence += d * (i > 0 && j>0?(board.getCells()[i-1][j-1].isLand() ? oldValue:board.getCells()[i-1][j-1].getOilHeight()) : ifBorder);
        sumOfDiffrence += d * (i < board.getHeight()-1 && j>0? (board.getCells()[i+1][j-1].isLand() ? oldValue:board.getCells()[i+1][j-1].getOilHeight()):ifBorder);
        sumOfDiffrence += d *(i > 0 && j < board.getWidth()-1? (board.getCells()[i-1][j+1].isLand() ? oldValue:board.getCells()[i-1][j+1].getOilHeight()):ifBorder);
        sumOfDiffrence += d * ((i < board.getHeight()-1 && j < board.getWidth()-1? (board.getCells()[i+1][j+1].isLand() ? oldValue: board.getCells()[i+1][j+1].getOilHeight()):ifBorder) - 4 * oldValue);

        float hightOfOliAfterGravity = oldCell.getOilHeight() + m * (sumOfDiffrence);

        return hightOfOliAfterGravity;
    }


    public static Board applyRules(Board board){
        Board newBoard = new Board();
        for (int i = 0; i < board.getHeight(); i++) {
            for (int j = 0; j < board.getWidth(); j++) {
                newBoard.getCells()[i][j].setOilHeight(rule(board,i,j));
            }
        }
        return newBoard;
    }


}
