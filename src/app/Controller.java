package app;

import Rules.Rules;
import board.Board;
import board.BoardFromFile;
import cell.Cell;
import com.sun.javafx.css.Rule;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static java.lang.System.exit;


public class Controller {

    static BufferedWriter out;
    private int lp = 0;
    private float  maxValueSurface = 0, maxValueSubsurface = 0;

    public static Board board = new Board();
    public static Board board_tmp = new Board();


    private final Timeline timeline = new Timeline();

    @FXML
    private Button rewind_btn, start_btn, next_state_btn;

    @FXML
    private TextField show_max_value_textfield, show_time_textfield, show_oil_subsurface_textfield, show_oil_surface_textfield, shorline_oil_textfield,shorline_deposition_textfield, area_textfield;

    @FXML
    private Slider slider_rewind, slider_animation_speed;

    @FXML
    private BorderPane main_window;

    //@FXML
    //private FlowPane flowPane;

    @FXML
    private TilePane tilePane;

    @FXML
    private Button whatToShow;


    private void writeDataToFile(float maxValueSurface, float maxValueSubsurface, String amountOfOilSurface, String amountOfOilSubsurface, String amountOfOilShorline, String amountOfOilShorlineBelow, String area){
        ++lp;
        try {
            out.newLine();
            out.write(lp+" "+ String.format("%f",Rules.timePassed) + " "+ String.format("%f",maxValueSurface) +" " + String.format("%f",maxValueSubsurface) + " " + amountOfOilSurface+" " + amountOfOilSubsurface+" "+amountOfOilShorline + " "+amountOfOilShorlineBelow + " "+area);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @FXML
    public void setWhatToShow(ActionEvent e){
        if(whatToShow.getText().equals("Subsurface")){
            whatToShow.setText("Surface");
            createBoard(board,1);}
        else {
            whatToShow.setText("Subsurface");
            createBoard(board,0);
        }
    }


    private int getWhatToShow() {
        if(whatToShow.getText().equals("Subsurface")) return 0;
        return 1;
    }

    @FXML
    public void rewind(ActionEvent e){
        for (int m = 0; m < slider_rewind.getValue(); m++) {

            board = Rules.applyRules(board);
            Rules.timePassed += Rules.timeForOneStep;


            float amountOfOilSurface = 0, amountOfOilSubsurface = 0, amountOfOilShorline = 0, amountOfOilShorlineBelow = 0, maxValueSurface = 0, maxValueSubsurface = 0;
            int area = 0;

            for (int i = 0; i < board.getHeight(); i++) {
                for (int j = 0; j < board.getWidth(); j++) {
                    Cell cell = board.getCells()[i][j];

                    if(maxValueSurface < cell.getOilHeight()) maxValueSurface = cell.getOilHeight();
                    if( !cell.isLand() && !cell.isBeach() && maxValueSubsurface < cell.getOilBelowSurface()) maxValueSubsurface = cell.getOilBelowSurface();

                    if(!cell.isLand() && !cell.isBeach()) amountOfOilSurface += cell.getOilHeight();
                    if(!cell.isLand() && !cell.isBeach()) amountOfOilSubsurface += cell.getOilBelowSurface();
                    if(cell.isLand() || cell.isBeach()) amountOfOilShorline += cell.getOilHeight();
                    if(cell.isLand() || cell.isBeach()) amountOfOilShorlineBelow += cell.getOilBelowSurface();
                    if(cell.getOilHeight() != 0) ++area;

                }
            }

            writeDataToFile(maxValueSurface, maxValueSubsurface, String.format("%f",amountOfOilSurface), String.format("%f",amountOfOilSubsurface), String.format("%f",amountOfOilShorline), String.format("%f",amountOfOilShorlineBelow), String.format("%f",area * Rules.lengthOfCellSide * Rules.lengthOfCellSide / 1000000));
        }
        createBoard(board,getWhatToShow());

    }

    @FXML
    public void setAnimationSpeed(MouseEvent e){

        timeline.stop();
//        timeline.getKeyFrames().setAll(new KeyFrame(Duration.millis(10000/slider_animation_speed.getValue()),
//                event -> nextState(null)));

//        timeline.getKeyFrames().setAll(new KeyFrame(Duration.millis(500*slider_animation_speed.getValue()),
//                event -> nextState(null)));



        timeline.getKeyFrames().setAll(new KeyFrame(Duration.millis(300 + slider_animation_speed.getValue() * 30),
                event -> {
                    for (int m = 0; m < slider_animation_speed.getValue(); m++) {
                        board = Rules.applyRules(board);
                        Rules.timePassed += Rules.timeForOneStep;


                        float amountOfOilSurface = 0, amountOfOilSubsurface = 0, amountOfOilShorline = 0, amountOfOilShorlineBelow = 0, maxValueSurface = 0, maxValueSubsurface = 0;
                        int area = 0;

                        for (int i = 0; i < board.getHeight(); i++) {
                            for (int j = 0; j < board.getWidth(); j++) {
                                Cell cell = board.getCells()[i][j];

                                if(maxValueSurface < cell.getOilHeight()) maxValueSurface = cell.getOilHeight();
                                if( !cell.isLand() && !cell.isBeach() && maxValueSubsurface < cell.getOilBelowSurface()) maxValueSubsurface = cell.getOilBelowSurface();

                                if(!cell.isLand() && !cell.isBeach()) amountOfOilSurface += cell.getOilHeight();
                                if(!cell.isLand() && !cell.isBeach()) amountOfOilSubsurface += cell.getOilBelowSurface();
                                if(cell.isLand() || cell.isBeach()) amountOfOilShorline += cell.getOilHeight();
                                if(cell.isLand() || cell.isBeach()) amountOfOilShorlineBelow += cell.getOilBelowSurface();
                                if(cell.getOilHeight() != 0) ++area;

                            }
                        }

                        writeDataToFile(maxValueSurface, maxValueSubsurface, String.format("%f",amountOfOilSurface), String.format("%f",amountOfOilSubsurface), String.format("%f",amountOfOilShorline), String.format("%f",amountOfOilShorlineBelow), String.format("%f",area * Rules.lengthOfCellSide * Rules.lengthOfCellSide / 1000000));
                    }
                    createBoard(board,getWhatToShow());
                }));


        start_btn.setText("Start");
    }

    @FXML
    public void startClicked(ActionEvent e){
        if(start_btn.getText().equals("Start")){
        timeline.play();
        start_btn.setText("Stop");}
        else {
            timeline.pause();
            start_btn.setText("Start");

        }
    }

    @FXML
    public void nextState(ActionEvent e){
        board = Rules.applyRules(board);
        Rules.timePassed += Rules.timeForOneStep;
        createBoard(board,getWhatToShow());
        writeDataToFile(maxValueSurface, maxValueSubsurface, show_oil_surface_textfield.getText().substring(17), show_oil_subsurface_textfield.getText().substring(20),shorline_oil_textfield.getText().substring(14), shorline_deposition_textfield.getText().substring(19), area_textfield.getText().substring(13));
    }



    public void createBoard(Board board, int n) {// jak 0 to pokazuje powierzchnie


        //odpowiedzialne za odpowiednia wysokosc i szerokosc okna
        double main_height = ((int)((main_window.getHeight() - 70)/board.getHeight())*board.getHeight());
        double main_width = ((int)((main_window.getWidth() - 5)/board.getWidth())*board.getWidth());

        //zeby kwadrat
        if(main_height > main_width) main_height = main_width;
        else main_width = main_height;

//        flowPane.getChildren().clear();
//        flowPane.setMaxWidth(main_width);
//        flowPane.setPrefWidth(main_width);
//        flowPane.setMinWidth(main_width);

        tilePane.setMaxWidth(main_width);
        tilePane.setPrefWidth(main_width);
        tilePane.setMinWidth(main_width);

        //max wartosc slupa oleju
        float maxValue = 0;
        maxValueSurface = 0; maxValueSubsurface = 0;
        /*if(n == 0){
            maxValue = board.getMaxValueSurface();
        }
        else {
            maxValue = board.getMaxValueSubsurface();
        }*/

        float amountOfOilSurface = 0, amountOfOilSubsurface = 0, amountOfOilShorline = 0, amountOfOilShorlineBelow = 0;
        int area = 0;

        for (int i = 0; i < board.getHeight(); i++) {
            for (int j = 0; j < board.getWidth(); j++) {
                Cell cell = board.getCells()[i][j];

                if( maxValueSurface < cell.getOilHeight()) maxValueSurface = cell.getOilHeight();
                if( !cell.isLand() && !cell.isBeach() && maxValueSubsurface < cell.getOilBelowSurface()) maxValueSubsurface = cell.getOilBelowSurface();

                if(!cell.isLand() && !cell.isBeach()) amountOfOilSurface += cell.getOilHeight();
                if(!cell.isLand() && !cell.isBeach()) amountOfOilSubsurface += cell.getOilBelowSurface();
                if(cell.isLand() || cell.isBeach()) amountOfOilShorline += cell.getOilHeight();
                if(cell.isLand() || cell.isBeach()) amountOfOilShorlineBelow += cell.getOilBelowSurface();
                if(cell.getOilHeight() != 0) ++area;

            }
        }

        if(n == 0) maxValue = maxValueSurface; else maxValue = maxValueSubsurface;
        show_oil_surface_textfield.setText(String.format("Oil surface (b): %.2f",amountOfOilSurface));
        show_oil_surface_textfield.setPrefWidth(show_oil_surface_textfield.getText().length() * 7 + 15);

        show_oil_subsurface_textfield.setText(String.format("Oil subsurface (b): %.2f",amountOfOilSubsurface));
        show_oil_subsurface_textfield.setPrefWidth(show_oil_subsurface_textfield.getText().length() * 7 + 15);

        shorline_oil_textfield.setText(String.format("Shorline (b): %.2f", amountOfOilShorline));
        shorline_oil_textfield.setPrefWidth(shorline_oil_textfield.getText().length() * 7 + 15);

        shorline_deposition_textfield.setText(String.format("Shorline below(b): %.2f",amountOfOilShorlineBelow));
        shorline_deposition_textfield.setPrefWidth(shorline_deposition_textfield.getText().length() * 7 + 15);

        show_time_textfield.setText(String.format("Time (h): %.2f",Rules.timePassed));
        show_time_textfield.setPrefWidth(show_time_textfield.getText().length() * 7 + 20);

        show_max_value_textfield.setText(String.format("Max (b): %.2f",maxValue));
        show_max_value_textfield.setPrefWidth(show_max_value_textfield.getText().length() * 7 + 25);

        area_textfield.setText(String.format("Area (km^2): %.1f", area * Rules.lengthOfCellSide * Rules.lengthOfCellSide / 1000000));
        area_textfield.setPrefWidth(area_textfield.getText().length() * 7 + 20);


        int w = (int) (main_width / board.getWidth());
        int h = (int) (main_height / board.getHeight());

        if (((Rectangle)tilePane.getChildren().get(0)).getWidth() != w){
            for (int i = 0; i < board.getHeight(); i++) {
                for (int j = 0; j < board.getWidth(); j++) {
                    Rectangle r = (Rectangle)tilePane.getChildren().get(i*Board.HEIGHT + j);
                    r.setWidth(w);
                    r.setHeight(h);
                }
            }
        }


        for (int i = 0; i < board.getHeight(); i++) {
            for (int j = 0; j < board.getWidth(); j++) {
                Rectangle r = (Rectangle)tilePane.getChildren().get(i*Board.HEIGHT + j);


                float oilVolume;
                if(n == 0) oilVolume = board.getCells()[i][j].getOilHeight(); else oilVolume = board.getCells()[i][j].getOilBelowSurface();

                if(maxValue == 0) maxValue=1;// zeby jak sa same zera wyswietlalo jako morze
                if(board.getCells()[i][j].isLand())  r.setFill(Paint.valueOf("00ff00"));
                else if(board.getCells()[i][j].isBeach()) r.setFill(Paint.valueOf("ffff00"));
                else {
                    String hex;
                    int  value = (int) (255 * (1 - (oilVolume / maxValue)));
                        if (value > 15) hex = Integer.toHexString(value);
                            else { hex = "0"+Integer.toHexString(value);}
                    r.setFill(Paint.valueOf(hex + hex+ hex));
                }
                /*else if(oilVolume == 0) r.setFill(Paint.valueOf("ffffff"));
                else if(oilVolume < 0.008 * maxValue) r.setFill(Paint.valueOf("fefefe"));
                else if(oilVolume < 0.016 * maxValue) r.setFill(Paint.valueOf("fdfdfd"));
                else if(oilVolume < 0.025 * maxValue) r.setFill(Paint.valueOf("fcfcfc"));
                else if(oilVolume < 0.05 * maxValue) r.setFill(Paint.valueOf("f9f9f9"));
                else if(oilVolume < 0.075 * maxValue) r.setFill(Paint.valueOf("f5f5f5"));
                else if(oilVolume < 0.10 * maxValue)r.setFill(Paint.valueOf("f2f2f2"));
                else if(oilVolume < 0.125 * maxValue)r.setFill(Paint.valueOf("ececec"));
                else if(oilVolume < 0.15 * maxValue)r.setFill(Paint.valueOf("e6e6e6"));
                else if(oilVolume < 0.175 * maxValue)r.setFill(Paint.valueOf("e0e0e0"));
                else if(oilVolume < 0.20 * maxValue)r.setFill(Paint.valueOf("d9d9d9"));
                else if(oilVolume < 0.225 * maxValue)r.setFill(Paint.valueOf("d2d2d2"));
                else if(oilVolume < 0.25 * maxValue)r.setFill(Paint.valueOf("cccccc"));
                else if(oilVolume < 0.275 * maxValue)r.setFill(Paint.valueOf("c5c5c5"));
                else if(oilVolume < 0.30 * maxValue)r.setFill(Paint.valueOf("bfbfbf"));
                else if(oilVolume < 0.325 * maxValue)r.setFill(Paint.valueOf("b9b9b9"));
                else if(oilVolume < 0.35 * maxValue)r.setFill(Paint.valueOf("b3b3b3"));
                else if(oilVolume < 0.375 * maxValue)r.setFill(Paint.valueOf("acacac"));
                else if(oilVolume < 0.40 * maxValue)r.setFill(Paint.valueOf("a6a6a6"));
                else if(oilVolume < 0.425 * maxValue)r.setFill(Paint.valueOf("a0a0a0"));
                else if(oilVolume < 0.45 * maxValue)r.setFill(Paint.valueOf("999999"));
                else if(oilVolume < 0.475 * maxValue)r.setFill(Paint.valueOf("929292"));
                else if(oilVolume < 0.50 * maxValue)r.setFill(Paint.valueOf("8c8c8c"));
                else if(oilVolume < 0.525 * maxValue)r.setFill(Paint.valueOf("868686"));
                else if(oilVolume < 0.55 * maxValue)r.setFill(Paint.valueOf("808080"));
                else if(oilVolume < 0.575 * maxValue)r.setFill(Paint.valueOf("797979"));
                else if(oilVolume < 0.60 * maxValue)r.setFill(Paint.valueOf("737373"));
                else if(oilVolume < 0.635 * maxValue)r.setFill(Paint.valueOf("6c6c6c"));
                else if(oilVolume < 0.65 * maxValue)r.setFill(Paint.valueOf("666666"));
                else if(oilVolume < 0.675 * maxValue)r.setFill(Paint.valueOf("606060"));
                else if(oilVolume < 0.70 * maxValue)r.setFill(Paint.valueOf("595959"));
                else if(oilVolume < 0.725 * maxValue)r.setFill(Paint.valueOf("535353"));
                else if(oilVolume < 0.75 * maxValue)r.setFill(Paint.valueOf("4d4d4d"));
                else if(oilVolume < 0.775 * maxValue)r.setFill(Paint.valueOf("464646"));
                else if(oilVolume < 0.80 * maxValue)r.setFill(Paint.valueOf("404040"));
                else if(oilVolume < 0.825 * maxValue)r.setFill(Paint.valueOf("393939"));
                else if(oilVolume < 0.85 * maxValue)r.setFill(Paint.valueOf("333333"));
                else if(oilVolume < 0.875 * maxValue)r.setFill(Paint.valueOf("2c2c2c"));
                else if(oilVolume < 0.90 * maxValue)r.setFill(Paint.valueOf("262626"));
                else if(oilVolume < 0.925 * maxValue)r.setFill(Paint.valueOf("202020"));
                else if(oilVolume < 0.95 * maxValue)r.setFill(Paint.valueOf("1a1a1a"));
                else if(oilVolume < 0.975 * maxValue)r.setFill(Paint.valueOf("0d0d0d"));
                else r.setFill(Paint.valueOf("000000"));*/


                //tilePane.getChildren().add(r);

            }
        }



    }


    @FXML
    void initialize(){

        try {
            out = new BufferedWriter(Files.newBufferedWriter(Paths.get("results.txt")));
            out.write("\"lp\" \"time\" \"maxValueSurface\" \"maxValueSubsurface\" \"amountOfOilSurface\" \"amountOfOilSubsurface\" \"amountOfOilShorline\" \"shorlineDeposition\" \"area\"");

        } catch (Exception e){
            System.out.println("Something went wrong with data.txt");
            e.printStackTrace();
            exit(1);
        }
        main_window.widthProperty().addListener((ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) -> {

                createBoard(board,getWhatToShow());

        });

        main_window.heightProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
                createBoard(board, getWhatToShow());
            }
        });

        timeline.setCycleCount(Animation.INDEFINITE);
        setAnimationSpeed(null);

        tilePane.setPrefColumns(Board.WIDTH);
        tilePane.setPrefRows(Board.HEIGHT);

        for (int i = 0; i < Board.HEIGHT; i++) {
            for (int j = 0; j < Board.WIDTH; j++) {
                tilePane.getChildren().add(new Rectangle());
            }
        }

        createBoard(board,getWhatToShow());

    }

}
