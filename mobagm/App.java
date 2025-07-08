package com.mobagm;

import com.mobagm.gui.MainMenuView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    private static Stage primaryStage;

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        stage.setTitle("MOBAGM – MOBA General Manager");
        stage.setScene(new Scene(MainMenuView.create(), 1024, 768));
        stage.show();
    }

    public static void switchCenter(javafx.scene.Node node) {
        MainMenuView.getRoot().setCenter(node);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
