package app;

import Rules.Rules;
import board.Board;

import board.BoardFromFile;
import cell.Cell;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        BoardFromFile.getAllCellsFromFile();


        FXMLLoader loader = new FXMLLoader();
        //Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        loader.setLocation(this.getClass().getResource("appearance.fxml"));

        Pane pane = loader.load();
        Scene scene = new Scene(pane);

        primaryStage.setTitle("I MISS U");
        primaryStage.setScene(scene);
        //primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();

    }



    public static void main(String[] args) {
        launch(args);
    }
}
