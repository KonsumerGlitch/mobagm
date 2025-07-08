// src/com/mobagm/entities/Player.java
package com.mobagm.entities;

import com.mobagm.core.Enums.Role;
import com.mobagm.core.Enums.PlayerTrait;
import java.util.*;

public class Player {
    private String name;
    private Role role;
    private int age;
    private double mechanical;
    private double strategic;
    private double softSkill;
    private double overall;
    private double potential;
    private Set<PlayerTrait> traits;
    private Team currentTeam;
    private int salary;
    private int contractLength;
    private Map<String, Double> championMastery;
    private PlayerStatistics statistics;
    private boolean isRetired;

    // Development curve parameters
    private double peakAge;
    private double declineRate;
    private double growthRate;

    public Player(String name, Role role, int age) {
        this.name = name;
        this.role = role;
        this.age  = age;
        this.traits = new HashSet<>();
        this.championMastery = new HashMap<>();
        this.statistics     = new PlayerStatistics();
        this.isRetired      = false;

        initializeAttributes();
        initializeTraits();
        initializeDevelopmentCurve();
        calculateOverall();
        calculatePotential();
    }

    private void initializeAttributes() {
        Random random = new Random();
        switch (role) {
            case TOP:
                mechanical = clamp(random.nextGaussian() * 15 + 65);
                strategic  = clamp(random.nextGaussian() * 15 + 60);
                softSkill  = clamp(random.nextGaussian() * 15 + 55);
                break;
            case JUNGLE:
                mechanical = clamp(random.nextGaussian() * 15 + 60);
                strategic  = clamp(random.nextGaussian() * 15 + 70);
                softSkill  = clamp(random.nextGaussian() * 15 + 65);
                break;
            case MID:
                mechanical = clamp(random.nextGaussian() * 15 + 70);
                strategic  = clamp(random.nextGaussian() * 15 + 65);
                softSkill  = clamp(random.nextGaussian() * 15 + 60);
                break;
            case ADC:
                mechanical = clamp(random.nextGaussian() * 15 + 75);
                strategic  = clamp(random.nextGaussian() * 15 + 55);
                softSkill  = clamp(random.nextGaussian() * 15 + 50);
                break;
            case SUPPORT:
                mechanical = clamp(random.nextGaussian() * 15 + 50);
                strategic  = clamp(random.nextGaussian() * 15 + 65);
                softSkill  = clamp(random.nextGaussian() * 15 + 75);
                break;
        }
    }

    private double clamp(double v) {
        return Math.max(30, Math.min(100, v));
    }

    private void initializeTraits() {
        Random random = new Random();
        int count = random.nextInt(3);
        List<PlayerTrait> all = Arrays.asList(PlayerTrait.values());
        Collections.shuffle(all, random);
        for (int i = 0; i < count; i++) traits.add(all.get(i));
    }

    private void initializeDevelopmentCurve() {
        Random random = new Random();
        peakAge     = 22 + random.nextGaussian() * 2;
        declineRate = 0.02 + random.nextGaussian() * 0.01;
        growthRate  = 0.05 + random.nextGaussian() * 0.02;
    }

    private void calculateOverall() {
        double base = (mechanical + strategic + softSkill) / 3.0;
        double bonus = 0;
        for (PlayerTrait t : traits) {
            bonus += t.getMechanicalBonus() * mechanical
                    + t.getStrategicBonus()   * strategic
                    + t.getSoftSkillBonus()   * softSkill;
        }
        overall = Math.min(100, base + bonus);
    }

    private void calculatePotential() {
        Random r = new Random();
        double factor = Math.max(0, (30 - age) / 14.0);
        potential = Math.min(100, overall + r.nextGaussian() * 10 * factor);
    }

    public void developPlayer() {
        if (age >= 30) {
            isRetired = true;
            return;
        }
        age++;
        double factor = (age <= peakAge)
                ? growthRate * (1 - age / peakAge)
                : -declineRate * Math.pow((age - peakAge) / 5.0, 2);
        Random r = new Random();
        mechanical = clamp(mechanical + factor * 10 + r.nextGaussian() * 2);
        strategic  = clamp(strategic  + factor * 10 + r.nextGaussian() * 2);
        softSkill  = clamp(softSkill  + factor * 10 + r.nextGaussian() * 2);
        calculateOverall();
        if (traits.contains(PlayerTrait.LATE_BLOOMER) && age > 25) {
            mechanical = clamp(mechanical + 2);
            strategic  = clamp(strategic  + 2);
            softSkill  = clamp(softSkill  + 2);
            calculateOverall();
        }
    }

    public double getMatchPerformance() {
        Random r = new Random();
        double perf = overall;
        for (PlayerTrait t : traits) {
            if (t == PlayerTrait.CLUTCH_PERFORMER && r.nextDouble() < 0.3) {
                perf *= 1.2;
            } else if (t == PlayerTrait.CONSISTENT_PERFORMER) {
                perf *= 1.1;
            } else if (t == PlayerTrait.PRESSURE_RESISTANT) {
                perf *= 1.05;
            }
        }
        return Math.min(100, perf + r.nextGaussian() * 15);
    }

    public void updateChampionMastery(String champ, double perf) {
        championMastery.merge(champ, 50 + (perf - 50) * 0.1, Double::sum);
    }

    // ** New getters for Game.java **
    public double getMechanical() { return mechanical; }
    public double getStrategic()  { return strategic; }
    public double getSoftSkill()  { return softSkill; }

    // Remaining getters/setters...
    public String getName()             { return name; }
    public Role getRole()               { return role; }
    public int getAge()                 { return age; }
    public double getOverall()          { return overall; }
    public double getPotential()        { return potential; }
    public Set<PlayerTrait> getTraits() { return traits; }
    public Team getCurrentTeam()        { return currentTeam; }
    public void setCurrentTeam(Team t)  { this.currentTeam = t; }
    public int getSalary()              { return salary; }
    public void setSalary(int s)        { this.salary = s; }
    public int getContractLength()      { return contractLength; }
    public void setContractLength(int c){ this.contractLength = c; }
    public PlayerStatistics getStatistics(){ return statistics; }
    public boolean isRetired()          { return isRetired; }
    public void setRetired(boolean r)   { this.isRetired = r; }

    @Override
    public String toString() {
        return String.format("%s (%s) Age:%d OVR:%.1f POT:%.1f",
                name, role, age, overall, potential);
    }
}
