// LeagueManager.java
package com.mobagm.core;

import com.mobagm.core.Enums.*;
import com.mobagm.entities.Player;
import com.mobagm.entities.Team;
import com.mobagm.leagues.RegionalLeague;
import com.mobagm.tournaments.SwissTournament;
import com.mobagm.tournaments.Tournament;
import java.util.*;
import java.util.stream.Collectors;

public class LeagueManager {
    private Map<Region, RegionalLeague> regionalLeagues;
    private List<Tournament> internationalTournaments;
    private int currentYear;
    private Split currentSplit;
    private boolean isRunning;
    private List<Player> freeAgents;
    private AllStarManager allStarManager;
    private StatisticsManager statisticsManager;
    private SimulationSettings settings;

    public LeagueManager() {
        this.regionalLeagues = new HashMap<>();
        this.internationalTournaments = new ArrayList<>();
        this.currentYear = 2024;
        this.currentSplit = Split.WINTER;
        this.isRunning = false;
        this.freeAgents = new ArrayList<>();
        this.allStarManager = new AllStarManager();
        this.statisticsManager = new StatisticsManager();
        this.settings = new SimulationSettings();

        initializeLeagues();
    }

    private void initializeLeagues() {
        // Initialize regional leagues
        for (Region region : Region.values()) {
            RegionalLeague league = new RegionalLeague(region);
            league.initializeLeague();
            regionalLeagues.put(region, league);
        }

        System.out.println("MOBAGM Simulation initialized with " + Region.values().length + " regions");
        System.out.println("Starting year: " + currentYear);
    }

    public void startSimulation() {
        isRunning = true;
        System.out.println("=== MOBAGM Simulation Started ===");

        while (isRunning) {
            simulateYear();

            if (settings.isAutoAdvance()) {
                currentYear++;
            } else {
                // Wait for user input to continue
                break;
            }
        }
    }

    private void simulateYear() {
        System.out.println("\n=== YEAR " + currentYear + " ===");

        // Simulate all three splits
        for (Split split : Split.values()) {
            currentSplit = split;
            simulateSplit();
        }

        // End of year activities
        conductEndOfYearActivities();

        // Update statistics
        statisticsManager.updateYearlyStatistics(currentYear);
    }

    private void simulateSplit() {
        System.out.println("\n--- " + currentSplit + " " + currentYear + " ---");

        // Simulate all regional leagues
        for (RegionalLeague league : regionalLeagues.values()) {
            league.simulateSplit();
        }

        // International tournaments
        if (currentSplit == Split.SPRING) {
            simulateMSI();
        } else if (currentSplit == Split.SUMMER) {
            simulateWorlds();
        }

        // All-Star events
        if (currentSplit == Split.WINTER) {
            allStarManager.simulateAllStarEvent(this);
        }

        // Update free agency
        updateFreeAgency();
    }

    private void simulateMSI() {
        System.out.println("\n=== MSI " + currentYear + " ===");

        // Get representative from each region (Spring champions)
        List<Team> msiTeams = new ArrayList<>();
        for (RegionalLeague league : regionalLeagues.values()) {
            Team springChampion = league.getSplitWinner();
            if (springChampion != null) {
                msiTeams.add(springChampion);
                springChampion.getStatistics().setInternationalAppearances(
                        springChampion.getStatistics().getInternationalAppearances() + 1);
            }
        }

        // Create MSI tournament
        Tournament msi = new SwissTournament("MSI " + currentYear, 3);
        msiTeams.forEach(msi::addParticipant);

        msi.simulateTournament();
        internationalTournaments.add(msi);

        System.out.println("MSI " + currentYear + " Champion: " + msi.getWinner().getName());
    }

