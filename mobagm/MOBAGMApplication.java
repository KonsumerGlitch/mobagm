// MOBAGMApplication.java
package com.mobagm;

import com.mobagm.core.LeagueManager;
import com.mobagm.core.Enums.Region;
import com.mobagm.entities.Player;
import com.mobagm.entities.Team;
import com.mobagm.ui.GameUI;
import java.util.Scanner;

public class MOBAGMApplication {
    private LeagueManager leagueManager;
    private GameUI gameUI;
    private Scanner scanner;
    private boolean isRunning;

    public MOBAGMApplication() {
        this.leagueManager = new LeagueManager();
        this.gameUI = new GameUI(this);
        this.scanner = new Scanner(System.in);
        this.isRunning = true;
    }

    public static void main(String[] args) {
        System.out.println("===============================================");
        System.out.println("       MOBAGM - MOBA General Manager          ");
        System.out.println("    Professional Esports Management Game      ");
        System.out.println("===============================================");
        System.out.println();

        MOBAGMApplication app = new MOBAGMApplication();
        app.run();
    }

    public void run() {
        displayWelcomeMessage();

        while (isRunning) {
            displayMainMenu();
            handleUserInput();
        }

        scanner.close();
        System.out.println("Thank you for playing MOBAGM!");
    }

    private void displayWelcomeMessage() {
        System.out.println("Welcome to MOBAGM - the ultimate MOBA esports management simulation!");
        System.out.println("Manage teams across 5 global regions:");
        System.out.println("• Americas (LTA) • EMEA (LEC) • Korea (LCK) • China (LPL) • Pacific (LCP)");
        System.out.println();
        System.out.println("Features:");
        System.out.println("• 3 splits per year (Winter, Spring, Summer)");
        System.out.println("• Promotion/Relegation system");
        System.out.println("• International tournaments (MSI, Worlds)");
        System.out.println("• Procedurally generated players and teams");
        System.out.println("• Deep player development system");
        System.out.println("• Swiss-style tournament simulation");
        System.out.println();
    }

    private void displayMainMenu() {
        System.out.println("=== MAIN MENU ===");
        System.out.println("1. Start/Resume Simulation");
        System.out.println("2. View League Standings");
        System.out.println("3. View Player Statistics");
        System.out.println("4. View Team Information");
        System.out.println("5. View Tournament History");
        System.out.println("6. Manage Free Agents");
        System.out.println("7. View All-Star Players");
        System.out.println("8. Simulation Settings");
        System.out.println("9. Generate Reports");
        System.out.println("0. Exit");
        System.out.print("Enter your choice: ");
    }

    private void handleUserInput() {
        try {
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    startSimulation();
                    break;
                case 2:
                    viewLeagueStandings();
                    break;
                case 3:
                    viewPlayerStatistics();
                    break;
                case 4:
                    viewTeamInformation();
                    break;
                case 5:
                    viewTournamentHistory();
                    break;
                case 6:
                    manageFreeAgents();
                    break;
                case 7:
                    viewAllStarPlayers();
                    break;
                case 8:
                    simulationSettings();
                    break;
                case 9:
                    generateReports();
                    break;
                case 0:
                    isRunning = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
        }

        if (isRunning) {
            System.out.println("\nPress Enter to continue...");
            scanner.nextLine();
        }
    }

