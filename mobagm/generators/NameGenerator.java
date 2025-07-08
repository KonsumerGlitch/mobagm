// NameGenerator.java
package com.mobagm.generators;

import java.util.*;

public class NameGenerator {
    private static final List<String> FIRST_NAMES = Arrays.asList(
            "Alex", "Jordan", "Casey", "Taylor", "Morgan", "Avery", "Riley", "Quinn", "Sage", "River",
            "Kai", "Rowan", "Phoenix", "Skyler", "Cameron", "Parker", "Emery", "Finley", "Hayden", "Peyton",
            "Blake", "Dana", "Jesse", "Kendall", "Logan", "Reese", "Remy", "Shiloh", "Tatum", "Teagan",
            "Adrian", "Ash", "Bay", "Bryce", "Cedar", "Drew", "Eden", "Ellis", "Ezra", "Gray",
            "Harper", "Indigo", "Jules", "Kris", "Lane", "Lux", "Max", "Neo", "Onyx", "Pax",
            "Raven", "Sage", "Storm", "Vale", "Wren", "Zara", "Zion", "Blaze", "Cruz", "Dash",
            "Echo", "Frost", "Hunter", "Jazz", "Knox", "Lark", "Mika", "Nova", "Orion", "Rain",
            "Scout", "Star", "Titan", "Vex", "Wave", "Zen", "Ace", "Bane", "Colt", "Dex",
            "Flux", "Ghost", "Hawk", "Ion", "Jet", "Koda", "Lynx", "Mage", "Nyx", "Pace",
            "Rogue", "Saber", "Talon", "Viper", "Wolf", "Xander", "Yuki", "Zephyr", "Cipher", "Drift",
            "Fable", "Grim", "Haven", "Ink", "Jade", "Karma", "Lunar", "Myth", "Neon", "Omen",
            "Pixel", "Quest", "Rune", "Spark", "Trace", "Unity", "Void", "Wraith", "Xenon", "Yara",
            "Zeal", "Amber", "Bolt", "Cosmo", "Delta", "Echo", "Flare", "Glitch", "Haze", "Iris",
            "Jinx", "Kilo", "Lore", "Matrix", "Nexus", "Obsidian", "Prism", "Quantum", "Radix", "Synth",
            "Tidal", "Ultra", "Vertex", "Whisper", "Xray", "Yonder", "Zenith", "Abyss", "Blade", "Comet",
            "Dusk", "Ember", "Flux", "Glacier", "Havoc", "Icarus", "Jolt", "Kinetic", "Lumen", "Mirage",
            "Nebula", "Opal", "Phantom", "Quartz", "Rift", "Shadow", "Tempest", "Umbra", "Vortex", "Wisp",
            "Xeno", "Yume", "Zara", "Astra", "Blitz", "Crimson", "Dawn", "Enigma", "Fern", "Grove",
            "Horizon", "Ivy", "Juniper", "Kestrel", "Leaf", "Moss", "North", "Ocean", "Pine", "Quill",
            "Reed", "Sage", "Thorn", "Umber", "Violet", "Willow", "Yarrow", "Zephyr", "Aspen", "Briar",
            "Clover", "Dahlia", "Elm", "Fern", "Grove", "Hazel", "Iris", "Jasmine", "Kale", "Lavender",
            "Maple", "Nettle", "Oak", "Poppy", "Quince", "Rose", "Sage", "Tulip", "Umber", "Vine",
            "Wisteria", "Xylem", "Yew", "Zinnia", "Acacia", "Basil", "Calla", "Daffodil", "Eucalyptus", "Forsythia",
            "Gardenia", "Hyacinth", "Impatiens", "Juniper", "Kudzu", "Lilac", "Magnolia", "Narcissus", "Orchid", "Peony",
            "Quaking", "Rhododendron", "Sunflower", "Thistle", "Umbrella", "Verbena", "Wisteria", "Xerophyte", "Yarrow", "Zucchini",
            "Caspian", "Dorian", "Evander", "Florian", "Gideon", "Hadrian", "Isidore", "Jasper", "Kieran", "Leander",
            "Lysander", "Maximus", "Nathaniel", "Octavius", "Phineas", "Quentin", "Rafferty", "Sebastian", "Thaddeus", "Ulysses",
            "Valerian", "Wilhelm", "Xander", "Yorick", "Zaccheus", "Alaric", "Bartholomew", "Crispin", "Damien", "Emeric",
            "Fabian", "Gideon", "Hadrian", "Ignatius", "Jasper", "Kieran", "Lucian", "Magnus", "Nathaniel", "Orlando",
            "Percival", "Quentin", "Reginald", "Sylvester", "Thaddeus", "Ulrich", "Valentine", "Wolfgang", "Xavier", "Yorick",
            "Zephyr", "Alistair", "Benedict", "Cornelius", "Desmond", "Edmund", "Ferdinand", "Gideon", "Humphrey", "Isidore"
    );

