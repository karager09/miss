package Rules;

import board.Board;
import board.BoardFromFile;
import cell.Cell;

import static cell.Cell.directions.*;


public class Rules {


    //evaporation
    final static float p = 0f; //oil evaporation coefficient
    final static float timeStep = 1;
    final static float temperature = 273;// in Kelvin

    //wind and current
    final static float windInfluenceOnOtherDirections = 0.25f;
    final static float currentInfluenceOnOtherDirections = 0.1f;
    final static float R = 0.16f; // wind speed to wind-driven current speed, between 0.03 and 0.16

    //shorline deposition
    final static float maxBeachCapacity = 500f * 20f * 0.2f * 0.3f; //3D: length, width, depth, coeff

    //vertical dispersion
    final static float Rs = 0.0001f; //3D
    final static float windCoeffForSubsurface = 0.1f;


    final static float m = 0.1f; //from Cellular Automata Based Model for the Prediction of Oil Slicks Behavior 0.0034, gravity, from Oil Spill Modeling Using 3D: 0.098
    final static float d = 0.22f; //from 3D, coeff to gravity 0.22 or 0.18
    final static float borderWanishRatio = 0.89f;//jesli natkiemy sie na granice to przyjmujemy ze ma 90% tyle ropy co nasza oryginalna komorka


    static float ruleForSurface(Board board, int i, int j){
        Cell oldCell = board.getCells()[i][j];
        Cell[][] cells = board.getCells();
        float oldValue = oldCell.getOilHeight();
        float ifBorder = borderWanishRatio * oldValue;

        float n,ne,e,se,s,sw,w,nw;

        float windCoeff = R * cells[i][j].windSpeed / BoardFromFile.maxWindSpeedEverObserved;
        float currentCoeff = cells[i][j].currentSpeed / BoardFromFile.maxCurrentSpeedEverObserved;

        float tableOfCoeff[] = new float[8];
        tableOfCoeff[0] = windCoeff;
        tableOfCoeff[1] = windInfluenceOnOtherDirections * windCoeff;
        tableOfCoeff[2] = 0;
        tableOfCoeff[3] = -windInfluenceOnOtherDirections * windCoeff;
        tableOfCoeff[4] = -windCoeff;
        tableOfCoeff[5] = -windInfluenceOnOtherDirections * windCoeff;
        tableOfCoeff[6] = 0;
        tableOfCoeff[7] = windInfluenceOnOtherDirections * windCoeff;

        int numberOfDirection = cells[i][j].windDirection.ordinal();

        n = tableOfCoeff[(8 - numberOfDirection) % 8];
        ne = tableOfCoeff[(9 - numberOfDirection) % 8];
        e = tableOfCoeff[(10 - numberOfDirection) % 8];
        se = tableOfCoeff[(11 - numberOfDirection) % 8];
        s = tableOfCoeff[(12 - numberOfDirection) % 8];
        sw = tableOfCoeff[(13 - numberOfDirection) % 8];
        w = tableOfCoeff[(14 - numberOfDirection) % 8];
        nw = tableOfCoeff[(15 - numberOfDirection) % 8];



        tableOfCoeff[0] = currentCoeff;
        tableOfCoeff[1] = currentInfluenceOnOtherDirections * currentCoeff;
        tableOfCoeff[2] = 0;
        tableOfCoeff[3] = -currentInfluenceOnOtherDirections * currentCoeff;
        tableOfCoeff[4] = -currentCoeff;
        tableOfCoeff[5] = -currentInfluenceOnOtherDirections * currentCoeff;
        tableOfCoeff[6] = 0;
        tableOfCoeff[7] = currentInfluenceOnOtherDirections * currentCoeff;

        numberOfDirection = cells[i][j].currentDirection.ordinal();

        n += tableOfCoeff[(8 - numberOfDirection) % 8];
        ne += tableOfCoeff[(9 - numberOfDirection) % 8];
        e += tableOfCoeff[(10 - numberOfDirection) % 8];
        se += tableOfCoeff[(11 - numberOfDirection) % 8];
        s += tableOfCoeff[(12 - numberOfDirection) % 8];
        sw += tableOfCoeff[(13 - numberOfDirection) % 8];
        w += tableOfCoeff[(14 - numberOfDirection) % 8];
        nw += tableOfCoeff[(15 - numberOfDirection) % 8];



        if(oldCell.isLand()) return 0;
        //jesli natykamy sie na lad, to traktujemy go jakby mial tyle ropy co dana komorka co zapobiega jej "gubienu" sie na ladzie

        if(oldCell.isBeach()) return 0;


        float sumOfDiffrence = (i > 0?(cells[i-1][j].isLand()||cells[i-1][j].isBeach() ? oldValue:cells[i-1][j].getOilHeight() * (1+n)) : ifBorder);
        sumOfDiffrence += (i < board.getHeight()-1? (cells[i+1][j].isLand()||cells[i+1][j].isBeach() ? oldValue:cells[i+1][j].getOilHeight() * (1+s)):ifBorder);
        sumOfDiffrence += (j > 0? (cells[i][j-1].isLand()||cells[i][j-1].isBeach() ? oldValue: cells[i][j-1].getOilHeight()*(1+w)):ifBorder);
        sumOfDiffrence += (j < board.getWidth()-1? (cells[i][j+1].isLand()||cells[i][j+1].isBeach() ? oldValue:cells[i][j+1].getOilHeight() * (1+e)):ifBorder) - 4 * oldValue;
        sumOfDiffrence += d * (i > 0 && j>0?(cells[i-1][j-1].isLand()||cells[i-1][j-1].isBeach() ? oldValue:cells[i-1][j-1].getOilHeight()*(1+nw)) : ifBorder);
        sumOfDiffrence += d * (i < board.getHeight()-1 && j>0? (cells[i+1][j-1].isLand()||cells[i+1][j-1].isBeach() ? oldValue:cells[i+1][j-1].getOilHeight()*(1+sw)):ifBorder);
        sumOfDiffrence += d *(i > 0 && j < board.getWidth()-1? (cells[i-1][j+1].isLand()||cells[i-1][j+1].isBeach() ? oldValue:cells[i-1][j+1].getOilHeight()*(1+ne)):ifBorder);
        sumOfDiffrence += d * ((i < board.getHeight()-1 && j < board.getWidth()-1? (cells[i+1][j+1].isLand()||cells[i+1][j+1].isBeach() ? oldValue: cells[i+1][j+1].getOilHeight()*(1+se)):ifBorder) - 4 * oldValue);

        sumOfDiffrence += -p * timeStep * temperature;
        float hightOfOli = oldCell.getOilHeight() + m * (sumOfDiffrence);

        return hightOfOli;
    }