    private void startSimulation() {
        System.out.println("\n=== SIMULATION CONTROL ===");
        System.out.println("Current Year: " + leagueManager.getCurrentYear());
        System.out.println("Current Split: " + leagueManager.getCurrentSplit());
        System.out.println("Status: " + (leagueManager.isRunning() ? "Running" : "Paused"));
        System.out.println();

        if (leagueManager.isRunning()) {
            System.out.println("1. Pause Simulation");
            System.out.println("2. View Live Results");
            System.out.println("3. Back to Main Menu");
        } else {
            System.out.println("1. Start Simulation");
            System.out.println("2. Resume Simulation");
            System.out.println("3. Back to Main Menu");
        }

        System.out.print("Enter your choice: ");
        int choice = Integer.parseInt(scanner.nextLine());

        switch (choice) {
            case 1:
                if (leagueManager.isRunning()) {
                    leagueManager.pauseSimulation();
                    System.out.println("Simulation paused.");
                } else {
                    leagueManager.startSimulation();
                }
                break;
            case 2:
                if (leagueManager.isRunning()) {
                    // Show live results
                    System.out.println("Simulation is running...");
                } else {
                    leagueManager.resumeSimulation();
                    System.out.println("Simulation resumed.");
                }
                break;
            case 3:
                return;
        }
    }

    private void viewLeagueStandings() {
        System.out.println("\n=== LEAGUE STANDINGS ===");
        System.out.println("Select a region:");

        Region[] regions = Region.values();
        for (int i = 0; i < regions.length; i++) {
            System.out.println((i + 1) + ". " + regions[i].getDisplayName());
        }

        System.out.print("Enter your choice: ");
        int choice = Integer.parseInt(scanner.nextLine());

        if (choice >= 1 && choice <= regions.length) {
            Region selectedRegion = regions[choice - 1];
            leagueManager.getRegionalLeagues().get(selectedRegion).printStandings();
        } else {
            System.out.println("Invalid choice.");
        }
    }

    private void viewPlayerStatistics() {
        System.out.println("\n=== PLAYER STATISTICS ===");
        System.out.println("Select a region:");

        Region[] regions = Region.values();
        for (int i = 0; i < regions.length; i++) {
            System.out.println((i + 1) + ". " + regions[i].getDisplayName());
        }

        System.out.print("Enter your choice: ");
        int choice = Integer.parseInt(scanner.nextLine());

        if (choice >= 1 && choice <= regions.length) {
            Region selectedRegion = regions[choice - 1];
            var topPlayers = leagueManager.getTopPlayersByRegion(selectedRegion, 10);

            System.out.println("\nTop 10 Players in " + selectedRegion.getDisplayName() + ":");
            System.out.println("Rank | Name | Role | Age | Overall | Team");
            System.out.println("-----|------|------|-----|---------|------");

            for (int i = 0; i < topPlayers.size(); i++) {
                Player player = topPlayers.get(i);
                System.out.printf("%4d | %-20s | %-7s | %3d | %7.1f | %s%n",
                        i + 1, player.getName(), player.getRole(),
                        player.getAge(), player.getOverall(),
                        player.getCurrentTeam() != null ? player.getCurrentTeam().getName() : "Free Agent");
            }
        } else {
            System.out.println("Invalid choice.");
        }
    }

    private void viewTeamInformation() {
        System.out.println("\n=== TEAM INFORMATION ===");
        System.out.println("Select a region:");

        Region[] regions = Region.values();
        for (int i = 0; i < regions.length; i++) {
            System.out.println((i + 1) + ". " + regions[i].getDisplayName());
        }

        System.out.print("Enter your choice: ");
        int choice = Integer.parseInt(scanner.nextLine());

        if (choice >= 1 && choice <= regions.length) {
            Region selectedRegion = regions[choice - 1];
            var league = leagueManager.getRegionalLeagues().get(selectedRegion);

            System.out.println("\nChampions Series Teams:");
            for (int i = 0; i < league.getChampionsTeams().size(); i++) {
                Team team = league.getChampionsTeams().get(i);
                System.out.println((i + 1) + ". " + team.getName() + " - Strength: " +
                        String.format("%.1f", team.getTeamStrength()));
            }
        } else {
            System.out.println("Invalid choice.");
        }
    }

    private void viewTournamentHistory() {
        System.out.println("\n=== TOURNAMENT HISTORY ===");

        var tournaments = leagueManager.getInternationalTournaments();
        if (tournaments.isEmpty()) {
            System.out.println("No international tournaments have been completed yet.");
            return;
        }

        System.out.println("International Tournament Winners:");
        for (var tournament : tournaments) {
            System.out.println("• " + tournament.getName() + ": " + tournament.getWinner().getName());
        }
    }

