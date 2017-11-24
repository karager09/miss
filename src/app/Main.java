package app;

import board.Board;

import cell.Cell;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }

    public static void print(Board board){
        for (Cell[] cTable:board.getCells()) {
            for (Cell c: cTable) {
                System.out.print("|"+c.getOilHeight());

            }
            System.out.println("|");
        }




    }


    public static void main(String[] args) {
        Board board = new Board();
        board = Rules.Rules.applyRules(board);
        //board = Rules.Rules.applyRules(board);
        print(board);
        //launch(args);
    }
}
