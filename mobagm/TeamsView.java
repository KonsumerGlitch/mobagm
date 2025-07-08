package com.mobagm.gui;

import com.mobagm.core.Enums.Region;
import com.mobagm.core.LeagueManager;
import com.mobagm.entities.Team;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

public class TeamsView {
    private static final LeagueManager lm = new LeagueManager();

    public static Node create() {
        VBox box = new VBox(10);
        box.setPadding(new Insets(15));
        ComboBox<Region> combo = new ComboBox<>(FXCollections.observableArrayList(Region.values()));
        TextArea area = new TextArea();
        area.setEditable(false);
        combo.setOnAction(e -> {
            Region r = combo.getValue();
            StringBuilder sb = new StringBuilder("Champions Series in " + r + ":\n");
            for (Team t : lm.getRegionalLeagues().get(r).getChampionsTeams()) {
                sb.append(t.getName())
                        .append(" Strength:")
                        .append(String.format("%.1f", t.getTeamStrength()))
                        .append("\n");
            }
            area.setText(sb.toString());
        });
        box.getChildren().addAll(combo, area);
        return box;
    }
}
