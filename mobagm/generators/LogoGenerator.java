// LogoGenerator.java
package com.mobagm.generators;

import com.mobagm.core.Enums.Region;
import java.util.*;

public class LogoGenerator {
    private static final String[] COLORS = {
            "#FF6B6B", "#4ECDC4", "#45B7D1", "#96CEB4", "#FFEAA7", "#DDA0DD", "#98D8C8",
            "#F06292", "#AED581", "#FFB74D", "#E57373", "#64B5F6", "#81C784", "#FFD54F",
            "#FF8A65", "#BA68C8", "#4DB6AC", "#FFF176", "#A1C4FD", "#C2E9FB", "#FFE066",
            "#FF9A9E", "#FECFEF", "#FECFEF", "#C1E8FF", "#FFE1E6", "#E0C3FC", "#F093FB",
            "#F5576C", "#4DD0E1", "#9575CD", "#F06292", "#7986CB", "#81C784", "#DCE775"
    };

    private static final String[] SHAPES = {
            "shield", "circle", "hexagon", "diamond", "star", "triangle", "square"
    };

    private static final String[] SYMBOLS = {
            "⚔️", "🛡️", "👑", "⚡", "🔥", "❄️", "🌟", "💎", "🦅", "🐺", "🦁", "🐉",
            "🌊", "⚓", "🗡️", "🏹", "🎯", "💫", "🌙", "☄️", "🌀", "💥", "🔱", "⚜️"
    };

    public static String generateTeamLogo(String teamName, Region region) {
        Random random = new Random();

        String primaryColor = COLORS[random.nextInt(COLORS.length)];
        String secondaryColor = COLORS[random.nextInt(COLORS.length)];
        String shape = SHAPES[random.nextInt(SHAPES.length)];
        String symbol = SYMBOLS[random.nextInt(SYMBOLS.length)];

        // Generate SVG logo
        StringBuilder svg = new StringBuilder();
        svg.append("<svg width=\"100\" height=\"100\" xmlns=\"http://www.w3.org/2000/svg\">");

        // Background shape
        switch (shape) {
            case "shield":
                svg.append("<path d=\"M50 10 L80 30 L80 70 L50 90 L20 70 L20 30 Z\" ")
                        .append("fill=\"").append(primaryColor).append("\" stroke=\"").append(secondaryColor).append("\" stroke-width=\"2\"/>");
                break;
            case "circle":
                svg.append("<circle cx=\"50\" cy=\"50\" r=\"40\" fill=\"").append(primaryColor)
                        .append("\" stroke=\"").append(secondaryColor).append("\" stroke-width=\"3\"/>");
                break;
            case "hexagon":
                svg.append("<polygon points=\"50,10 80,30 80,70 50,90 20,70 20,30\" ")
                        .append("fill=\"").append(primaryColor).append("\" stroke=\"").append(secondaryColor).append("\" stroke-width=\"2\"/>");
                break;
            case "diamond":
                svg.append("<polygon points=\"50,10 80,50 50,90 20,50\" ")
                        .append("fill=\"").append(primaryColor).append("\" stroke=\"").append(secondaryColor).append("\" stroke-width=\"2\"/>");
                break;
            case "star":
                svg.append("<polygon points=\"50,10 55,35 80,35 62,52 70,77 50,65 30,77 38,52 20,35 45,35\" ")
                        .append("fill=\"").append(primaryColor).append("\" stroke=\"").append(secondaryColor).append("\" stroke-width=\"2\"/>");
                break;
            case "triangle":
                svg.append("<polygon points=\"50,10 80,80 20,80\" ")
                        .append("fill=\"").append(primaryColor).append("\" stroke=\"").append(secondaryColor).append("\" stroke-width=\"2\"/>");
                break;
            case "square":
                svg.append("<rect x=\"15\" y=\"15\" width=\"70\" height=\"70\" ")
                        .append("fill=\"").append(primaryColor).append("\" stroke=\"").append(secondaryColor).append("\" stroke-width=\"3\"/>");
                break;
        }

        // Team initials or symbol
        String initials = getTeamInitials(teamName);
        svg.append("<text x=\"50\" y=\"58\" font-family=\"Arial, sans-serif\" font-size=\"24\" font-weight=\"bold\" ")
                .append("text-anchor=\"middle\" fill=\"").append(secondaryColor).append("\">")
                .append(initials).append("</text>");

        // Region indicator
        svg.append("<text x=\"50\" y=\"95\" font-family=\"Arial, sans-serif\" font-size=\"8\" ")
                .append("text-anchor=\"middle\" fill=\"").append(secondaryColor).append("\">")
                .append(region.name()).append("</text>");

        svg.append("</svg>");

        // Convert to data URL
        String svgData = svg.toString();
        return "data:image/svg+xml;base64," + Base64.getEncoder().encodeToString(svgData.getBytes());
    }

    private static String getTeamInitials(String teamName) {
        String[] words = teamName.split(" ");
        StringBuilder initials = new StringBuilder();

        for (String word : words) {
            if (!word.isEmpty()) {
                initials.append(word.charAt(0));
            }
        }

        return initials.toString().toUpperCase();
    }
}