    private void simulateWorlds() {
        System.out.println("\n=== WORLDS " + currentYear + " ===");

        // Get representatives from each region
        List<Team> worldsTeams = new ArrayList<>();

        for (RegionalLeague league : regionalLeagues.values()) {
            List<Team> regionTeams = league.getTopTeamsForInternational(3); // Top 3 from each region
            worldsTeams.addAll(regionTeams);

            // Mark international appearances
            for (Team team : regionTeams) {
                team.getStatistics().setInternationalAppearances(
                        team.getStatistics().getInternationalAppearances() + 1);
            }
        }

        // Create Worlds tournament
        Tournament worlds = new SwissTournament("Worlds " + currentYear, 5);
        worldsTeams.forEach(worlds::addParticipant);

        worlds.simulateTournament();
        internationalTournaments.add(worlds);

        System.out.println("Worlds " + currentYear + " Champion: " + worlds.getWinner().getName());

        // Award championship
        worlds.getWinner().getStatistics().setChampionshipsWon(
                worlds.getWinner().getStatistics().getChampionshipsWon() + 1);
    }

    private void conductEndOfYearActivities() {
        System.out.println("\n=== End of Year " + currentYear + " Activities ===");

        // Retire old players and generate new ones
        managePlayerRetirements();

        // Generate new rookie players
        generateRookies();

        // Update free agency
        updateFreeAgency();

        // Print year summary
        printYearSummary();
    }

    private void managePlayerRetirements() {
        int retiredCount = 0;

        for (RegionalLeague league : regionalLeagues.values()) {
            // Check Champions teams
            for (Team team : league.getChampionsTeams()) {
                Iterator<Player> iterator = team.getRoster().values().iterator();
                while (iterator.hasNext()) {
                    Player player = iterator.next();
                    if (player.isRetired()) {
                        System.out.println(player.getName() + " has retired from professional play");
                        team.releasePlayer(player);
                        retiredCount++;
                    }
                }
            }

            // Check Challengers teams
            for (Team team : league.getChallengersTeams()) {
                Iterator<Player> iterator = team.getRoster().values().iterator();
                while (iterator.hasNext()) {
                    Player player = iterator.next();
                    if (player.isRetired()) {
                        System.out.println(player.getName() + " has retired from professional play");
                        team.releasePlayer(player);
                        retiredCount++;
                    }
                }
            }
        }

        System.out.println("Total players retired: " + retiredCount);
    }

    private void generateRookies() {
        int rookiesGenerated = 0;

        // Generate rookies for each region
        for (Region region : Region.values()) {
            int rookiesForRegion = 10 + new Random().nextInt(20); // 10-30 rookies per region

            for (int i = 0; i < rookiesForRegion; i++) {
                Role role = Role.values()[new Random().nextInt(Role.values().length)];
                String name = com.mobagm.generators.NameGenerator.generatePlayerName();
                Player rookie = new Player(name, role, 16 + new Random().nextInt(3)); // 16-18 years old

                // Rookies have lower salary expectations
                rookie.setSalary(20000 + new Random().nextInt(50000));
                rookie.setContractLength(1 + new Random().nextInt(2));

                freeAgents.add(rookie);
                rookiesGenerated++;
            }
        }

        System.out.println("Generated " + rookiesGenerated + " rookie players");
    }

    private void updateFreeAgency() {
        // Remove players who have been signed
        freeAgents.removeIf(player -> player.getCurrentTeam() != null);

        // Add players whose contracts have expired
        for (RegionalLeague league : regionalLeagues.values()) {
            List<Team> allTeams = new ArrayList<>();
            allTeams.addAll(league.getChampionsTeams());
            allTeams.addAll(league.getChallengersTeams());

            for (Team team : allTeams) {
                List<Player> toRelease = new ArrayList<>();

                for (Player player : team.getRoster().values()) {
                    player.setContractLength(player.getContractLength() - 1);
                    if (player.getContractLength() <= 0) {
                        toRelease.add(player);
                    }
                }

                for (Player player : toRelease) {
                    team.releasePlayer(player);
                    freeAgents.add(player);
                }
            }
        }

        System.out.println("Free agents available: " + freeAgents.size());
    }

