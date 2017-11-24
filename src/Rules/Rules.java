package Rules;

import board.Board;
import cell.Cell;


public class Rules {
    final static float m = 0.14f; //from Cellular Automata Based Model for the Prediction of Oil Slicks Behavior 0.0034
    final static float d = 0.22f;

    public static float rule(Board board, int i, int j){
        Cell oldCell = board.getCells()[i][j];
        Cell newCell = new Cell(0);
        float sumOfDiffrence = (i > 0?board.getCells()[i-1][j].getOilHeight() : 0) + (i < board.getHeight()-1? board.getCells()[i+1][j].getOilHeight():0) + (j > 0? board.getCells()[i][j-1].getOilHeight():0) + (j < board.getWidth()-1? board.getCells()[i][j+1].getOilHeight():0) - 4 * oldCell.getOilHeight() + d * ((i > 0 && j>0?board.getCells()[i-1][j-1].getOilHeight() : 0) + (i < board.getHeight()-1 && j>0? board.getCells()[i+1][j-1].getOilHeight():0) + (i > 0 && j < board.getWidth()-1? board.getCells()[i-1][j+1].getOilHeight():0) + (i < board.getHeight()-1 && j < board.getWidth()-1? board.getCells()[i+1][j+1].getOilHeight():0) - 4 * oldCell.getOilHeight());

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
