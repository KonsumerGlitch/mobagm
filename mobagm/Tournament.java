// Tournament.java
package com.mobagm.tournaments;

import com.mobagm.core.Enums.*;
import com.mobagm.entities.Team;
import com.mobagm.simulation.Match;
import com.mobagm.simulation.MatchResult;
import java.util.*;

public abstract class Tournament {
    protected String name;
    protected TournamentType type;
    protected List<Team> participants;
    protected List<Match> matches;
    protected Map<Team, Integer> standings;
    protected Team winner;
    protected boolean isComplete;
    protected Date startDate;
    protected Date endDate;
    protected int currentRound;
    protected int totalRounds;

    public Tournament(String name, TournamentType type) {
        this.name = name;
        this.type = type;
        this.participants = new ArrayList<>();
        this.matches = new ArrayList<>();
        this.standings = new HashMap<>();
        this.isComplete = false;
        this.startDate = new Date();
        this.currentRound = 1;
    }

    public abstract void initialize();
    public abstract void simulateRound();
    public abstract boolean isRoundComplete();
    public abstract void advance();
    public abstract List<Team> getQualifiedTeams();

    public void addParticipant(Team team) {
        if (!participants.contains(team)) {
            participants.add(team);
            standings.put(team, 0);
        }
    }

    public void removeParticipant(Team team) {
        participants.remove(team);
        standings.remove(team);
    }

    public void simulateTournament() {
        initialize();

        while (!isComplete) {
            simulateRound();
            if (isRoundComplete()) {
                advance();
            }
        }

        endDate = new Date();
    }

    protected void updateStandings(MatchResult result) {
        Team winner = result.getWinner();
        Team loser = result.getLoser();

        standings.put(winner, standings.get(winner) + getWinPoints());
        standings.put(loser, standings.get(loser) + getLossPoints());
    }

    protected int getWinPoints() {
        return 3; // Standard win points
    }

    protected int getLossPoints() {
        return 0; // Standard loss points
    }

    protected List<Team> getTeamsByStandings() {
        return participants.stream()
                .sorted((t1, t2) -> Integer.compare(standings.get(t2), standings.get(t1)))
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    // Getters
    public String getName() { return name; }
    public TournamentType getType() { return type; }
    public List<Team> getParticipants() { return participants; }
    public List<Match> getMatches() { return matches; }
    public Map<Team, Integer> getStandings() { return standings; }
    public Team getWinner() { return winner; }
    public boolean isComplete() { return isComplete; }
    public Date getStartDate() { return startDate; }
    public Date getEndDate() { return endDate; }
    public int getCurrentRound() { return currentRound; }
    public int getTotalRounds() { return totalRounds; }
}
