// Enums.java
package com.mobagm.core;

public class Enums {
    public enum Region {
        LTA("Americas"), LEC("EMEA"), LCK("Korea"), LPL("China"), LCP("Pacific");

        private final String displayName;

        Region(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public enum Split {
        WINTER, SPRING, SUMMER
    }

    public enum Role {
        TOP, JUNGLE, MID, ADC, SUPPORT
    }

    public enum TournamentType {
        REGIONAL_SPLIT, MSI, WORLDS
    }

    public enum MatchFormat {
        BO1, BO3, BO5
    }

    public enum League {
        CHAMPIONS_SERIES, CHALLENGERS
    }

    public enum PlayerTrait {
        CLUTCH_PERFORMER("Clutch Performer", 0.15, 0.0, 0.1),
        VISION_MASTER("Vision Master", 0.0, 0.2, 0.05),
        LATE_BLOOMER("Late Bloomer", 0.1, 0.05, 0.15),
        EARLY_GAME_SPECIALIST("Early Game Specialist", 0.12, 0.08, 0.0),
        TEAM_CAPTAIN("Team Captain", 0.05, 0.1, 0.2),
        MECHANICAL_PRODIGY("Mechanical Prodigy", 0.25, 0.0, 0.0),
        STRATEGIC_MIND("Strategic Mind", 0.0, 0.25, 0.1),
        VERSATILE_PLAYER("Versatile Player", 0.08, 0.08, 0.08),
        PRESSURE_RESISTANT("Pressure Resistant", 0.1, 0.05, 0.15),
        ROOKIE_SENSATION("Rookie Sensation", 0.15, 0.0, 0.0),
        VETERAN_WISDOM("Veteran Wisdom", 0.0, 0.15, 0.1),
        LANE_DOMINATOR("Lane Dominator", 0.2, 0.0, 0.0),
        MACRO_GENIUS("Macro Genius", 0.0, 0.2, 0.05),
        CHAMPION_SPECIALIST("Champion Specialist", 0.18, 0.0, 0.0),
        CONSISTENT_PERFORMER("Consistent Performer", 0.05, 0.1, 0.1),
        CARRY_POTENTIAL("Carry Potential", 0.2, 0.0, 0.0),
        SUPPORT_ANCHOR("Support Anchor", 0.0, 0.1, 0.15),
        SHOTCALLER("Shotcaller", 0.0, 0.15, 0.1),
        ADAPTABLE("Adaptable", 0.1, 0.1, 0.1),
        INTERNATIONAL_EXPERIENCE("International Experience", 0.05, 0.1, 0.15),
        SYNERGY_BUILDER("Synergy Builder", 0.0, 0.1, 0.2),
        CLUTCH_ROOKIE("Clutch Rookie", 0.12, 0.0, 0.08),
        STRATEGIC_VETERAN("Strategic Veteran", 0.05, 0.2, 0.1),
        EMOTIONAL_STABILITY("Emotional Stability", 0.0, 0.05, 0.2);

        private final String displayName;
        private final double mechanicalBonus;
        private final double strategicBonus;
        private final double softSkillBonus;

        PlayerTrait(String displayName, double mechanicalBonus, double strategicBonus, double softSkillBonus) {
            this.displayName = displayName;
            this.mechanicalBonus = mechanicalBonus;
            this.strategicBonus = strategicBonus;
            this.softSkillBonus = softSkillBonus;
        }

        public String getDisplayName() { return displayName; }
        public double getMechanicalBonus() { return mechanicalBonus; }
        public double getStrategicBonus() { return strategicBonus; }
        public double getSoftSkillBonus() { return softSkillBonus; }
    }
}
