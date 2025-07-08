package com.mobagm.ui;

import com.mobagm.core.LeagueManager;
import com.mobagm.core.Enums.Region;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class GUIApplication extends JFrame {
    private final LeagueManager leagueManager;
    private final CardLayout cardLayout;
    private final JPanel mainPanel;

    public GUIApplication() {
        super("MOBAGM – MOBA General Manager");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1024, 768);
        setLocationRelativeTo(null);

        leagueManager = new LeagueManager();
        cardLayout    = new CardLayout();
        mainPanel     = new JPanel(cardLayout);

        initMenuBar();
        initPanels();

        add(mainPanel);
        setVisible(true);
    }

    private void initMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // Simulation Menu
        JMenu simMenu = new JMenu("Simulation");
        simMenu.add(new JMenuItem(new AbstractAction("Start") {
            public void actionPerformed(ActionEvent e) {
                leagueManager.startSimulation();
                JOptionPane.showMessageDialog(null,
                        "Simulation started for Year " + leagueManager.getCurrentYear());
            }
        }));
        simMenu.add(new JMenuItem(new AbstractAction("Pause") {
            public void actionPerformed(ActionEvent e) {
                leagueManager.pauseSimulation();
                JOptionPane.showMessageDialog(null, "Simulation paused.");
            }
        }));
        menuBar.add(simMenu);

        // View Menu
        JMenu viewMenu = new JMenu("View");
        viewMenu.add(new JMenuItem(new AbstractAction("Standings") {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "standings");
            }
        }));
        viewMenu.add(new JMenuItem(new AbstractAction("Players") {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "players");
            }
        }));
        viewMenu.add(new JMenuItem(new AbstractAction("Teams") {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "teams");
            }
        }));
        menuBar.add(viewMenu);

        // Exit
        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(new JMenuItem(new AbstractAction("Exit") {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        }));

        setJMenuBar(menuBar);
    }

    private void initPanels() {
        // Home panel
        mainPanel.add(new JLabel("Welcome to MOBAGM!", SwingConstants.CENTER), "home");

        // Standings panel
        JPanel standPanel = new JPanel(new BorderLayout());
        JComboBox<Region> standRegions = new JComboBox<>(Region.values());
        JTextArea standText = new JTextArea();
        standText.setEditable(false);
        standRegions.addActionListener(e -> {
            Region r = (Region) standRegions.getSelectedItem();
            StringBuilder sb = new StringBuilder();
            leagueManager.getRegionalLeagues().get(r).printStandings();
            standText.setText(sb.toString());
        });
        standPanel.add(standRegions, BorderLayout.NORTH);
        standPanel.add(new JScrollPane(standText), BorderLayout.CENTER);
        mainPanel.add(standPanel, "standings");

        // Players panel
        JPanel playerPanel = new JPanel(new BorderLayout());
        JComboBox<Region> playerRegions = new JComboBox<>(Region.values());
        JTextArea playerText = new JTextArea();
        playerText.setEditable(false);
        playerRegions.addActionListener(e -> {
            Region r = (Region) playerRegions.getSelectedItem();
            StringBuilder sb = new StringBuilder("Top Players in " + r + "\n");
            leagueManager.getTopPlayersByRegion(r, 10)
                    .forEach(p -> sb.append(p).append("\n"));
            playerText.setText(sb.toString());
        });
        playerPanel.add(playerRegions, BorderLayout.NORTH);
        playerPanel.add(new JScrollPane(playerText), BorderLayout.CENTER);
        mainPanel.add(playerPanel, "players");

        // Teams panel
        JPanel teamPanel = new JPanel(new BorderLayout());
        JComboBox<Region> teamRegions = new JComboBox<>(Region.values());
        JTextArea teamText = new JTextArea();
        teamText.setEditable(false);
        teamRegions.addActionListener(e -> {
            Region r = (Region) teamRegions.getSelectedItem();
            StringBuilder sb = new StringBuilder("Teams in " + r + "\n");
            leagueManager.getRegionalLeagues().get(r)
                    .getChampionsTeams()
                    .forEach(t -> sb.append(t).append("\n"));
            teamText.setText(sb.toString());
        });
        teamPanel.add(teamRegions, BorderLayout.NORTH);
        teamPanel.add(new JScrollPane(teamText), BorderLayout.CENTER);
        mainPanel.add(teamPanel, "teams");

        cardLayout.show(mainPanel, "home");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GUIApplication::new);
    }
}
