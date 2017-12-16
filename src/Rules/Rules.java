package Rules;

import board.Board;
import board.BoardFromFile;
import cell.Cell;

import static cell.Cell.directions.*;


public class Rules {
    public final static float lengthOfCellSide = 500;

    public static float timePassed = 0f;
    public final static float timeForOneStep = 1.1f;

    //evaporation, CA based
    //final static float p = 0f; //oil evaporation coefficient
    final static float temperature = 293;// in Kelvin
    final static float percentD = 7.87f; //percentage distilled at 180 oC



    //dissolution (rozpuszczanie), CA based model
    final static float K = 0.000003f; // coefficient
    final static float S0 = 10f; //initial solubility, TRZEBA SPRAWDZIC!!
    final static float density = 0.8787f; // gestosc (g/cm^3)


    //wind and current
    final static float windInfluenceOnOtherDirections = 0.25f;
    final static float currentInfluenceOnOtherDirections = 0.1f;
    final static float R = 0.16f; // wind speed to wind-driven current speed, between 0.03 and 0.16

    //shorline deposition
    final static float maxBeachCapacity = 2f;//500f * 5f * 0.1f * 0.15f; //3D: length, width, depth, coeff
    final static float maxLandCapacity = 5f;//500f * 3f * 0.2f * 0.3f;
    final static float P_beach = 0.0005f; //3D Shorline Deposition Coeff
    final static float P_land = 0.0015f;
    final static float lambda = 2 * 24 * 60; //half life (3D)



    //vertical dispersion
    final static float Rs = 0.0001f; //3D
    final static float windCoeffForSubsurface = 0.1f;
    final static float waveLength = 40; // CA based
    final static float waveHeight = 1.5f;//CA based
    final static float wavePeriod = 20;
    final static double Ez = 0.028 * (waveHeight*waveHeight/wavePeriod) * Math.exp(-4*Math.PI/waveLength);
    //final static double Ez = 100; // (cm^2 /s)


    final static float m = 0.1f; //from Cellular Automata Based Model for the Prediction of Oil Slicks Behavior 0.0034, gravity, from Oil Spill Modeling Using 3D: 0.098
    final static float d = 0.22f; //from 3D, coeff to gravity 0.22 or 0.18
    final static float borderWanishRatio = 0.89f;//jesli natkiemy sie na granice to przyjmujemy ze ma 90% tyle ropy co nasza oryginalna komorka







