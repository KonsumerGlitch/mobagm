// SwissTournament.java
package com.mobagm.tournaments;

import com.mobagm.core.Enums.*;
import com.mobagm.entities.Team;
import com.mobagm.simulation.Match;
import com.mobagm.simulation.MatchResult;
import java.util.*;
import java.util.stream.Collectors;

public class SwissTournament extends com.mobagm.tournaments.Tournament {
    private Map<Team, List<Team>> playedAgainst;
    private Map<Team, Integer> wins;
    private Map<Team, Integer> losses;
    private int swissRounds;
    private List<Team> qualifiedTeams;

    public SwissTournament(String name, int swissRounds) {
        super(name, TournamentType.WORLDS); // Can be used for MSI or Worlds
        this.swissRounds = swissRounds;
        this.playedAgainst = new HashMap<>();
        this.wins = new HashMap<>();
        this.losses = new HashMap<>();
        this.qualifiedTeams = new ArrayList<>();
        this.totalRounds = swissRounds;
    }

    @Override
    public void initialize() {
        for (Team team : participants) {
            playedAgainst.put(team, new ArrayList<>());
            wins.put(team, 0);
            losses.put(team, 0);
        }

        // Handle odd number of participants with byes
        if (participants.size() % 2 == 1) {
            // Add a bye team or handle odd participants
            handleOddParticipants();
        }
    }

    private void handleOddParticipants() {
        // In a real Swiss tournament, the extra team would get a bye
        // For simulation purposes, we'll add a dummy "bye" result
        System.out.println("Warning: Odd number of participants (" + participants.size() + ")");
        System.out.println("One team will receive a bye each round");
    }

    @Override
    public void simulateRound() {
        List<Match> roundMatches = generateRoundPairings();

        for (Match match : roundMatches) {
            MatchResult result = match.simulate();
            matches.add(match);

            // Update Swiss-specific records
            Team winner = result.getWinner();
            Team loser = result.getLoser();

            wins.put(winner, wins.get(winner) + 1);
            losses.put(loser, losses.get(loser) + 1);

            playedAgainst.get(result.getTeam1()).add(result.getTeam2());
            playedAgainst.get(result.getTeam2()).add(result.getTeam1());

            updateStandings(result);
        }
    }

    private List<Match> generateRoundPairings() {
        List<Match> roundMatches = new ArrayList<>();
        List<Team> availableTeams = new ArrayList<>(participants);

        // Sort teams by record (wins-losses, then by team strength)
        availableTeams.sort((t1, t2) -> {
            int record1 = wins.get(t1) - losses.get(t1);
            int record2 = wins.get(t2) - losses.get(t2);

            if (record1 != record2) {
                return Integer.compare(record2, record1); // Higher record first
            }

            return Double.compare(t2.getTeamStrength(), t1.getTeamStrength());
        });

        // Swiss pairing algorithm
        while (availableTeams.size() >= 2) {
            Team team1 = availableTeams.remove(0);
            Team team2 = findBestOpponent(team1, availableTeams);

            if (team2 != null) {
                availableTeams.remove(team2);
                roundMatches.add(new Match(team1, team2, MatchFormat.BO3, name, currentRound));
            }
        }

        // Handle bye if odd number of teams
        if (availableTeams.size() == 1) {
            Team byeTeam = availableTeams.get(0);
            wins.put(byeTeam, wins.get(byeTeam) + 1);
            System.out.println(byeTeam.getName() + " receives a bye in round " + currentRound);
        }

        return roundMatches;
    }

    private Team findBestOpponent(Team team, List<Team> availableTeams) {
        int teamRecord = wins.get(team) - losses.get(team);

        // Try to find opponent with same record who hasn't played against this team
        for (Team opponent : availableTeams) {
            int opponentRecord = wins.get(opponent) - losses.get(opponent);

            if (teamRecord == opponentRecord && !playedAgainst.get(team).contains(opponent)) {
                return opponent;
            }
        }

        // If no same-record opponent available, find closest record
        for (Team opponent : availableTeams) {
            if (!playedAgainst.get(team).contains(opponent)) {
                return opponent;
            }
        }

        // If all have played against this team, return first available (rare case)
        return availableTeams.isEmpty() ? null : availableTeams.get(0);
    }

    @Override
    public boolean isRoundComplete() {
        return true; // Each round is complete after simulation
    }

    @Override
    public void advance() {
        currentRound++;

        if (currentRound > swissRounds) {
            completeSwissStage();
        }
    }

    private void completeSwissStage() {
        // Determine qualified teams (typically top 8 or 16)
        List<Team> sortedTeams = participants.stream()
                .sorted((t1, t2) -> {
                    int record1 = wins.get(t1) - losses.get(t1);
                    int record2 = wins.get(t2) - losses.get(t2);

                    if (record1 != record2) {
                        return Integer.compare(record2, record1);
                    }

                    return Double.compare(t2.getTeamStrength(), t1.getTeamStrength());
                })
                .collect(Collectors.toList());

        int qualificationSpots = Math.min(8, participants.size() / 2);
        qualifiedTeams = sortedTeams.subList(0, qualificationSpots);

        isComplete = true;

        if (!qualifiedTeams.isEmpty()) {
            winner = qualifiedTeams.get(0); // Top seed
        }
    }

    @Override
    public List<Team> getQualifiedTeams() {
        return new ArrayList<>(qualifiedTeams);
    }

    public Map<Team, Integer> getWins() {
        return wins;
    }

    public Map<Team, Integer> getLosses() {
        return losses;
    }

    public String getTeamRecord(Team team) {
        return wins.get(team) + "-" + losses.get(team);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Swiss Tournament: ").append(name).append("\n");
        sb.append("Round ").append(currentRound).append("/").append(swissRounds).append("\n");
        sb.append("Standings:\n");

        List<Team> sortedTeams = participants.stream()
                .sorted((t1, t2) -> {
                    int record1 = wins.get(t1) - losses.get(t1);
                    int record2 = wins.get(t2) - losses.get(t2);
                    return Integer.compare(record2, record1);
                })
                .collect(Collectors.toList());

        for (int i = 0; i < sortedTeams.size(); i++) {
            Team team = sortedTeams.get(i);
            sb.append(i + 1).append(". ").append(team.getName())
                    .append(" (").append(getTeamRecord(team)).append(")\n");
        }

        return sb.toString();
    }
}
