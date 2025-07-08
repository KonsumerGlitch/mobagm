// GameUI.java
package com.mobagm.ui;

import com.mobagm.MOBAGMApplication;
import com.mobagm.core.Enums.Region;
import com.mobagm.entities.Player;
import com.mobagm.entities.Team;

public class GameUI {
    private MOBAGMApplication app;

    public GameUI(MOBAGMApplication app) {
        this.app = app;
    }

    public void displayLiveMatch(String team1, String team2) {
        System.out.println("\n🔴 LIVE: " + team1 + " vs " + team2);
        System.out.println("Match in progress...");
    }

    public void displayMatchResult(String winner, String loser, String score) {
        System.out.println("\n✅ RESULT: " + winner + " defeats " + loser + " (" + score + ")");
    }

    public void displayPlayerTransfer(String playerName, String fromTeam, String toTeam) {
        System.out.println("\n💼 TRANSFER: " + playerName + " moves from " + fromTeam + " to " + toTeam);
    }

    public void displayTournamentUpdate(String tournament, String update) {
        System.out.println("\n🏆 " + tournament + ": " + update);
    }

    public void displayProgressBar(String task, int progress, int total) {
        int barLength = 30;
        int filled = (int) ((double) progress / total * barLength);

        StringBuilder bar = new StringBuilder();
        bar.append("[");
        for (int i = 0; i < barLength; i++) {
            if (i < filled) {
                bar.append("=");
            } else {
                bar.append(" ");
            }
        }
        bar.append("]");

        System.out.printf("\r%s %s %d/%d", task, bar.toString(), progress, total);

        if (progress == total) {
            System.out.println(" ✅ Complete!");
        }
    }

    public void displaySeasonSummary(int year, String split) {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("          " + split.toUpperCase() + " " + year + " SUMMARY");
        System.out.println("=".repeat(50));
    }

    public void displayError(String message) {
        System.out.println("\n❌ ERROR: " + message);
    }

    public void displaySuccess(String message) {
        System.out.println("\n✅ SUCCESS: " + message);
    }

    public void displayWarning(String message) {
        System.out.println("\n⚠️ WARNING: " + message);
    }

    public void displayInfo(String message) {
        System.out.println("\nℹ️ INFO: " + message);
    }

    public void clearScreen() {
        System.out.print("\033[2J\033[H");
        System.out.flush();
    }

    public void waitForEnter() {
        System.out.println("\nPress Enter to continue...");
        try {
            System.in.read();
        } catch (Exception e) {
            // Ignore
        }
    }
}
