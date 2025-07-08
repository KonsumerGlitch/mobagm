// PlayerStatistics.java
package com.mobagm.entities;

public class PlayerStatistics {
    private int gamesPlayed;
    private int wins;
    private int losses;
    private double averagePerformance;
    private int kills;
    private int deaths;
    private int assists;
    private double kda;
    private int mvpAwards;
    private int internationalAppearances;
    private double clutchPerformance;

    public PlayerStatistics() {
        this.gamesPlayed = 0;
        this.wins = 0;
        this.losses = 0;
        this.averagePerformance = 0.0;
        this.kills = 0;
        this.deaths = 0;
        this.assists = 0;
        this.kda = 0.0;
        this.mvpAwards = 0;
        this.internationalAppearances = 0;
        this.clutchPerformance = 0.0;
    }

    public void updateGameStats(boolean won, double performance, int kills, int deaths, int assists) {
        gamesPlayed++;
        if (won) {
            wins++;
        } else {
            losses++;
        }

        averagePerformance = ((averagePerformance * (gamesPlayed - 1)) + performance) / gamesPlayed;

        this.kills += kills;
        this.deaths += deaths;
        this.assists += assists;

        updateKDA();
    }

    private void updateKDA() {
        if (deaths == 0) {
            kda = kills + assists;
        } else {
            kda = (double) (kills + assists) / deaths;
        }
    }

    public double getWinRate() {
        return gamesPlayed > 0 ? (double) wins / gamesPlayed : 0.0;
    }

    // Getters and setters
    public int getGamesPlayed() { return gamesPlayed; }
    public int getWins() { return wins; }
    public int getLosses() { return losses; }
    public double getAveragePerformance() { return averagePerformance; }
    public int getKills() { return kills; }
    public int getDeaths() { return deaths; }
    public int getAssists() { return assists; }
    public double getKda() { return kda; }
    public int getMvpAwards() { return mvpAwards; }
    public void setMvpAwards(int mvpAwards) { this.mvpAwards = mvpAwards; }
    public int getInternationalAppearances() { return internationalAppearances; }
    public void setInternationalAppearances(int internationalAppearances) { this.internationalAppearances = internationalAppearances; }
    public double getClutchPerformance() { return clutchPerformance; }
    public void setClutchPerformance(double clutchPerformance) { this.clutchPerformance = clutchPerformance; }
}