    static float ruleForSurface(Board board, int i, int j){
        Cell oldCell = board.getCells()[i][j];
        Cell[][] cells = board.getCells();
        float oldValue = oldCell.getOilHeight();
        float ifBorder = borderWanishRatio * oldValue;

        float n,ne,e,se,s,sw,w,nw;

        float oilToSubsurface = (float) (0.1 * (Rs * cells[i][j].getOilHeight() * cells[i][j].windSpeed / BoardFromFile.maxWindSpeedEverObserved) + 0.9 * m * Ez * (oldCell.getOilHeight() - oldCell.getOilBelowSurface()));
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

        //System.out.println(n + ", " + ne + ", " + e + ", " + se + ", " + s + ", " + sw + ", " + w + ", " + nw);

        //jesli natykamy sie na lad, to traktujemy go jakby mial tyle ropy co dana komorka co zapobiega jej "gubienu" sie na ladzie

        if(oldCell.isBeach() || oldCell.isLand()) {
            float sumOfDifference = (i > 0?(cells[i-1][j].isLand()||cells[i-1][j].isBeach() ? oldValue:cells[i-1][j].getOilHeight() * (1+n)) : ifBorder);
            sumOfDifference += (i < board.getHeight()-1? (cells[i+1][j].isLand()||cells[i+1][j].isBeach() ? oldValue:cells[i+1][j].getOilHeight() * (1+s)):ifBorder);
            sumOfDifference += (j > 0? (cells[i][j-1].isLand()||cells[i][j-1].isBeach() ? oldValue: cells[i][j-1].getOilHeight()*(1+w)):ifBorder);
            sumOfDifference += (j < board.getWidth()-1? (cells[i][j+1].isLand()||cells[i][j+1].isBeach() ? oldValue:cells[i][j+1].getOilHeight() * (1+e)):ifBorder) - 4 * oldValue;
            sumOfDifference += d * (i > 0 && j>0?(cells[i-1][j-1].isLand()||cells[i-1][j-1].isBeach() ? oldValue:cells[i-1][j-1].getOilHeight()*(1+nw)) : ifBorder);
            sumOfDifference += d * (i < board.getHeight()-1 && j>0? (cells[i+1][j-1].isLand()||cells[i+1][j-1].isBeach() ? oldValue:cells[i+1][j-1].getOilHeight()*(1+sw)):ifBorder);
            sumOfDifference += d *(i > 0 && j < board.getWidth()-1? (cells[i-1][j+1].isLand()||cells[i-1][j+1].isBeach() ? oldValue:cells[i-1][j+1].getOilHeight()*(1+ne)):ifBorder);
            sumOfDifference += d * ((i < board.getHeight()-1 && j < board.getWidth()-1? (cells[i+1][j+1].isLand()||cells[i+1][j+1].isBeach() ? oldValue: cells[i+1][j+1].getOilHeight()*(1+se)):ifBorder) - 4 * oldValue);

            //sumOfDifference += -p * timeForOneStep * temperature;
            float heightOfOli = oldCell.getOilHeight() + m * (sumOfDifference) - (ruleForSubsurface(board,i,j) - oldCell.getOilBelowSurface());

            return heightOfOli;

        }


        float sumOfDifference = (i > 0?cells[i-1][j].getOilHeight() * (1+n) : ifBorder);
        sumOfDifference += (i < board.getHeight()-1?cells[i+1][j].getOilHeight() * (1+s):ifBorder);
        sumOfDifference += (j > 0? cells[i][j-1].getOilHeight()*(1+w):ifBorder);
        sumOfDifference += (j < board.getWidth()-1? cells[i][j+1].getOilHeight() * (1+e):ifBorder) - 4 * oldValue;
        sumOfDifference += d * (i > 0 && j>0?cells[i-1][j-1].getOilHeight()*(1+nw) : ifBorder);
        sumOfDifference += d * (i < board.getHeight()-1 && j>0?cells[i+1][j-1].getOilHeight()*(1+sw):ifBorder);
        sumOfDifference += d *(i > 0 && j < board.getWidth()-1? cells[i-1][j+1].getOilHeight()*(1+ne):ifBorder);
        sumOfDifference += d * ((i < board.getHeight()-1 && j < board.getWidth()-1? cells[i+1][j+1].getOilHeight()*(1+se):ifBorder) - 4 * oldValue);



       /* float sumOfDifference = (i > 0?(cells[i-1][j].isLand()||cells[i-1][j].isBeach() ? oldValue:cells[i-1][j].getOilHeight() * (1+n)) : ifBorder);
        sumOfDifference += (i < board.getHeight()-1? (cells[i+1][j].isLand()||cells[i+1][j].isBeach() ? oldValue:cells[i+1][j].getOilHeight() * (1+s)):ifBorder);
        sumOfDifference += (j > 0? (cells[i][j-1].isLand()||cells[i][j-1].isBeach() ? oldValue: cells[i][j-1].getOilHeight()*(1+w)):ifBorder);
        sumOfDifference += (j < board.getWidth()-1? (cells[i][j+1].isLand()||cells[i][j+1].isBeach() ? oldValue:cells[i][j+1].getOilHeight() * (1+e)):ifBorder) - 4 * oldValue;
        sumOfDifference += d * (i > 0 && j>0?(cells[i-1][j-1].isLand()||cells[i-1][j-1].isBeach() ? oldValue:cells[i-1][j-1].getOilHeight()*(1+nw)) : ifBorder);
        sumOfDifference += d * (i < board.getHeight()-1 && j>0? (cells[i+1][j-1].isLand()||cells[i+1][j-1].isBeach() ? oldValue:cells[i+1][j-1].getOilHeight()*(1+sw)):ifBorder);
        sumOfDifference += d *(i > 0 && j < board.getWidth()-1? (cells[i-1][j+1].isLand()||cells[i-1][j+1].isBeach() ? oldValue:cells[i-1][j+1].getOilHeight()*(1+ne)):ifBorder);
        sumOfDifference += d * ((i < board.getHeight()-1 && j < board.getWidth()-1? (cells[i+1][j+1].isLand()||cells[i+1][j+1].isBeach() ? oldValue: cells[i+1][j+1].getOilHeight()*(1+se)):ifBorder) - 4 * oldValue);*/


        //sumOfDifference += -p * timeForOneStep * temperature;
        float heightOfOli = oldCell.getOilHeight() + m * (sumOfDifference) - oilToSubsurface;
//        float dissolution = 0.001 * (K * (lengthOfCellSide*lengthOfCellSide)* S0 * Math.exp(-0.1 * timePassed)) * timeForOneStep / (density * 119.24); //odejmujemy ile sie rozpuscilo, 119- zamieniamy na barrel

        float evaporation = (float) (0.0000005 * (0.165 * percentD + 0.45 * (temperature - 15)) * Math.log(timeForOneStep*60*60) * oldValue);
       // float evaporation = 0;
        heightOfOli = heightOfOli - evaporation;
        if (heightOfOli > 0) return heightOfOli;
        return 0;
    }


