package board;

import cell.Cell;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

import static cell.Cell.directions.*;
import static cell.Cell.type.*;

public class BoardFromFile {
    public static float maxWindSpeedEverObserved = 100;
    public static float maxCurrentSpeedEverObserved = 30;

    public static float maxWindSpeed = 25;
    public static float maxCurrentSpeed = 10;

    public static Cell.type[][] cellsWithLandType;
    public static float[][][] cellsWithWindSpeed;
    public static Cell.directions[][][] cellsWithWindDirection;
    public static float[][][] cellsWithCurrentSpeed;
    public static Cell.directions[][][] cellsWithCurrentDirection;


    public static void getAllCellsFromFile(){

        cellsWithLandType = new Cell.type[Board.HEIGHT][Board.WIDTH];
        try {
            File file = new File("lands.jpg");
            BufferedImage image = ImageIO.read(file);

            for (int i = 0; i < Board.HEIGHT; i++) {
                for (int j = 0; j < Board.WIDTH; j++) {
                    int pxl = image.getRGB(j, i);
                    if((pxl & 0x00ffffff) > 0xeeeeee) //czy jest białe, jak tak to morze
                        cellsWithLandType[i][j] = WATER;
                    else if((pxl & 0x00ffffff) > 0x555555)
                        cellsWithLandType[i][j] = BEACH;
                    else cellsWithLandType[i][j] = LAND;
                }
            }
        } catch (Exception e){
            System.out.println("Something went wrong with the file: lands.jpg");
        }




        File[] listOfFiles = new File(".").listFiles();
        //System.out.println(listOfFiles.length);
        int windCount = 0, currentCount = 0;
        for (File f:listOfFiles) {
            if(f.getName().contains("windspeed")) windCount++;
            if (f.getName().contains("currentspeed")) currentCount++;
        }
        //System.out.println(windCount);

        cellsWithWindSpeed = new float[windCount][][];
        for (int n = 1; n <= windCount; n++) {
            cellsWithWindSpeed[n - 1] = new float[Board.HEIGHT][Board.WIDTH];

            try {
                File file = new File("windspeed"+n+".jpg");
                BufferedImage image = ImageIO.read(file);

                for (int i = 0; i < Board.HEIGHT; i++) {
                    for (int j = 0; j < Board.WIDTH; j++) {
                        int pxl = image.getRGB(j, i);
                        cellsWithWindSpeed[n-1][i][j] = maxWindSpeed * (0x00ffffff - pxl & 0x00ffffff) / 0x00ffffff;
                    }
                }
            } catch (Exception e){
                System.out.println("Something went wrong with the file: windspeed.jpg");
            }
        }



        cellsWithCurrentSpeed = new float[currentCount][][];
        for (int n = 1; n <= currentCount; n++) {
            cellsWithCurrentSpeed[n - 1] = new float[Board.HEIGHT][Board.WIDTH];

            try {
                File file = new File("currentspeed" + n + ".jpg");
                BufferedImage image = ImageIO.read(file);

                for (int i = 0; i < Board.HEIGHT; i++) {
                    for (int j = 0; j < Board.WIDTH; j++) {
                        int pxl = image.getRGB(j, i);
                        cellsWithCurrentSpeed[n - 1][i][j] = maxCurrentSpeed * (0x00ffffff - pxl & 0x00ffffff) / 0x00ffffff;
                    }
                }
            } catch (Exception e) {
                System.out.println("Something went wrong with the file: currentspeed.jpg");
            }
        }



        cellsWithWindDirection = new Cell.directions[windCount][][];
        for (int n = 1; n <= windCount; n++) {
            cellsWithWindDirection[n - 1] = new Cell.directions[Board.HEIGHT][Board.WIDTH];

            try {
                File file = new File("winddirection"+n+".jpg");
                BufferedImage image = ImageIO.read(file);

                for (int i = 0; i < Board.HEIGHT; i++) {
                    for (int j = 0; j < Board.WIDTH; j++) {
                        int pxl = image.getRGB(j, i);
                        int  red   = (pxl & 0x00ff0000) >> 16;
                        int  green = (pxl & 0x0000ff00) >> 8;
                        int  blue  =  pxl & 0x000000ff;
                        //cellsWithWindSpeed[n-1][i][j] = maxWindSpeed * (0x00ffffff - pxl & 0x00ffffff) / 0x00ffffff;
                    if(green > 170 && blue < 127 && red < 127) cellsWithWindDirection[n-1][i][j] = N;
                    else if(green > 170 && blue > 127 && red < 127) cellsWithWindDirection[n-1][i][j] = NE;
                    else if(green < 170 && green > 85 && blue > 127 && red < 127) cellsWithWindDirection[n-1][i][j] = E;
                    else if(green < 85 && blue > 127 && red < 127) cellsWithWindDirection[n-1][i][j] = SE;
                    else if(green < 85 && blue < 127 && red < 127) cellsWithWindDirection[n-1][i][j] = S;
                    else if(green < 85 && blue < 127 && red > 127) cellsWithWindDirection[n-1][i][j] = SW;
                    else if(green < 170 && green > 85 && blue < 127 && red > 127) cellsWithWindDirection[n-1][i][j] = W;
                    else cellsWithWindDirection[n-1][i][j] = NW;
                    }
                }
            } catch (Exception e){
                System.out.println("Something went wrong with the file: winddirection.jpg");
            }
        }





        cellsWithCurrentDirection = new Cell.directions[currentCount][][];
        for (int n = 1; n <= currentCount; n++) {
            cellsWithCurrentDirection[n - 1] = new Cell.directions[Board.HEIGHT][Board.WIDTH];

            try {
                File file = new File("currentdirection"+n+".jpg");
                BufferedImage image = ImageIO.read(file);

                for (int i = 0; i < Board.HEIGHT; i++) {
                    for (int j = 0; j < Board.WIDTH; j++) {
                        int pxl = image.getRGB(j, i);
                        int  red   = (pxl & 0x00ff0000) >> 16;
                        int  green = (pxl & 0x0000ff00) >> 8;
                        int  blue  =  pxl & 0x000000ff;
                        if(green > 170 && blue < 127 && red < 127) cellsWithCurrentDirection[n-1][i][j] = N;
                        else if(green > 170 && blue > 127 && red < 127) cellsWithCurrentDirection[n-1][i][j] = NE;
                        else if(green < 170 && green > 85 && blue > 127 && red < 127) cellsWithCurrentDirection[n-1][i][j] = E;
                        else if(green < 85 && blue > 127 && red < 127) cellsWithCurrentDirection[n-1][i][j] = SE;
                        else if(green < 85 && blue < 127 && red < 127) cellsWithCurrentDirection[n-1][i][j] = S;
                        else if(green < 85 && blue < 127 && red > 127) cellsWithCurrentDirection[n-1][i][j] = SW;
                        else if(green < 170 && green > 85 && blue < 127 && red > 127) cellsWithCurrentDirection[n-1][i][j] = W;
                        else cellsWithCurrentDirection[n-1][i][j] = NW;
                        //System.out.println(cellsWithWindDirection[n-1][i][j].name());
                    }
                    //System.out.println();
                }
            } catch (Exception e){
                System.out.println("Something went wrong with the file: currentdirection.jpg");
            }
        }
    }

}