    private void manageFreeAgents() {
        System.out.println("\n=== FREE AGENTS ===");

        var freeAgents = leagueManager.getFreeAgents();
        if (freeAgents.isEmpty()) {
            System.out.println("No free agents available.");
            return;
        }

        System.out.println("Available Free Agents (" + freeAgents.size() + "):");
        System.out.println("Name | Role | Age | Overall | Potential | Salary");
        System.out.println("-----|------|-----|---------|-----------|--------");

        for (int i = 0; i < Math.min(20, freeAgents.size()); i++) {
            Player player = freeAgents.get(i);
            System.out.printf("%-20s | %-7s | %3d | %7.1f | %9.1f | $%,d%n",
                    player.getName(), player.getRole(), player.getAge(),
                    player.getOverall(), player.getPotential(), player.getSalary());
        }

        if (freeAgents.size() > 20) {
            System.out.println("... and " + (freeAgents.size() - 20) + " more players");
        }
    }

    private void viewAllStarPlayers() {
        System.out.println("\n=== ALL-STAR PLAYERS ===");

        for (Region region : Region.values()) {
            var allStars = leagueManager.getAllStarPlayersByRegion(region);

            System.out.println("\n" + region.getDisplayName() + " All-Stars:");
            for (Player player : allStars) {
                System.out.println("• " + player.getRole() + ": " + player.getName() +
                        " (" + player.getCurrentTeam().getName() + ") - " +
                        String.format("%.1f", player.getOverall()) + " OVR");
            }
        }
    }

    private void simulationSettings() {
        System.out.println("\n=== SIMULATION SETTINGS ===");
        var settings = leagueManager.getSettings();

        System.out.println("Current Settings:");
        System.out.println("1. Auto Advance: " + settings.isAutoAdvance());
        System.out.println("2. Simulation Speed: " + settings.getSimulationSpeed());
        System.out.println("3. Promotion/Relegation: " + settings.isEnablePromotionRelegation());
        System.out.println("4. International Tournaments: " + settings.isEnableInternationalTournaments());
        System.out.println("5. All-Star Events: " + settings.isEnableAllStarEvents());
        System.out.println("6. Player Development: " + settings.isEnablePlayerDevelopment());
        System.out.println("7. Back to Main Menu");

        System.out.print("Enter setting to toggle (1-6) or 7 to go back: ");
        int choice = Integer.parseInt(scanner.nextLine());

        switch (choice) {
            case 1:
                settings.setAutoAdvance(!settings.isAutoAdvance());
                System.out.println("Auto Advance set to: " + settings.isAutoAdvance());
                break;
            case 2:
                System.out.print("Enter new simulation speed (1-10): ");
                int speed = Integer.parseInt(scanner.nextLine());
                settings.setSimulationSpeed(Math.max(1, Math.min(10, speed)));
                System.out.println("Simulation speed set to: " + settings.getSimulationSpeed());
                break;
            case 3:
                settings.setEnablePromotionRelegation(!settings.isEnablePromotionRelegation());
                System.out.println("Promotion/Relegation set to: " + settings.isEnablePromotionRelegation());
                break;
            case 4:
                settings.setEnableInternationalTournaments(!settings.isEnableInternationalTournaments());
                System.out.println("International Tournaments set to: " + settings.isEnableInternationalTournaments());
                break;
            case 5:
                settings.setEnableAllStarEvents(!settings.isEnableAllStarEvents());
                System.out.println("All-Star Events set to: " + settings.isEnableAllStarEvents());
                break;
            case 6:
                settings.setEnablePlayerDevelopment(!settings.isEnablePlayerDevelopment());
                System.out.println("Player Development set to: " + settings.isEnablePlayerDevelopment());
                break;
            case 7:
                return;
        }
    }

