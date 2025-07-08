// TeamStatistics.java
package com.mobagm.entities;

import java.util.*;

public class TeamStatistics {
    private int gamesPlayed;
    private int wins;
    private int losses;
    private Map<String, Integer> splitRecords;
    private int championshipsWon;
    private int internationalAppearances;
    private double averageGameTime;
    private int totalKills;
    private int totalDeaths;
    private double averageKDA;
    private Map<String, Double> performanceMetrics;

    public TeamStatistics() {
        this.gamesPlayed = 0;
        this.wins = 0;
        this.losses = 0;
        this.splitRecords = new HashMap<>();
        this.championshipsWon = 0;
        this.internationalAppearances = 0;
        this.averageGameTime = 0.0;
        this.totalKills = 0;
        this.totalDeaths = 0;
        this.averageKDA = 0.0;
        this.performanceMetrics = new HashMap<>();

        initializeMetrics();
    }

    private void initializeMetrics() {
        performanceMetrics.put("earlyGameWinRate", 0.0);
        performanceMetrics.put("lateGameWinRate", 0.0);
        performanceMetrics.put("baronControl", 0.0);
        performanceMetrics.put("dragonControl", 0.0);
        performanceMetrics.put("firstBloodRate", 0.0);
        performanceMetrics.put("averageVisionScore", 0.0);
    }

    public void updateGameResult(boolean won, double gameTime, int kills, int deaths) {
        gamesPlayed++;
        if (won) {
            wins++;
        } else {
            losses++;
        }

        averageGameTime = ((averageGameTime * (gamesPlayed - 1)) + gameTime) / gamesPlayed;
        totalKills += kills;
        totalDeaths += deaths;

        updateKDA();
    }

    private void updateKDA() {
        if (totalDeaths == 0) {
            averageKDA = totalKills;
        } else {
            averageKDA = (double) totalKills / totalDeaths;
        }
    }

    public void updateSplitRecord(String split, boolean won) {
        String key = split + (won ? "_wins" : "_losses");
        splitRecords.put(key, splitRecords.getOrDefault(key, 0) + 1);
    }

    public double getWinRate() {
        return gamesPlayed > 0 ? (double) wins / gamesPlayed : 0.0;
    }

    public void updatePerformanceMetric(String metric, double value) {
        performanceMetrics.put(metric, value);
    }

    // Getters and setters
    public int getGamesPlayed() { return gamesPlayed; }
    public int getWins() { return wins; }
    public int getLosses() { return losses; }
    public Map<String, Integer> getSplitRecords() { return splitRecords; }
    public int getChampionshipsWon() { return championshipsWon; }
    public void setChampionshipsWon(int championshipsWon) { this.championshipsWon = championshipsWon; }
    public int getInternationalAppearances() { return internationalAppearances; }
    public void setInternationalAppearances(int internationalAppearances) { this.internationalAppearances = internationalAppearances; }
    public double getAverageGameTime() { return averageGameTime; }
    public int getTotalKills() { return totalKills; }
    public int getTotalDeaths() { return totalDeaths; }
    public double getAverageKDA() { return averageKDA; }
    public Map<String, Double> getPerformanceMetrics() { return performanceMetrics; }
}