    private static final List<String> LAST_NAMES = Arrays.asList(
            "Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller", "Davis", "Rodriguez", "Martinez",
            "Hernandez", "Lopez", "Gonzalez", "Wilson", "Anderson", "Thomas", "Taylor", "Moore", "Jackson", "Martin",
            "Lee", "Perez", "Thompson", "White", "Harris", "Sanchez", "Clark", "Ramirez", "Lewis", "Robinson",
            "Walker", "Young", "Allen", "King", "Wright", "Scott", "Torres", "Nguyen", "Hill", "Flores",
            "Green", "Adams", "Nelson", "Baker", "Hall", "Rivera", "Campbell", "Mitchell", "Carter", "Roberts",
            "Gomez", "Phillips", "Evans", "Turner", "Diaz", "Parker", "Cruz", "Edwards", "Collins", "Reyes",
            "Stewart", "Morris", "Morales", "Murphy", "Cook", "Rogers", "Gutierrez", "Ortiz", "Morgan", "Cooper",
            "Peterson", "Bailey", "Reed", "Kelly", "Howard", "Ramos", "Kim", "Cox", "Ward", "Richardson",
            "Watson", "Brooks", "Chavez", "Wood", "James", "Bennett", "Gray", "Mendoza", "Ruiz", "Hughes",
            "Price", "Alvarez", "Castillo", "Sanders", "Patel", "Myers", "Long", "Ross", "Foster", "Jimenez",
            "Powell", "Jenkins", "Perry", "Russell", "Sullivan", "Bell", "Coleman", "Butler", "Henderson", "Barnes",
            "Gonzales", "Fisher", "Vasquez", "Simmons", "Romero", "Jordan", "Patterson", "Alexander", "Hamilton", "Graham",
            "Reynolds", "Griffin", "Wallace", "Moreno", "West", "Cole", "Hayes", "Bryant", "Herrera", "Gibson",
            "Ellis", "Tran", "Medina", "Aguilar", "Stevens", "Murray", "Ford", "Castro", "Marshall", "Owens",
            "Harrison", "Fernandez", "Mcdonald", "Woods", "Washington", "Kennedy", "Wells", "Vargas", "Henry", "Chen",
            "Freeman", "Webb", "Tucker", "Guzman", "Burns", "Crawford", "Olson", "Simpson", "Porter", "Hunter",
            "Gordon", "Mendez", "Silva", "Shaw", "Snyder", "Mason", "Dixon", "Munoz", "Hunt", "Hicks",
            "Holmes", "Palmer", "Wagner", "Black", "Robertson", "Boyd", "Rose", "Stone", "Salazar", "Fox",
            "Warren", "Mills", "Meyer", "Rice", "Schmidt", "Garza", "Daniels", "Ferguson", "Nichols", "Stephens",
            "Soto", "Weaver", "Ryan", "Gardner", "Payne", "Grant", "Dunn", "Kelley", "Spencer", "Hawkins",
            "Arnold", "Pierce", "Vazquez", "Hansen", "Peters", "Santos", "Hart", "Bradley", "Knight", "Elliott",
            "Cunningham", "Duncan", "Burke", "Huber", "Lane", "Hoffman", "Warren", "Harper", "Fox", "Henderson",
            "Vega", "Burke", "Caldwell", "Wallace", "Lamb", "Barber", "Cain", "Marsh", "Holt", "Delgado",
            "Carlson", "Stokes", "Goodwin", "Farmer", "Schneider", "Walters", "Lawson", "Palmer", "Wolfe", "Schneider",
            "Vega", "Greer", "Horton", "Parsons", "Waters", "Strickland", "Osborne", "Maxwell", "Hodges", "Garrett",
            "Sims", "Oneal", "Pittman", "Cochran", "Crosby", "Vance", "Park", "Barry", "Russell", "Wilkins",
            "Bowen", "Flores", "Ramsey", "Lambert", "Abbott", "Reeves", "Byrd", "Dawson", "Hardy", "Steele",
            "Curry", "Powers", "Schultz", "Barker", "Gould", "Greene", "Mccarthy", "Underwood", "Hogan", "Mckinney",
            "Costello", "Wise", "Mcbride", "Mccormick", "Bowers", "Todd", "Rowe", "Mckinney", "Zimmerman", "Mccarthy",
            "Moody", "Vang", "Frazier", "Deleon", "Huff", "Malone", "Durham", "Carlson", "Benson", "Ingram"
    );

    private static Set<String> usedNames = new HashSet<>();

    public static String generatePlayerName() {
        String fullName;
        do {
            String firstName = FIRST_NAMES.get(new Random().nextInt(FIRST_NAMES.size()));
            String lastName = LAST_NAMES.get(new Random().nextInt(LAST_NAMES.size()));
            fullName = firstName + " " + lastName;
        } while (usedNames.contains(fullName));

        usedNames.add(fullName);
        return fullName;
    }

    public static void resetUsedNames() {
        usedNames.clear();
    }

    public static int getUsedNameCount() {
        return usedNames.size();
    }
}