    private void generateReports() {
        System.out.println("\n=== GENERATE REPORTS ===");
        System.out.println("1. League Summary Report");
        System.out.println("2. Player Performance Report");
        System.out.println("3. Team Analysis Report");
        System.out.println("4. Tournament History Report");
        System.out.println("5. Back to Main Menu");

        System.out.print("Enter your choice: ");
        int choice = Integer.parseInt(scanner.nextLine());

        switch (choice) {
            case 1:
                generateLeagueSummaryReport();
                break;
            case 2:
                generatePlayerPerformanceReport();
                break;
            case 3:
                generateTeamAnalysisReport();
                break;
            case 4:
                generateTournamentHistoryReport();
                break;
            case 5:
                return;
        }
    }

    private void generateLeagueSummaryReport() {
        System.out.println("\n=== LEAGUE SUMMARY REPORT ===");
        System.out.println("Current Year: " + leagueManager.getCurrentYear());
        System.out.println("Current Split: " + leagueManager.getCurrentSplit());
        System.out.println();

        for (Region region : Region.values()) {
            var league = leagueManager.getRegionalLeagues().get(region);
            System.out.println(region.getDisplayName() + ":");
            System.out.println("  Champions Teams: " + league.getChampionsTeams().size());
            System.out.println("  Challengers Teams: " + league.getChallengersTeams().size());
            if (league.getSplitWinner() != null) {
                System.out.println("  Current Split Winner: " + league.getSplitWinner().getName());
            }
            System.out.println();
        }
    }

    private void generatePlayerPerformanceReport() {
        System.out.println("\n=== PLAYER PERFORMANCE REPORT ===");

        // Global top performers
        System.out.println("Global Top 10 Players:");
        var allPlayers = leagueManager.getRegionalLeagues().values().stream()
                .flatMap(league -> league.getChampionsTeams().stream())
                .flatMap(team -> team.getRoster().values().stream())
                .sorted((p1, p2) -> Double.compare(p2.getOverall(), p1.getOverall()))
                .limit(10)
                .toList();

        for (int i = 0; i < allPlayers.size(); i++) {
            Player player = allPlayers.get(i);
            System.out.printf("%2d. %-20s (%s) - %.1f OVR - %s%n",
                    i + 1, player.getName(), player.getRole(),
                    player.getOverall(), player.getCurrentTeam().getName());
        }
    }

    private void generateTeamAnalysisReport() {
        System.out.println("\n=== TEAM ANALYSIS REPORT ===");

        // Strongest teams globally
        var allTeams = leagueManager.getRegionalLeagues().values().stream()
                .flatMap(league -> league.getChampionsTeams().stream())
                .sorted((t1, t2) -> Double.compare(t2.getTeamStrength(), t1.getTeamStrength()))
                .limit(10)
                .toList();

        System.out.println("Global Top 10 Teams:");
        for (int i = 0; i < allTeams.size(); i++) {
            Team team = allTeams.get(i);
            System.out.printf("%2d. %-25s (%s) - %.1f Strength%n",
                    i + 1, team.getName(), team.getRegion().getDisplayName(), team.getTeamStrength());
        }
    }

    private void generateTournamentHistoryReport() {
        System.out.println("\n=== TOURNAMENT HISTORY REPORT ===");

        var tournaments = leagueManager.getInternationalTournaments();
        if (tournaments.isEmpty()) {
            System.out.println("No international tournaments completed yet.");
            return;
        }

        System.out.println("International Tournament Results:");
        for (var tournament : tournaments) {
            System.out.println("• " + tournament.getName());
            System.out.println("  Winner: " + tournament.getWinner().getName() +
                    " (" + tournament.getWinner().getRegion().getDisplayName() + ")");
            System.out.println("  Participants: " + tournament.getParticipants().size());
            System.out.println();
        }
    }

    // Getters for UI access
    public LeagueManager getLeagueManager() {
        return leagueManager;
    }

    public Scanner getScanner() {
        return scanner;
    }
}
