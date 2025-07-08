package com.mobagm.gui;

import com.mobagm.core.Enums.Region;
import com.mobagm.core.LeagueManager;
import com.mobagm.entities.Player;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

public class PlayersView {
    private static final LeagueManager lm = new LeagueManager();

    public static Node create() {
        VBox box = new VBox(10);
        box.setPadding(new Insets(15));
        ComboBox<Region> combo = new ComboBox<>(FXCollections.observableArrayList(Region.values()));
        TextArea area = new TextArea();
        area.setEditable(false);
        combo.setOnAction(e -> {
            Region r = combo.getValue();
            StringBuilder sb = new StringBuilder("Top Players in " + r + ":\n");
            for (Player p : lm.getTopPlayersByRegion(r, 10)) {
                sb.append(p.getName())
                        .append(" (").append(p.getRole())
                        .append(") OVR:").append(String.format("%.1f", p.getOverall()))
                        .append("\n");
            }
            area.setText(sb.toString());
        });
        box.getChildren().addAll(combo, area);
        return box;
    }
}
