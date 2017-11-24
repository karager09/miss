package app;

import Rules.Rules;
import board.Board;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class Controller {

    @FXML
    private FlowPane flowPane;

    public void startClicked(ActionEvent e){
        //createBoard(new Board());

    }

    public void createBoard(Board board) {

        flowPane.getChildren().clear();
        flowPane.setMaxWidth(600);
        flowPane.setPrefWidth(600);
        flowPane.setMinWidth(600);

        float maxValue = 0;
        for (int i = 0; i < board.getHeight(); i++) {
            for (int j = 0; j < board.getWidth(); j++) {
                if(maxValue < board.getCells()[i][j].getOilHeight()) maxValue = board.getCells()[i][j].getOilHeight();
            }
        }

        double px_height = flowPane.getHeight();
        double px_width = flowPane.getWidth();
        System.out.println(px_height+", "+ px_width);
        double w = (600 / board.getWidth()) - 4;

        if ((board.getWidth() * (w + 4) + w) < 600) {
            flowPane.setMaxWidth(board.getWidth() * (w + 4));
            flowPane.setPrefWidth(board.getWidth() * (w + 4));
            flowPane.setMinWidth(board.getWidth() * (w + 4));
        }
        double h = (600 / board.getHeight()) - 4;

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


//        Rectangle r = new Rectangle();
//        r.setWidth(100);
//        r.setHeight(100);
//        r.setFill(Paint.valueOf("000000"));
//        flowPane.getChildren().add(r);
    }


    @FXML
    void initialize(){
        createBoard(Rules.applyRules(Rules.applyRules(Rules.applyRules(new Board()))));
    }

}
