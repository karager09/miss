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

    static BufferedWriter out; // plik z rezultatami
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

            for (int i = 0; i < Board.getHeight(); i++) {
                for (int j = 0; j < Board.getWidth(); j++) {
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

        timeline.getKeyFrames().setAll(new KeyFrame(Duration.millis(400 + slider_animation_speed.getValue() * 150),
                event -> {
                    for (int m = 0; m < slider_animation_speed.getValue(); m++) {
                        board = Rules.applyRules(board);
                        Rules.timePassed += Rules.timeForOneStep;


                        float amountOfOilSurface = 0, amountOfOilSubsurface = 0, amountOfOilShorline = 0, amountOfOilShorlineBelow = 0, maxValueSurface = 0, maxValueSubsurface = 0;
                        int area = 0;

                        for (int i = 0; i < Board.getHeight(); i++) {
                            for (int j = 0; j < Board.getWidth(); j++) {
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
        writeDataToFile(maxValueSurface, maxValueSubsurface, show_oil_surface_textfield.getText().substring(17), show_oil_subsurface_textfield.getText().substring(20),shorline_oil_textfield.getText().substring(19), shorline_deposition_textfield.getText().substring(14), area_textfield.getText().substring(13));
    }



    public void createBoard(Board board, int n) {// jak 0 to pokazuje powierzchnie


        //odpowiedzialne za odpowiednia wysokosc i szerokosc okna
        double main_height = ((int)((main_window.getHeight() - 70)/Board.getHeight())*Board.getHeight());
        double main_width = ((int)((main_window.getWidth() - 5)/Board.getWidth())*Board.getWidth());

        //zeby kwadrat
        if(main_height > main_width) main_height = main_width;
        else main_width = main_height;

        //zeby mozna bylo skalowac
        tilePane.setMaxWidth(main_width);
        tilePane.setPrefWidth(main_width);
        tilePane.setMinWidth(main_width);

        //max wartosc slupa oleju
        float maxValue = 0;
        maxValueSurface = 0; maxValueSubsurface = 0;

        //do wcześniejszego, współbieżnego kodu
        /*if(n == 0){
            maxValue = board.getMaxValueSurface();
        }
        else {
            maxValue = board.getMaxValueSubsurface();
        }*/

        float amountOfOilSurface = 0, amountOfOilSubsurface = 0, amountOfOilShorline = 0, amountOfOilShorlineBelow = 0;
        int area = 0;

        // obliczanie statystyk
        for (int i = 0; i < Board.getHeight(); i++) {
            for (int j = 0; j < Board.getWidth(); j++) {
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

        //wypisywanie statystyk
        if(n == 0) maxValue = maxValueSurface; else maxValue = maxValueSubsurface;
        show_oil_surface_textfield.setText(String.format("Oil surface (b): %.2f",amountOfOilSurface));
        show_oil_surface_textfield.setPrefWidth(show_oil_surface_textfield.getText().length() * 7 + 15);

        show_oil_subsurface_textfield.setText(String.format("Oil subsurface (b): %.2f",amountOfOilSubsurface));
        show_oil_subsurface_textfield.setPrefWidth(show_oil_subsurface_textfield.getText().length() * 7 + 15);

        shorline_oil_textfield.setText(String.format("Near shorline (b): %.2f", amountOfOilShorline));
        shorline_oil_textfield.setPrefWidth(shorline_oil_textfield.getText().length() * 7 + 15);

        shorline_deposition_textfield.setText(String.format("Shorline (b): %.2f",amountOfOilShorlineBelow));
        shorline_deposition_textfield.setPrefWidth(shorline_deposition_textfield.getText().length() * 7 + 15);

        show_time_textfield.setText(String.format("Time (h): %.2f",Rules.timePassed));
        show_time_textfield.setPrefWidth(show_time_textfield.getText().length() * 7 + 20);

        show_max_value_textfield.setText(String.format("Max (b): %.2f",maxValue));
        show_max_value_textfield.setPrefWidth(show_max_value_textfield.getText().length() * 7 + 25);

        area_textfield.setText(String.format("Area (km^2): %.1f", area * Rules.lengthOfCellSide * Rules.lengthOfCellSide / 1000000));
        area_textfield.setPrefWidth(area_textfield.getText().length() * 7 + 20);


        int w = (int) (main_width / Board.getWidth());
        int h = (int) (main_height / Board.getHeight());

        if (((Rectangle)tilePane.getChildren().get(0)).getWidth() != w){
            for (int i = 0; i < Board.getHeight(); i++) {
                for (int j = 0; j < Board.getWidth(); j++) {
                    Rectangle r = (Rectangle)tilePane.getChildren().get(i*Board.getHeight() + j);
                    r.setWidth(w);
                    r.setHeight(h);
                }
            }
        }


        for (int i = 0; i < Board.getHeight(); i++) {
            for (int j = 0; j < Board.getWidth(); j++) {
                Rectangle r = (Rectangle)tilePane.getChildren().get(i*Board.getHeight() + j);


                float oilVolume;
                if(n == 0) oilVolume = board.getCells()[i][j].getOilHeight(); else oilVolume = board.getCells()[i][j].getOilBelowSurface();

                //wyswietlanie ropy w odpowiednich kolorach szarosci
                if(maxValue == 0) maxValue=1;// zeby jak sa same zera wyswietlalo jako morze
                if(board.getCells()[i][j].isLand())  r.setFill(Paint.valueOf("00ff00"));
                else if(board.getCells()[i][j].isBeach()) r.setFill(Paint.valueOf("ffff00"));
                else {
                    String hex;
                    int  value = (int) Math.ceil(255 * (1 - (oilVolume / maxValue)));
                        if (value > 15) hex = Integer.toHexString(value);
                            else { hex = "0"+Integer.toHexString(value);}
                    r.setFill(Paint.valueOf(hex + hex+ hex));
                }

            }
        }



    }


    @FXML
    void initialize(){

        try {
            out = new BufferedWriter(Files.newBufferedWriter(Paths.get("results.txt")));
            out.write("\"lp\" \"time\" \"maxValueSurface\" \"maxValueSubsurface\" \"amountOfOilSurface\" \"amountOfOilSubsurface\" \"oilNearShorline\" \"shorlineDeposition\" \"area\"");

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

        tilePane.setPrefColumns(Board.getWidth());
        tilePane.setPrefRows(Board.getHeight());

        for (int i = 0; i < Board.getHeight(); i++) {
            for (int j = 0; j < Board.getWidth(); j++) {
                tilePane.getChildren().add(new Rectangle());
            }
        }

        createBoard(board,getWhatToShow());

    }

}
