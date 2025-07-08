// Game.java
package com.mobagm.simulation;

import com.mobagm.entities.Player;
import com.mobagm.entities.Team;
import java.util.*;

public class Game {
    private Team team1;
    private Team team2;
    private Team winner;
    private double gameTime;
    private Map<Team, Integer> kills;
    private Map<Team, Integer> deaths;
    private Map<Team, GameStats> gameStats;
    private List<GameEvent> events;
    private Champion[] draft;

    public Game(Team team1, Team team2) {
        this.team1 = team1;
        this.team2 = team2;
        this.kills = new HashMap<>();
        this.deaths = new HashMap<>();
        this.gameStats = new HashMap<>();
        this.events = new ArrayList<>();
        this.draft = new Champion[10]; // 5 for each team

        kills.put(team1, 0);
        kills.put(team2, 0);
        deaths.put(team1, 0);
        deaths.put(team2, 0);
    }

    public Game simulate() {
        // Simulate draft phase
        simulateDraft();

        // Simulate game phases
        simulateEarlyGame();
        simulateMidGame();
        simulateLateGame();

        // Determine winner
        determineWinner();

        return this;
    }

    private void simulateDraft() {
        Random random = new Random();

        // Simple draft simulation - in a real implementation this would be more complex
        String[] champions = {"Aatrox", "Azir", "Graves", "Jinx", "Thresh", "Gnar", "Zed", "Lucian", "Leona", "Syndra"};

        for (int i = 0; i < 10; i++) {
            Champion champion = new Champion(champions[random.nextInt(champions.length)]);
            draft[i] = champion;
        }
    }

    private void simulateEarlyGame() {
        Random random = new Random();

        // Early game lasts 0-15 minutes
        double phase1Time = 5 + random.nextDouble() * 10;

        // Team strength in early game
        double team1EarlyStrength = calculateEarlyGameStrength(team1);
        double team2EarlyStrength = calculateEarlyGameStrength(team2);

        // Simulate early game events
        if (team1EarlyStrength > team2EarlyStrength) {
            kills.put(team1, kills.get(team1) + random.nextInt(3) + 1);
            deaths.put(team2, deaths.get(team2) + kills.get(team1));
            events.add(new GameEvent("First Blood", team1, phase1Time));
        } else {
            kills.put(team2, kills.get(team2) + random.nextInt(3) + 1);
            deaths.put(team1, deaths.get(team1) + kills.get(team2));
            events.add(new GameEvent("First Blood", team2, phase1Time));
        }
    }

    private void simulateMidGame() {
        Random random = new Random();

        // Mid game lasts 15-25 minutes
        double phase2Time = 15 + random.nextDouble() * 10;

        // More kills and objectives
        int team1Kills = random.nextInt(5) + 2;
        int team2Kills = random.nextInt(5) + 2;

        kills.put(team1, kills.get(team1) + team1Kills);
        kills.put(team2, kills.get(team2) + team2Kills);
        deaths.put(team1, deaths.get(team1) + team2Kills);
        deaths.put(team2, deaths.get(team2) + team1Kills);

        // Baron fight
        if (random.nextBoolean()) {
            Team baronWinner = random.nextBoolean() ? team1 : team2;
            events.add(new GameEvent("Baron", baronWinner, phase2Time));
        }
    }

    private void simulateLateGame() {
        Random random = new Random();

        // Late game determines winner
        double phase3Time = 25 + random.nextDouble() * 20;
        gameTime = phase3Time;

        // Final teamfight
        double team1LateStrength = calculateLateGameStrength(team1);
        double team2LateStrength = calculateLateGameStrength(team2);

        if (team1LateStrength > team2LateStrength) {
            events.add(new GameEvent("Victory", team1, gameTime));
        } else {
            events.add(new GameEvent("Victory", team2, gameTime));
        }
    }

    private double calculateEarlyGameStrength(Team team) {
        double strength = 0;
        for (Player player : team.getRoster().values()) {
            strength += player.getMechanical() * 0.7 + player.getStrategic() * 0.3;
        }
        return strength / 5.0;
    }

    private double calculateLateGameStrength(Team team) {
        double strength = 0;
        for (Player player : team.getRoster().values()) {
            strength += player.getMechanical() * 0.4 + player.getStrategic() * 0.6;
        }
        return strength / 5.0;
    }

    private void determineWinner() {
        // Winner is typically the team with better late game performance
        double team1Strength = calculateLateGameStrength(team1);
        double team2Strength = calculateLateGameStrength(team2);

        // Add some randomness
        Random random = new Random();
        team1Strength += random.nextGaussian() * 5;
        team2Strength += random.nextGaussian() * 5;

        winner = team1Strength > team2Strength ? team1 : team2;
    }

    // Getters
    public Team getTeam1() { return team1; }
    public Team getTeam2() { return team2; }
    public Team getWinner() { return winner; }
    public double getGameTime() { return gameTime; }
    public int getKills(Team team) { return kills.getOrDefault(team, 0); }
    public int getDeaths(Team team) { return deaths.getOrDefault(team, 0); }
    public List<GameEvent> getEvents() { return events; }
    public Champion[] getDraft() { return draft; }

    // Inner classes
    public static class GameEvent {
        private String type;
        private Team team;
        private double time;

        public GameEvent(String type, Team team, double time) {
            this.type = type;
            this.team = team;
            this.time = time;
        }

        public String getType() { return type; }
        public Team getTeam() { return team; }
        public double getTime() { return time; }
    }

    public static class GameStats {
        private int kills;
        private int deaths;
        private int assists;
        private double goldEarned;
        private double damageDealt;

        public GameStats(int kills, int deaths, int assists, double goldEarned, double damageDealt) {
            this.kills = kills;
            this.deaths = deaths;
            this.assists = assists;
            this.goldEarned = goldEarned;
            this.damageDealt = damageDealt;
        }

        // Getters
        public int getKills() { return kills; }
        public int getDeaths() { return deaths; }
        public int getAssists() { return assists; }
        public double getGoldEarned() { return goldEarned; }
        public double getDamageDealt() { return damageDealt; }
    }

    public static class Champion {
        private String name;
        private double winRate;
        private Map<String, Double> synergies;

        public Champion(String name) {
            this.name = name;
            this.winRate = 0.5; // Base 50% win rate
            this.synergies = new HashMap<>();
        }

        public String getName() { return name; }
        public double getWinRate() { return winRate; }
        public void setWinRate(double winRate) { this.winRate = winRate; }
        public Map<String, Double> getSynergies() { return synergies; }
    }
}
