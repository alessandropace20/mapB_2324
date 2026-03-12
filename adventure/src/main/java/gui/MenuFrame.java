/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gui;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author aless
 */

public class MenuFrame extends JFrame {

    public MenuFrame() {
        setTitle("Avventura Testuale");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // centro dello schermo

        // Layout verticale centrato
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.CYAN);

        // Pulsanti
        JButton newGameBtn = createButton("Nuova Partita");
        JButton loadGameBtn = createButton("Carica Partita");
        JButton leaderboardBtn = createButton("Classifica");
        JButton exitBtn = createButton("Esci");

        // Aggiunta pulsanti al panel con spazio verticale
        panel.add(Box.createVerticalGlue());
        panel.add(newGameBtn);
        panel.add(Box.createVerticalStrut(20));
        panel.add(loadGameBtn);
        panel.add(Box.createVerticalStrut(20));
        panel.add(leaderboardBtn);
        panel.add(Box.createVerticalStrut(20));
        panel.add(exitBtn);
        panel.add(Box.createVerticalGlue());

        add(panel);

        // Azioni pulsanti
        newGameBtn.addActionListener(e -> {
            GameGUI game = new GameGUI();
            game.setVisible(true);
            this.dispose(); // chiude il menu iniziale
        });

        loadGameBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "Funzionalità Carica Partita da implementare"));
        leaderboardBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "Funzionalità Classifica da implementare"));
        exitBtn.addActionListener(e -> System.exit(0));
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT); // centra il pulsante
        button.setMaximumSize(new Dimension(200, 40)); // dimensione fissa
        button.setFocusPainted(false);
        return button;
    }
}