package app;

import Rules.Rules;
import board.Board;

import board.BoardFromFile;
import cell.Cell;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        BoardFromFile.getAllCellsFromFile();


        FXMLLoader loader = new FXMLLoader();
        //Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        loader.setLocation(this.getClass().getResource("appearance.fxml"));

        Pane pane = loader.load();
        Scene scene = new Scene(pane);

        primaryStage.setOnCloseRequest(e -> {

            Platform.exit();

            try {
                Controller.out.close();
            } catch (IOException e1) {
                System.out.println("Can't close file");
                e1.printStackTrace();
            }});

        primaryStage.setTitle("I MISS U");
        primaryStage.setScene(scene);
        primaryStage.show();

    }



    public static void main(String[] args) {
        launch(args);
    }
}