    private void printYearSummary() {
        System.out.println("\n=== " + currentYear + " YEAR SUMMARY ===");

        // Regional champions
        System.out.println("\nRegional Champions:");
        for (Region region : Region.values()) {
            RegionalLeague league = regionalLeagues.get(region);
            Team champion = league.getSplitWinner();
            if (champion != null) {
                System.out.println(region.getDisplayName() + ": " + champion.getName());
            }
        }

        // International tournaments
        System.out.println("\nInternational Tournament Winners:");
        for (Tournament tournament : internationalTournaments) {
            if (tournament.getName().contains(String.valueOf(currentYear))) {
                System.out.println(tournament.getName() + ": " + tournament.getWinner().getName());
            }
        }

        // Top players by region
        System.out.println("\nTop Players by Region:");
        for (Region region : Region.values()) {
            List<Player> topPlayers = getTopPlayersByRegion(region, 3);
            System.out.println(region.getDisplayName() + ":");
            for (int i = 0; i < topPlayers.size(); i++) {
                Player player = topPlayers.get(i);
                System.out.println("  " + (i + 1) + ". " + player.getName() + " (" +
                        player.getRole() + ") - " + String.format("%.1f", player.getOverall()) + " OVR");
            }
        }
    }

    public List<Player> getTopPlayersByRegion(Region region, int count) {
        RegionalLeague league = regionalLeagues.get(region);
        List<Player> allPlayers = new ArrayList<>();

        // Collect all players from Champions teams
        for (Team team : league.getChampionsTeams()) {
            allPlayers.addAll(team.getRoster().values());
        }

        // Sort by overall rating
        return allPlayers.stream()
                .sorted((p1, p2) -> Double.compare(p2.getOverall(), p1.getOverall()))
                .limit(count)
                .collect(Collectors.toList());
    }

    public List<Player> getAllStarPlayersByRegion(Region region) {
        return allStarManager.selectAllStarPlayers(region, this);
    }

    public void pauseSimulation() {
        isRunning = false;
    }

    public void resumeSimulation() {
        isRunning = true;
    }

    public void setSimulationSpeed(int speed) {
        settings.setSimulationSpeed(speed);
    }

    // Getters
    public Map<Region, RegionalLeague> getRegionalLeagues() { return regionalLeagues; }
    public List<Tournament> getInternationalTournaments() { return internationalTournaments; }
    public int getCurrentYear() { return currentYear; }
    public Split getCurrentSplit() { return currentSplit; }
    public boolean isRunning() { return isRunning; }
    public List<Player> getFreeAgents() { return freeAgents; }
    public AllStarManager getAllStarManager() { return allStarManager; }
    public StatisticsManager getStatisticsManager() { return statisticsManager; }
    public SimulationSettings getSettings() { return settings; }

    // Inner classes for supporting functionality
    public static class AllStarManager {
        public List<Player> selectAllStarPlayers(Region region, LeagueManager manager) {
            List<Player> allPlayers = new ArrayList<>();
            RegionalLeague league = manager.getRegionalLeagues().get(region);

            // Collect all players from Champions teams
            for (Team team : league.getChampionsTeams()) {
                allPlayers.addAll(team.getRoster().values());
            }

            // Sort by performance and select top players by role
            Map<Role, List<Player>> playersByRole = allPlayers.stream()
                    .collect(Collectors.groupingBy(Player::getRole));

            List<Player> allStars = new ArrayList<>();
            for (Role role : Role.values()) {
                List<Player> rolePlayers = playersByRole.get(role);
                if (rolePlayers != null && !rolePlayers.isEmpty()) {
                    rolePlayers.sort((p1, p2) -> Double.compare(p2.getOverall(), p1.getOverall()));
                    allStars.add(rolePlayers.get(0)); // Best player in each role
                }
            }

            return allStars;
        }

