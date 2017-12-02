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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class Controller {


    Board board = new Board(1);

    final Timeline timeline = new Timeline();

    @FXML
    private Button rewind_btn, start_btn, next_state_btn;

    @FXML
    private TextField show_max_value_textfield;

    @FXML
    private Slider slider_rewind, slider_animation_speed;

    @FXML
    private BorderPane main_window;

    @FXML
    private FlowPane flowPane;

    @FXML
    public void rewind(ActionEvent e){
        for (int i = 0; i < slider_rewind.getValue(); i++) {
            board = Rules.applyRules(board);
        }
        createBoard(board,1);

    }

    @FXML
    public void setAnimationSpeed(MouseEvent e){

        timeline.stop();
//        timeline.getKeyFrames().setAll(new KeyFrame(Duration.millis(10000/slider_animation_speed.getValue()),
//                event -> nextState(null)));

        timeline.getKeyFrames().setAll(new KeyFrame(Duration.millis(500*slider_animation_speed.getValue()),
                event -> nextState(null)));

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
        createBoard(board,1);

    }



    public void createBoard(Board board, int n) {// jak 0 to pokazuje powierzchnie

        //odpowiedzialne za odpowiednia wysokosc i szerokosc okna
        double main_height = ((int)((main_window.getHeight() - 50)/board.getHeight())*board.getHeight());
        double main_width = ((int)((main_window.getWidth() - 5)/board.getWidth())*board.getWidth());

        //zeby kwadrat
        if(main_height > main_width) main_height = main_width;
        else main_width = main_height;

        flowPane.getChildren().clear();
        flowPane.setMaxWidth(main_width);
        flowPane.setPrefWidth(main_width);
        flowPane.setMinWidth(main_width);

        //max wartosc slupa oleju
        float maxValue = 0;
        if(n == 0){
            for (int i = 0; i < board.getHeight(); i++) {
                for (int j = 0; j < board.getWidth(); j++) {
                    if(maxValue < board.getCells()[i][j].getOilHeight()) maxValue = board.getCells()[i][j].getOilHeight();
                }
            }
        }
        else {
            for (int i = 0; i < board.getHeight(); i++) {
                for (int j = 0; j < board.getWidth(); j++) {
                    if (maxValue < board.getCells()[i][j].getSubsurfaceOil())
                        maxValue = board.getCells()[i][j].getSubsurfaceOil();
                }
            }
        }




        show_max_value_textfield.setText(String.format("Max: %.2f",maxValue));

        int w = (int) (main_width / board.getWidth());
        int h = (int) (main_height / board.getHeight());


        for (int i = 0; i < board.getHeight(); i++) {
            for (int j = 0; j < board.getWidth(); j++) {
                Rectangle r = new Rectangle();
                r.setWidth(w);
                r.setHeight(h);


                float oilVolume;
                if(n == 0) oilVolume = board.getCells()[i][j].getOilHeight(); else oilVolume = board.getCells()[i][j].getSubsurfaceOil();


                if(board.getCells()[i][j].isLand())  r.setFill(Paint.valueOf("00ff00"));
                else if(board.getCells()[i][j].isBeach()) r.setFill(Paint.valueOf("ffff00"));
                else if(oilVolume < 0.05 * maxValue) r.setFill(Paint.valueOf("ffffff"));
                else if(oilVolume < 0.10 * maxValue)r.setFill(Paint.valueOf("f2f2f2"));
                else if(oilVolume < 0.15 * maxValue)r.setFill(Paint.valueOf("e6e6e6"));
                else if(oilVolume < 0.20 * maxValue)r.setFill(Paint.valueOf("d9d9d9"));
                else if(oilVolume < 0.25 * maxValue)r.setFill(Paint.valueOf("cccccc"));
                else if(oilVolume < 0.30 * maxValue)r.setFill(Paint.valueOf("bfbfbf"));
                else if(oilVolume < 0.35 * maxValue)r.setFill(Paint.valueOf("b3b3b3"));
                else if(oilVolume < 0.40 * maxValue)r.setFill(Paint.valueOf("a6a6a6"));
                else if(oilVolume < 0.45 * maxValue)r.setFill(Paint.valueOf("999999"));
                else if(oilVolume < 0.50 * maxValue)r.setFill(Paint.valueOf("8c8c8c"));
                else if(oilVolume < 0.55 * maxValue)r.setFill(Paint.valueOf("808080"));
                else if(oilVolume < 0.60 * maxValue)r.setFill(Paint.valueOf("737373"));
                else if(oilVolume < 0.65 * maxValue)r.setFill(Paint.valueOf("666666"));
                else if(oilVolume < 0.70 * maxValue)r.setFill(Paint.valueOf("595959"));
                else if(oilVolume < 0.75 * maxValue)r.setFill(Paint.valueOf("4d4d4d"));
                else if(oilVolume < 0.80 * maxValue)r.setFill(Paint.valueOf("404040"));
                else if(oilVolume < 0.85 * maxValue)r.setFill(Paint.valueOf("333333"));
                else if(oilVolume < 0.90 * maxValue)r.setFill(Paint.valueOf("262626"));
                else if(oilVolume < 0.95 * maxValue)r.setFill(Paint.valueOf("1a1a1a"));
                else r.setFill(Paint.valueOf("000000"));


                flowPane.getChildren().add(r);
            }
        }

    }


    @FXML
    void initialize(){
        main_window.widthProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
                createBoard(board,1);
            }
        });

        main_window.heightProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
                createBoard(board, 1);
            }
        });

        timeline.setCycleCount(Animation.INDEFINITE);
        setAnimationSpeed(null);


        createBoard(board,1);


    }

}
