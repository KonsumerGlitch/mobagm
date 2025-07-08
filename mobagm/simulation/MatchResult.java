// MatchResult.java
package com.mobagm.simulation;

import com.mobagm.entities.Team;
import java.util.List;

public class MatchResult {
    private Team team1;
    private Team team2;
    private int team1Score;
    private int team2Score;
    private Team winner;
    private List<Game> games;
    private double averageGameTime;
    private int totalKills;
    private String mvpPlayer;

    public MatchResult(Team team1, Team team2, int team1Score, int team2Score,
                       Team winner, List<Game> games) {
        this.team1 = team1;
        this.team2 = team2;
        this.team1Score = team1Score;
        this.team2Score = team2Score;
        this.winner = winner;
        this.games = games;

        calculateMatchStats();
    }

    private void calculateMatchStats() {
        if (games.isEmpty()) return;

        averageGameTime = games.stream().mapToDouble(Game::getGameTime).average().orElse(0.0);
        totalKills = games.stream().mapToInt(g -> g.getKills(team1) + g.getKills(team2)).sum();

        // Simple MVP calculation - player from winning team with best performance
        // In a real implementation, this would be more sophisticated
        mvpPlayer = "Player from " + winner.getName();
    }

    public boolean isUpset() {
        double team1Strength = team1.getTeamStrength();
        double team2Strength = team2.getTeamStrength();

        // An upset is when the weaker team wins by more than 10 points difference
        return Math.abs(team1Strength - team2Strength) > 10 &&
                ((team1Strength < team2Strength && winner == team1) ||
                        (team2Strength < team1Strength && winner == team2));
    }

    public String getScoreString() {
        return team1Score + "-" + team2Score;
    }

    // Getters
    public Team getTeam1() { return team1; }
    public Team getTeam2() { return team2; }
    public int getTeam1Score() { return team1Score; }
    public int getTeam2Score() { return team2Score; }
    public Team getWinner() { return winner; }
    public Team getLoser() { return winner == team1 ? team2 : team1; }
    public List<Game> getGames() { return games; }
    public double getAverageGameTime() { return averageGameTime; }
    public int getTotalKills() { return totalKills; }
    public String getMvpPlayer() { return mvpPlayer; }

    @Override
    public String toString() {
        return String.format("%s %d-%d %s (Winner: %s)",
                team1.getName(), team1Score, team2Score,
                team2.getName(), winner.getName());
    }
}
