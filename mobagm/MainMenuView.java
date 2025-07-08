package com.mobagm.gui;

import com.mobagm.App;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class MainMenuView {
    private static final BorderPane root = new BorderPane();

    public static BorderPane getRoot() {
        return root;
    }

    public static BorderPane create() {
        root.setTop(createMenu());
        root.setCenter(new javafx.scene.control.Label("Welcome to MOBAGM!", javafx.geometry.Pos.CENTER));
        root.getStylesheets().add(MainMenuView.class.getResource("styles.css").toExternalForm());
        return root;
    }

    private static Node createMenu() {
        VBox menu = new VBox(10);
        menu.setPadding(new Insets(15));
        Button btnStandings = new Button("View Standings");
        btnStandings.setOnAction(e -> App.switchCenter(StandingsView.create()));
        Button btnPlayers   = new Button("View Top Players");
        btnPlayers.setOnAction(e -> App.switchCenter(PlayersView.create()));
        Button btnTeams     = new Button("View Teams");
        btnTeams.setOnAction(e -> App.switchCenter(TeamsView.create()));
        Button btnBack      = new Button("Main Menu");
        btnBack.setOnAction(e -> App.switchCenter(new javafx.scene.control.Label("Welcome to MOBAGM!", javafx.geometry.Pos.CENTER)));

        menu.getChildren().addAll(btnStandings, btnPlayers, btnTeams, btnBack);
        menu.getStyleClass().add("menu-box");
        return menu;
    }
}