    static float ruleForSubsurface(Board board, int i, int j){

        Cell[][] cells = board.getCells();
        Cell oldCell = cells[i][j];
        float oldValue = oldCell.getOilHeight();
        float ifBorder = borderWanishRatio * oldValue;
        float oilFromSurface = Rs * cells[i][j].getOilHeight() * cells[i][j].windSpeed / BoardFromFile.maxWindSpeedEverObserved;


        float n,ne,e,se,s,sw,w,nw;

        float windCoeff = windCoeffForSubsurface * R * cells[i][j].windSpeed / BoardFromFile.maxWindSpeedEverObserved;
        float currentCoeff = cells[i][j].currentSpeed / BoardFromFile.maxCurrentSpeedEverObserved;

        float tableOfCoeff[] = new float[8];
        tableOfCoeff[0] = windCoeff;
        tableOfCoeff[1] = windInfluenceOnOtherDirections * windCoeff;
        tableOfCoeff[2] = 0;
        tableOfCoeff[3] = -windInfluenceOnOtherDirections * windCoeff;
        tableOfCoeff[4] = -windCoeff;
        tableOfCoeff[5] = -windInfluenceOnOtherDirections * windCoeff;
        tableOfCoeff[6] = 0;
        tableOfCoeff[7] = windInfluenceOnOtherDirections * windCoeff;

        int numberOfDirection = cells[i][j].windDirection.ordinal();

        n = tableOfCoeff[(8 - numberOfDirection) % 8];
        ne = tableOfCoeff[(9 - numberOfDirection) % 8];
        e = tableOfCoeff[(10 - numberOfDirection) % 8];
        se = tableOfCoeff[(11 - numberOfDirection) % 8];
        s = tableOfCoeff[(12 - numberOfDirection) % 8];
        sw = tableOfCoeff[(13 - numberOfDirection) % 8];
        w = tableOfCoeff[(14 - numberOfDirection) % 8];
        nw = tableOfCoeff[(15 - numberOfDirection) % 8];



        tableOfCoeff[0] = currentCoeff;
        tableOfCoeff[1] = currentInfluenceOnOtherDirections * currentCoeff;
        tableOfCoeff[2] = 0;
        tableOfCoeff[3] = -currentInfluenceOnOtherDirections * currentCoeff;
        tableOfCoeff[4] = -currentCoeff;
        tableOfCoeff[5] = -currentInfluenceOnOtherDirections * currentCoeff;
        tableOfCoeff[6] = 0;
        tableOfCoeff[7] = currentInfluenceOnOtherDirections * currentCoeff;

        numberOfDirection = cells[i][j].currentDirection.ordinal();

        n += tableOfCoeff[(8 - numberOfDirection) % 8];
        ne += tableOfCoeff[(9 - numberOfDirection) % 8];
        e += tableOfCoeff[(10 - numberOfDirection) % 8];
        se += tableOfCoeff[(11 - numberOfDirection) % 8];
        s += tableOfCoeff[(12 - numberOfDirection) % 8];
        sw += tableOfCoeff[(13 - numberOfDirection) % 8];
        w += tableOfCoeff[(14 - numberOfDirection) % 8];
        nw += tableOfCoeff[(15 - numberOfDirection) % 8];



        if(oldCell.isLand()) return 0;
        //jesli natykamy sie na lad, to traktujemy go jakby mial tyle ropy co dana komorka co zapobiega jej "gubienu" sie na ladzie

        if(oldCell.isBeach()) return 0;


        float sumOfDiffrence = (i > 0?(cells[i-1][j].isLand()||cells[i-1][j].isBeach() ? oldValue:cells[i-1][j].getSubsurfaceOil() * (1+n)) : ifBorder);
        sumOfDiffrence += (i < board.getHeight()-1? (cells[i+1][j].isLand()||cells[i+1][j].isBeach() ? oldValue:cells[i+1][j].getSubsurfaceOil() * (1+s)):ifBorder);
        sumOfDiffrence += (j > 0? (cells[i][j-1].isLand()||cells[i][j-1].isBeach() ? oldValue: cells[i][j-1].getSubsurfaceOil()*(1+w)):ifBorder);
        sumOfDiffrence += (j < board.getWidth()-1? (cells[i][j+1].isLand()||cells[i][j+1].isBeach() ? oldValue:cells[i][j+1].getSubsurfaceOil() * (1+e)):ifBorder) - 4 * oldValue;
        sumOfDiffrence += d * (i > 0 && j>0?(cells[i-1][j-1].isLand()||cells[i-1][j-1].isBeach() ? oldValue:cells[i-1][j-1].getSubsurfaceOil()*(1+nw)) : ifBorder);
        sumOfDiffrence += d * (i < board.getHeight()-1 && j>0? (cells[i+1][j-1].isLand()||cells[i+1][j-1].isBeach() ? oldValue:cells[i+1][j-1].getSubsurfaceOil()*(1+sw)):ifBorder);
        sumOfDiffrence += d *(i > 0 && j < board.getWidth()-1? (cells[i-1][j+1].isLand()||cells[i-1][j+1].isBeach() ? oldValue:cells[i-1][j+1].getSubsurfaceOil()*(1+ne)):ifBorder);
        sumOfDiffrence += d * ((i < board.getHeight()-1 && j < board.getWidth()-1? (cells[i+1][j+1].isLand()||cells[i+1][j+1].isBeach() ? oldValue: cells[i+1][j+1].getSubsurfaceOil()*(1+se)):ifBorder) - 4 * oldValue);

        float subsurfaceOil = oilFromSurface + oldCell.getSubsurfaceOil() + m * (sumOfDiffrence);

        return subsurfaceOil;
    }

    public static Board applyRules(Board board){
        Board newBoard = new Board(1);
        for (int i = 0; i < board.getHeight(); i++) {
            for (int j = 0; j < board.getWidth(); j++) {
                newBoard.getCells()[i][j].setOilHeight(ruleForSurface(board,i,j));
                newBoard.getCells()[i][j].setOilBelowSurface(ruleForSubsurface(board,i,j));
            }
        }
        return newBoard;
    }


}
