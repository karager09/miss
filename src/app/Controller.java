package app;

import Rules.Rules;
import board.Board;
import com.sun.javafx.css.Rule;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class Controller {

    Board board = Rules.applyRules(Rules.applyRules(Rules.applyRules(Rules.applyRules(Rules.applyRules(new Board())))));

    @FXML
    private Button rewind_btn;

    @FXML
    private Slider slider_rewind;

    @FXML
    private BorderPane main_window;

    @FXML
    private FlowPane flowPane;

    @FXML
    public void rewind(ActionEvent e){
        for (int i = 0; i < slider_rewind.getValue(); i++) {
            board = Rules.applyRules(board);
        }
        createBoard(board);

    }

    @FXML
    public void startClicked(ActionEvent e){
        //board = Rules.applyRules(board);
        createBoard(board);
    }

    @FXML
    public void nextState(ActionEvent e){
        board = Rules.applyRules(board);
        createBoard(board);

    }



    @FXML
    public void resizeWindow(ActionEvent e){
        createBoard(board);


    }


    public void createBoard(Board board) {

        //odpowiedizalne za odpowiednia wysokosc i szerokosc okna
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
        for (int i = 0; i < board.getHeight(); i++) {
            for (int j = 0; j < board.getWidth(); j++) {
                if(maxValue < board.getCells()[i][j].getOilHeight()) maxValue = board.getCells()[i][j].getOilHeight();
            }
        }

        int w = (int) (main_width / board.getWidth());
        int h = (int) (main_height / board.getHeight());

        for (int i = 0; i < board.getHeight(); i++) {
            for (int j = 0; j < board.getWidth(); j++) {
                Rectangle r = new Rectangle();
                r.setWidth(w);
                r.setHeight(h);

                if(board.getCells()[i][j].getOilHeight() < 0.05 * maxValue) r.setFill(Paint.valueOf("FFFFFF"));
                else if(board.getCells()[i][j].getOilHeight() < 0.10 * maxValue)r.setFill(Paint.valueOf("f2f2f2"));
                else if(board.getCells()[i][j].getOilHeight() < 0.15 * maxValue)r.setFill(Paint.valueOf("e6e6e6"));
                else if(board.getCells()[i][j].getOilHeight() < 0.20 * maxValue)r.setFill(Paint.valueOf("d9d9d9"));
                else if(board.getCells()[i][j].getOilHeight() < 0.25 * maxValue)r.setFill(Paint.valueOf("cccccc"));
                else if(board.getCells()[i][j].getOilHeight() < 0.30 * maxValue)r.setFill(Paint.valueOf("bfbfbf"));
                else if(board.getCells()[i][j].getOilHeight() < 0.35 * maxValue)r.setFill(Paint.valueOf("b3b3b3"));
                else if(board.getCells()[i][j].getOilHeight() < 0.40 * maxValue)r.setFill(Paint.valueOf("a6a6a6"));
                else if(board.getCells()[i][j].getOilHeight() < 0.45 * maxValue)r.setFill(Paint.valueOf("999999"));
                else if(board.getCells()[i][j].getOilHeight() < 0.50 * maxValue)r.setFill(Paint.valueOf("8c8c8c"));
                else if(board.getCells()[i][j].getOilHeight() < 0.55 * maxValue)r.setFill(Paint.valueOf("808080"));
                else if(board.getCells()[i][j].getOilHeight() < 0.60 * maxValue)r.setFill(Paint.valueOf("737373"));
                else if(board.getCells()[i][j].getOilHeight() < 0.65 * maxValue)r.setFill(Paint.valueOf("666666"));
                else if(board.getCells()[i][j].getOilHeight() < 0.70 * maxValue)r.setFill(Paint.valueOf("595959"));
                else if(board.getCells()[i][j].getOilHeight() < 0.75 * maxValue)r.setFill(Paint.valueOf("4d4d4d"));
                else if(board.getCells()[i][j].getOilHeight() < 0.80 * maxValue)r.setFill(Paint.valueOf("404040"));
                else if(board.getCells()[i][j].getOilHeight() < 0.85 * maxValue)r.setFill(Paint.valueOf("333333"));
                else if(board.getCells()[i][j].getOilHeight() < 0.90 * maxValue)r.setFill(Paint.valueOf("262626"));
                else if(board.getCells()[i][j].getOilHeight() < 0.95 * maxValue)r.setFill(Paint.valueOf("1a1a1a"));
                else r.setFill(Paint.valueOf("000000"));


                flowPane.getChildren().add(r);
            }
        }

    }


    @FXML
    void initialize(){

        main_window.widthProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
                //System.out.println("Width: " + newSceneWidth);
                createBoard(board);
            }
        });

        main_window.heightProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
                //System.out.println("Height: " + newSceneHeight);
                createBoard(board);
            }
        });

        createBoard(board);
    }

}