        public void simulateAllStarEvent(LeagueManager manager) {
            System.out.println("\n=== All-Star Event " + manager.getCurrentYear() + " ===");

            // Select all-star players from each region
            Map<Region, List<Player>> allStarsByRegion = new HashMap<>();
            for (Region region : Region.values()) {
                allStarsByRegion.put(region, selectAllStarPlayers(region, manager));
            }

            // Print all-star selections
            for (Region region : Region.values()) {
                System.out.println("\n" + region.getDisplayName() + " All-Stars:");
                List<Player> allStars = allStarsByRegion.get(region);
                for (Player player : allStars) {
                    System.out.println("  " + player.getRole() + ": " + player.getName() +
                            " (" + player.getCurrentTeam().getName() + ") - " +
                            String.format("%.1f", player.getOverall()) + " OVR");
                }
            }
        }
    }

    public static class StatisticsManager {
        private Map<Integer, YearlyStatistics> yearlyStats;

        public StatisticsManager() {
            this.yearlyStats = new HashMap<>();
        }

        public void updateYearlyStatistics(int year) {
            YearlyStatistics stats = new YearlyStatistics(year);
            yearlyStats.put(year, stats);
        }

        public YearlyStatistics getYearlyStatistics(int year) {
            return yearlyStats.get(year);
        }

        public static class YearlyStatistics {
            private int year;
            private Map<Region, Team> regionalChampions;
            private Team msiChampion;
            private Team worldsChampion;
            private int totalMatches;
            private int totalGames;
            private double averageGameTime;

            public YearlyStatistics(int year) {
                this.year = year;
                this.regionalChampions = new HashMap<>();
            }

            // Getters and setters
            public int getYear() { return year; }
            public Map<Region, Team> getRegionalChampions() { return regionalChampions; }
            public Team getMsiChampion() { return msiChampion; }
            public void setMsiChampion(Team msiChampion) { this.msiChampion = msiChampion; }
            public Team getWorldsChampion() { return worldsChampion; }
            public void setWorldsChampion(Team worldsChampion) { this.worldsChampion = worldsChampion; }
            public int getTotalMatches() { return totalMatches; }
            public void setTotalMatches(int totalMatches) { this.totalMatches = totalMatches; }
            public int getTotalGames() { return totalGames; }
            public void setTotalGames(int totalGames) { this.totalGames = totalGames; }
            public double getAverageGameTime() { return averageGameTime; }
            public void setAverageGameTime(double averageGameTime) { this.averageGameTime = averageGameTime; }
        }
    }

    public static class SimulationSettings {
        private boolean autoAdvance;
        private int simulationSpeed;
        private boolean enablePromotionRelegation;
        private boolean enableInternationalTournaments;
        private boolean enableAllStarEvents;
        private boolean enablePlayerDevelopment;

        public SimulationSettings() {
            this.autoAdvance = true;
            this.simulationSpeed = 1;
            this.enablePromotionRelegation = true;
            this.enableInternationalTournaments = true;
            this.enableAllStarEvents = true;
            this.enablePlayerDevelopment = true;
        }

        // Getters and setters
        public boolean isAutoAdvance() { return autoAdvance; }
        public void setAutoAdvance(boolean autoAdvance) { this.autoAdvance = autoAdvance; }
        public int getSimulationSpeed() { return simulationSpeed; }
        public void setSimulationSpeed(int simulationSpeed) { this.simulationSpeed = simulationSpeed; }
        public boolean isEnablePromotionRelegation() { return enablePromotionRelegation; }
        public void setEnablePromotionRelegation(boolean enablePromotionRelegation) { this.enablePromotionRelegation = enablePromotionRelegation; }
        public boolean isEnableInternationalTournaments() { return enableInternationalTournaments; }
        public void setEnableInternationalTournaments(boolean enableInternationalTournaments) { this.enableInternationalTournaments = enableInternationalTournaments; }
        public boolean isEnableAllStarEvents() { return enableAllStarEvents; }
        public void setEnableAllStarEvents(boolean enableAllStarEvents) { this.enableAllStarEvents = enableAllStarEvents; }
        public boolean isEnablePlayerDevelopment() { return enablePlayerDevelopment; }
        public void setEnablePlayerDevelopment(boolean enablePlayerDevelopment) { this.enablePlayerDevelopment = enablePlayerDevelopment; }
    }
}