    static float ruleForSubsurface(Board board, int i, int j){

        Cell[][] cells = board.getCells();
        Cell oldCell = cells[i][j];
        float oldValue = oldCell.getOilBelowSurface();
        float ifBorder = borderWanishRatio * oldValue;
        float oilFromSurface = (float) (0.1 * (Rs * cells[i][j].getOilHeight() * cells[i][j].windSpeed / BoardFromFile.maxWindSpeedEverObserved) + 0.9 * m * Ez * (oldCell.getOilHeight() - oldCell.getOilBelowSurface()));



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



        if(oldCell.isLand()){

            //float newValue = (float) (oldCell.oilBelowSurface * Math.exp(-timeForOneStep * (-Math.log(1/2)/lambda)) + P_land * oldCell.getOilHeight());
            float newValue = oldCell.oilBelowSurface + P_land * oldCell.getOilHeight();
            if(newValue > maxLandCapacity) return maxLandCapacity;
            return newValue;

        }

        if(oldCell.isBeach()){

            //float newValue = (float) (oldCell.oilBelowSurface * Math.exp(-timeForOneStep * (-Math.log(1/2)/lambda)) + P_beach * oldCell.getOilHeight());
            float newValue = oldCell.oilBelowSurface + P_beach * oldCell.getOilHeight();
            if(newValue > maxBeachCapacity) return maxBeachCapacity;
            return newValue;

        }


        float sumOfDifference = (i > 0?(cells[i-1][j].isLand()||cells[i-1][j].isBeach() ? oldValue:cells[i-1][j].getOilBelowSurface() * (1+n)) : ifBorder);
        sumOfDifference += (i < board.getHeight()-1? (cells[i+1][j].isLand()||cells[i+1][j].isBeach() ? oldValue:cells[i+1][j].getOilBelowSurface() * (1+s)):ifBorder);
        sumOfDifference += (j > 0? (cells[i][j-1].isLand()||cells[i][j-1].isBeach() ? oldValue: cells[i][j-1].getOilBelowSurface()*(1+w)):ifBorder);
        sumOfDifference += (j < board.getWidth()-1? (cells[i][j+1].isLand()||cells[i][j+1].isBeach() ? oldValue:cells[i][j+1].getOilBelowSurface() * (1+e)):ifBorder) - 4 * oldValue;
        sumOfDifference += d * (i > 0 && j>0?(cells[i-1][j-1].isLand()||cells[i-1][j-1].isBeach() ? oldValue:cells[i-1][j-1].getOilBelowSurface()*(1+nw)) : ifBorder);
        sumOfDifference += d * (i < board.getHeight()-1 && j>0? (cells[i+1][j-1].isLand()||cells[i+1][j-1].isBeach() ? oldValue:cells[i+1][j-1].getOilBelowSurface()*(1+sw)):ifBorder);
        sumOfDifference += d *(i > 0 && j < board.getWidth()-1? (cells[i-1][j+1].isLand()||cells[i-1][j+1].isBeach() ? oldValue:cells[i-1][j+1].getOilBelowSurface()*(1+ne)):ifBorder);
        sumOfDifference += d * ((i < board.getHeight()-1 && j < board.getWidth()-1? (cells[i+1][j+1].isLand()||cells[i+1][j+1].isBeach() ? oldValue: cells[i+1][j+1].getOilBelowSurface()*(1+se)):ifBorder) - 4 * oldValue);

        float subsurfaceOil = oilFromSurface + oldCell.getOilBelowSurface() + m * (sumOfDifference);

        double dissolution=  oldCell.oilBelowSurface * (K * (lengthOfCellSide*lengthOfCellSide)* S0 * Math.exp(-0.1 * timePassed)) * (timeForOneStep * 60 * 60) / (density * 119.24); //odejmujemy ile sie rozpuscilo, 119- zamieniamy na barrel

        subsurfaceOil = subsurfaceOil - (float)dissolution;
        if(subsurfaceOil > 0) return subsurfaceOil;
        return 0;

        //return subsurfaceOil;
    }

    public static Board applyRules(Board board){
        Board newBoard = new Board();
        for (int i = 0; i < board.getHeight(); i++) {
            for (int j = 0; j < board.getWidth(); j++) {
                newBoard.getCells()[i][j].setOilHeight(ruleForSurface(board,i,j));
                newBoard.getCells()[i][j].setOilBelowSurface(ruleForSubsurface(board,i,j));
            }
        }
        return newBoard;
    }


}
