// Match.java
package com.mobagm.simulation;

import com.mobagm.core.Enums.*;
import com.mobagm.entities.Player;
import com.mobagm.entities.Team;
import java.util.*;

public class Match {
    private Team team1;
    private Team team2;
    private MatchFormat format;
    private List<Game> games;
    private Team winner;
    private MatchResult result;
    private Date matchDate;
    private String tournament;
    private int round;
    private boolean isInternational;

    public Match(Team team1, Team team2, MatchFormat format) {
        this.team1 = team1;
        this.team2 = team2;
        this.format = format;
        this.games = new ArrayList<>();
        this.matchDate = new Date();
        this.isInternational = !team1.getRegion().equals(team2.getRegion());
    }

    public Match(Team team1, Team team2, MatchFormat format, String tournament, int round) {
        this(team1, team2, format);
        this.tournament = tournament;
        this.round = round;
    }

    public MatchResult simulate() {
        if (!team1.isRosterComplete() || !team2.isRosterComplete()) {
            throw new IllegalStateException("Both teams must have complete rosters");
        }

        int team1Wins = 0;
        int team2Wins = 0;
        int gamesToWin = getGamesToWin();

        while (team1Wins < gamesToWin && team2Wins < gamesToWin) {
            Game game = simulateGame();
            games.add(game);

            if (game.getWinner() == team1) {
                team1Wins++;
            } else {
                team2Wins++;
            }
        }

        winner = team1Wins > team2Wins ? team1 : team2;
        result = new MatchResult(team1, team2, team1Wins, team2Wins, winner, games);

        // Update team and player statistics
        updateStatistics();

        return result;
    }

    private int getGamesToWin() {
        switch (format) {
            case BO1: return 1;
            case BO3: return 2;
            case BO5: return 3;
            default: return 1;
        }
    }

    private Game simulateGame() {
        Game game = new Game(team1, team2);
        return game.simulate();
    }

    private void updateStatistics() {
        boolean team1Won = winner == team1;
        boolean team2Won = winner == team2;

        // Update team statistics
        team1.getStatistics().updateGameResult(team1Won, calculateAverageGameTime(),
                calculateTeamKills(team1), calculateTeamDeaths(team1));
        team2.getStatistics().updateGameResult(team2Won, calculateAverageGameTime(),
                calculateTeamKills(team2), calculateTeamDeaths(team2));

        // Update player statistics
        for (Player player : team1.getRoster().values()) {
            updatePlayerStatistics(player, team1Won);
        }

        for (Player player : team2.getRoster().values()) {
            updatePlayerStatistics(player, team2Won);
        }

        // Update international appearances if applicable
        if (isInternational) {
            for (Player player : team1.getRoster().values()) {
                player.getStatistics().setInternationalAppearances(
                        player.getStatistics().getInternationalAppearances() + 1);
            }
            for (Player player : team2.getRoster().values()) {
                player.getStatistics().setInternationalAppearances(
                        player.getStatistics().getInternationalAppearances() + 1);
            }
        }
    }

    private void updatePlayerStatistics(Player player, boolean won) {
        Random random = new Random();

        // Generate random stats based on performance
        double performance = player.getMatchPerformance();
        int kills = Math.max(0, (int) (random.nextGaussian() * 3 + performance / 20));
        int deaths = Math.max(1, (int) (random.nextGaussian() * 2 + (100 - performance) / 25));
        int assists = Math.max(0, (int) (random.nextGaussian() * 4 + performance / 15));

        player.getStatistics().updateGameStats(won, performance, kills, deaths, assists);
    }

    private double calculateAverageGameTime() {
        return games.stream().mapToDouble(Game::getGameTime).average().orElse(25.0);
    }

    private int calculateTeamKills(Team team) {
        return games.stream().mapToInt(g -> g.getKills(team)).sum();
    }

    private int calculateTeamDeaths(Team team) {
        return games.stream().mapToInt(g -> g.getDeaths(team)).sum();
    }

    // Getters
    public Team getTeam1() { return team1; }
    public Team getTeam2() { return team2; }
    public MatchFormat getFormat() { return format; }
    public List<Game> getGames() { return games; }
    public Team getWinner() { return winner; }
    public MatchResult getResult() { return result; }
    public Date getMatchDate() { return matchDate; }
    public String getTournament() { return tournament; }
    public int getRound() { return round; }
    public boolean isInternational() { return isInternational; }

    @Override
    public String toString() {
        return String.format("%s vs %s (%s) - Winner: %s",
                team1.getName(), team2.getName(), format,
                winner != null ? winner.getName() : "TBD");
    }
}
