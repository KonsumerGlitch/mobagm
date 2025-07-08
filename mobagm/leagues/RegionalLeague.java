// src/com/mobagm/leagues/RegionalLeague.java
package com.mobagm.leagues;

import com.mobagm.core.Enums.Region;
import com.mobagm.core.Enums.Split;
import com.mobagm.core.Enums.League;
import com.mobagm.core.Enums.MatchFormat;
import com.mobagm.entities.Team;
import com.mobagm.simulation.Match;
import com.mobagm.simulation.MatchResult;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RegionalLeague {
    private final Region region;
    private Split currentSplit;
    private int currentYear;
    private final List<Team> championsTeams;
    private final List<Team> challengersTeams;
    private final List<Match> regularSeasonMatches;
    private final List<Match> playoffMatches;
    private final Map<Team, LeagueRecord> records;
    private Team splitWinner;
    private boolean isPlayoffsComplete;
    private final List<Team> promotedTeams;
    private final List<Team> relegatedTeams;

    public RegionalLeague(Region region) {
        this.region             = region;
        this.currentSplit       = Split.WINTER;
        this.currentYear        = 2024;
        this.championsTeams     = new ArrayList<>();
        this.challengersTeams   = new ArrayList<>();
        this.regularSeasonMatches = new ArrayList<>();
        this.playoffMatches     = new ArrayList<>();
        this.records            = new HashMap<>();
        this.promotedTeams      = new ArrayList<>();
        this.relegatedTeams     = new ArrayList<>();
        this.isPlayoffsComplete = false;
    }

    public void initializeLeague() {
        generateChampionsTeams();
        generateChallengersTeams();
        initializeRecords();
    }

    private void generateChampionsTeams() {
        String[] names = getRegionalTeamNames();
        for (int i = 0; i < 10; i++) {
            String name = region.name() + " " + names[i % names.length];
            Team team = new Team(name, region, League.CHAMPIONS_SERIES);
            championsTeams.add(team);
            generateTeamRoster(team);
        }
    }

    private void generateChallengersTeams() {
        String[] names = getRegionalTeamNames();
        for (int i = 0; i < 8; i++) {
            String name = region.name() + " Challengers " + (i + 1);
            Team team = new Team(name, region, League.CHALLENGERS);
            challengersTeams.add(team);
            generateTeamRoster(team);
        }
    }

    private String[] getRegionalTeamNames() {
        switch (region) {
            case LTA: return new String[]{"Cloud9","Team Liquid","TSM","100 Thieves","FlyQuest","Evil Geniuses","Dignitas","Immortals","CLG","Golden Guardians"};
            case LEC: return new String[]{"G2","Fnatic","MAD Lions","Rogue","Vitality","BDS","Excel","Heretics","Giants","Astralis"};
            case LCK: return new String[]{"T1","Gen.G","DRX","KT Rolster","Hanwha Life","DPlus KIA","Kwangdong Freecs","Liiv SANDBOX","Nongshim RedForce","BRO"};
            case LPL: return new String[]{"JDG","BLG","WBG","LNG","TES","EDG","FPX","IG","RNG","WE","AL","OMG","LGD","UP","TT","NIP"};
            case LCP: return new String[]{"PSG Talon","DetonationFocusMe","ZETA Division","Crazy Raccoon","ORDER","Chiefs","Pentanet.GG","Legacy","Dire Wolves","Peace"};
            default:  return new String[]{"Alpha","Beta","Gamma","Delta","Epsilon"};
        }
    }

    private void generateTeamRoster(Team team) {
        for (var role : com.mobagm.core.Enums.Role.values()) {
            String playerName = com.mobagm.generators.NameGenerator.generatePlayerName();
            var player = new com.mobagm.entities.Player(playerName, role, 18 + new Random().nextInt(10));
            player.setSalary(50_000 + new Random().nextInt(200_000));
            player.setContractLength(1 + new Random().nextInt(3));
            team.signPlayer(player);
        }
    }

    private void initializeRecords() {
        Stream.concat(championsTeams.stream(), challengersTeams.stream())
                .forEach(team -> records.put(team, new LeagueRecord(team)));
    }

    public void simulateSplit() {
        simulateRegularSeason();
        simulatePlayoffs();
        if (currentSplit == Split.SUMMER) {
            simulatePromotionRelegation();
        }
        advanceToNextSplit();
    }

    private void simulateRegularSeason() {
        simulateRoundRobin(championsTeams);
        simulateRoundRobin(challengersTeams);
    }

    private void simulateRoundRobin(List<Team> teams) {
        for (int i = 0; i < teams.size(); i++) {
            for (int j = i + 1; j < teams.size(); j++) {
                Team t1 = teams.get(i), t2 = teams.get(j);
                simulateMatch(t1, t2, MatchFormat.BO1);
                simulateMatch(t2, t1, MatchFormat.BO1);
            }
        }
    }

    private void simulateMatch(Team a, Team b, MatchFormat format) {
        Match m = new Match(a, b, format);
        MatchResult r = m.simulate();
        regularSeasonMatches.add(m);
        updateRecord(r);
    }

    private void updateRecord(MatchResult r) {
        records.get(r.getWinner()).addWin();
        records.get(r.getLoser()).addLoss();
        r.getTeam1().getStatistics().updateSplitRecord(currentSplit.name(), r.getWinner() == r.getTeam1());
        r.getTeam2().getStatistics().updateSplitRecord(currentSplit.name(), r.getWinner() == r.getTeam2());
    }

    private void simulatePlayoffs() {
        List<Team> top6 = getTopTeams(championsTeams, 6);
        List<Team> semis = new ArrayList<>();

        semis.add(playoffMatch(top6.get(2), top6.get(5), MatchFormat.BO3));
        semis.add(playoffMatch(top6.get(3), top6.get(4), MatchFormat.BO3));

        Team f1 = playoffMatch(top6.get(0), semis.get(0), MatchFormat.BO5);
        Team f2 = playoffMatch(top6.get(1), semis.get(1), MatchFormat.BO5);
        splitWinner = playoffMatch(f1, f2, MatchFormat.BO5);

        splitWinner.getStatistics().setChampionshipsWon(
                splitWinner.getStatistics().getChampionshipsWon() + 1
        );
        isPlayoffsComplete = true;
    }

    private Team playoffMatch(Team a, Team b, MatchFormat fmt) {
        Match m = new Match(a, b, fmt);
        Team winner = m.simulate().getWinner();
        playoffMatches.add(m);
        return winner;
    }

    private void simulatePromotionRelegation() {
        List<Team> bottom = getBottomTeams(championsTeams, 2);
        List<Team> top     = getTopTeams(challengersTeams, 2);

        for (Team t : bottom) {
            t.setLeague(League.CHALLENGERS);
            championsTeams.remove(t);
            challengersTeams.add(t);
            relegatedTeams.add(t);
        }
        for (Team t : top) {
            t.setLeague(League.CHAMPIONS_SERIES);
            challengersTeams.remove(t);
            championsTeams.add(t);
            promotedTeams.add(t);
        }
    }

    private List<Team> getTopTeams(List<Team> teams, int n) {
        return teams.stream()
                .sorted((a,b) -> Integer.compare(records.get(b).getWins(), records.get(a).getWins()))
                .limit(n)
                .collect(Collectors.toList());
    }

    private List<Team> getBottomTeams(List<Team> teams, int n) {
        return teams.stream()
                .sorted((a,b) -> Integer.compare(records.get(a).getWins(), records.get(b).getWins()))
                .limit(n)
                .collect(Collectors.toList());
    }

    private void advanceToNextSplit() {
        currentSplit = switch (currentSplit) {
            case WINTER -> Split.SPRING;
            case SPRING -> Split.SUMMER;
            case SUMMER -> { currentYear++; yield Split.WINTER; }
        };
        regularSeasonMatches.clear();
        playoffMatches.clear();
        isPlayoffsComplete = false;
        splitWinner       = null;
        records.values().forEach(LeagueRecord::reset);
        championsTeams.forEach(Team::developRoster);
        challengersTeams.forEach(Team::developRoster);
        promotedTeams.clear();
        relegatedTeams.clear();
    }

    public List<Team> getTopTeamsForInternational(int n) {
        return championsTeams.stream()
                .sorted((a,b) -> Double.compare(b.getTeamStrength(), a.getTeamStrength()))
                .limit(n)
                .collect(Collectors.toList());
    }

    public void printStandings() {
        System.out.println("\n=== " + region.getDisplayName() + " Champions Series ===");
        printTable(championsTeams);
        System.out.println("\n=== " + region.getDisplayName() + " Challengers ===");
        printTable(challengersTeams);
    }

    private void printTable(List<Team> list) {
        list.stream()
                .sorted((a,b) -> Integer.compare(records.get(b).getWins(), records.get(a).getWins()))
                .forEachOrdered((team)-> {
                    LeagueRecord r = records.get(team);
                    System.out.printf("%-20s : %2dW-%2dL (%.1f%%)%n",
                            team.getName(), r.getWins(), r.getLosses(), r.getWinRate()*100);
                });
    }

    // Getters for integration
    public Region getRegion()                { return region; }
    public Split getCurrentSplit()          { return currentSplit; }
    public int getCurrentYear()             { return currentYear; }
    public Team getSplitWinner()            { return splitWinner; }
    public List<Team> getChampionsTeams()   { return Collections.unmodifiableList(championsTeams); }
    public List<Team> getChallengersTeams() { return Collections.unmodifiableList(challengersTeams); }
    public List<Team> getPromotedTeams()    { return Collections.unmodifiableList(promotedTeams); }
    public List<Team> getRelegatedTeams()   { return Collections.unmodifiableList(relegatedTeams); }

    public static class LeagueRecord {
        private final Team team;
        private int wins, losses;
        public LeagueRecord(Team t) { this.team = t; }
        public void addWin()        { wins++; }
        public void addLoss()       { losses++; }
        public void reset()         { wins = 0; losses = 0; }
        public int getWins()        { return wins; }
        public int getLosses()      { return losses; }
        public double getWinRate()  {
            int total = wins + losses;
            return total > 0 ? (double) wins / total : 0.0;
        }
    }
}
