// Team.java
package com.mobagm.entities;

import com.mobagm.core.Enums.*;
import com.mobagm.generators.LogoGenerator;
import java.util.*;
import java.util.stream.Collectors;

public class Team {
    private String name;
    private String logoDataUrl;
    private Region region;
    private League league;
    private Map<Role, Player> roster;
    private List<Player> bench;
    private List<Player> academy;
    private TeamStatistics statistics;
    private int budget;
    private String coach;
    private Map<String, Double> teamSynergy;
    private int currentSplit;
    private boolean isActive;
    private Map<String, Object> teamStrategy;

    public Team(String name, Region region, League league) {
        this.name = name;
        this.region = region;
        this.league = league;
        this.roster = new HashMap<>();
        this.bench = new ArrayList<>();
        this.academy = new ArrayList<>();
        this.statistics = new TeamStatistics();
        this.budget = generateInitialBudget();
        this.coach = generateCoachName();
        this.teamSynergy = new HashMap<>();
        this.currentSplit = 0;
        this.isActive = true;
        this.teamStrategy = new HashMap<>();
        this.logoDataUrl = LogoGenerator.generateTeamLogo(name, region);

        initializeTeamStrategy();
    }

    private int generateInitialBudget() {
        Random random = new Random();
        return 500000 + random.nextInt(1000000); // $500k to $1.5M
    }

    private String generateCoachName() {
        String[] coaches = {"Coach Kim", "Coach Johnson", "Coach Chen", "Coach Mueller",
                "Coach Silva", "Coach Petrov", "Coach Martinez", "Coach Wang"};
        return coaches[new Random().nextInt(coaches.length)];
    }

    private void initializeTeamStrategy() {
        teamStrategy.put("earlyGameFocus", 0.6);
        teamStrategy.put("lateGameFocus", 0.4);
        teamStrategy.put("aggressionLevel", 0.5);
        teamStrategy.put("visionControl", 0.7);
        teamStrategy.put("objectiveControl", 0.6);
    }

    public boolean signPlayer(Player player) {
        if (player.getSalary() > budget) {
            return false;
        }

        if (roster.containsKey(player.getRole())) {
            // Move current player to bench
            bench.add(roster.get(player.getRole()));
        }

        roster.put(player.getRole(), player);
        player.setCurrentTeam(this);
        budget -= player.getSalary();

        updateTeamSynergy();
        return true;
    }

    public void releasePlayer(Player player) {
        if (roster.containsValue(player)) {
            roster.entrySet().removeIf(entry -> entry.getValue().equals(player));
        } else {
            bench.remove(player);
        }

        player.setCurrentTeam(null);
        budget += player.getSalary() / 2; // Partial salary recovery

        updateTeamSynergy();
    }

    public void developRoster() {
        roster.values().forEach(Player::developPlayer);
        bench.forEach(Player::developPlayer);
        academy.forEach(Player::developPlayer);

        // Remove retired players
        roster.entrySet().removeIf(entry -> entry.getValue().isRetired());
        bench.removeIf(Player::isRetired);
        academy.removeIf(Player::isRetired);
    }

    private void updateTeamSynergy() {
        if (roster.size() < 5) return;

        // Calculate synergy based on player traits and roles
        double synergy = 0.0;
        List<Player> players = new ArrayList<>(roster.values());

        for (int i = 0; i < players.size(); i++) {
            for (int j = i + 1; j < players.size(); j++) {
                Player p1 = players.get(i);
                Player p2 = players.get(j);

                // Base synergy
                double pairSynergy = 0.5;

                // Trait-based synergy
                if (p1.getTraits().contains(PlayerTrait.TEAM_CAPTAIN) ||
                        p2.getTraits().contains(PlayerTrait.TEAM_CAPTAIN)) {
                    pairSynergy += 0.1;
                }

                if (p1.getTraits().contains(PlayerTrait.SYNERGY_BUILDER) ||
                        p2.getTraits().contains(PlayerTrait.SYNERGY_BUILDER)) {
                    pairSynergy += 0.15;
                }

                synergy += pairSynergy;
            }
        }

        teamSynergy.put("overall", synergy / 10.0); // Normalize
    }

    public double getTeamStrength() {
        if (roster.size() < 5) return 0.0;

        double totalStrength = roster.values().stream()
                .mapToDouble(Player::getOverall)
                .sum();

        double synergyBonus = teamSynergy.getOrDefault("overall", 0.5) * 10;

        return (totalStrength / 5.0) + synergyBonus;
    }

    public List<Player> getRosterSorted() {
        return roster.values().stream()
                .sorted((p1, p2) -> Double.compare(p2.getOverall(), p1.getOverall()))
                .collect(Collectors.toList());
    }

    public boolean isRosterComplete() {
        return roster.size() == 5 &&
                Arrays.stream(Role.values()).allMatch(role -> roster.containsKey(role));
    }

    public double getWinProbability(Team opponent) {
        if (!isRosterComplete() || !opponent.isRosterComplete()) {
            return 0.0;
        }

        double strengthDiff = getTeamStrength() - opponent.getTeamStrength();

        // Logistic function for win probability
        return 1.0 / (1.0 + Math.exp(-strengthDiff / 10.0));
    }

    // Getters and setters
    public String getName() { return name; }
    public String getLogoDataUrl() { return logoDataUrl; }
    public Region getRegion() { return region; }
    public League getLeague() { return league; }
    public void setLeague(League league) { this.league = league; }
    public Map<Role, Player> getRoster() { return roster; }
    public List<Player> getBench() { return bench; }
    public List<Player> getAcademy() { return academy; }
    public TeamStatistics getStatistics() { return statistics; }
    public int getBudget() { return budget; }
    public void setBudget(int budget) { this.budget = budget; }
    public String getCoach() { return coach; }
    public Map<String, Double> getTeamSynergy() { return teamSynergy; }
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
    public Map<String, Object> getTeamStrategy() { return teamStrategy; }

    @Override
    public String toString() {
        return String.format("%s (%s) - League: %s, Strength: %.1f",
                name, region.getDisplayName(), league, getTeamStrength());
    }
}
