package gui;

import adventure.Engine;
import impl.FireHouseGame;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class GameGUI {

    private Engine engine;
    private JTextArea output;
    private JTextField input;
    private JLabel imageLabel;

    public GameGUI() {

        engine = new Engine(new FireHouseGame());

        JFrame frame = new JFrame("Avventura Testuale");
        frame.setSize(700, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        /*
        ---------------- MENU BAR ----------------
        */

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("File");

        JMenuItem salva = new JMenuItem("Salva");
        JMenuItem esci = new JMenuItem("Esci");

        menu.add(salva);
        menu.add(esci);
        menuBar.add(menu);

        frame.setJMenuBar(menuBar);

        /*
        ---------------- AREA IMMAGINE ----------------
        */

        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageLabel.setPreferredSize(new Dimension(700,150));

        frame.add(imageLabel, BorderLayout.NORTH);

        /*
        ---------------- AREA TESTO ----------------
        */

        output = new JTextArea();
        output.setEditable(false);
        JScrollPane scroll = new JScrollPane(output);

        frame.add(scroll, BorderLayout.CENTER);

        /*
        ---------------- INPUT COMANDI ----------------
        */

        input = new JTextField();
        JButton send = new JButton("Invia");

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.add(input, BorderLayout.CENTER);
        bottom.add(send, BorderLayout.EAST);

        frame.add(bottom, BorderLayout.SOUTH);

        /*
        ---------------- MESSAGGIO INIZIALE ----------------
        */

        output.append(engine.getInitialMessage());

        /*
        ---------------- EVENTI ----------------
        */

        send.addActionListener(e -> sendCommand());
        input.addActionListener(e -> sendCommand());

        esci.addActionListener(e -> System.exit(0));

        salva.addActionListener(e -> salvaPartita());

        frame.setVisible(true);
    }

    /*
    ---------------- INVIO COMANDO ----------------
    */

    private void sendCommand() {

        String command = input.getText();

        output.append("\n?> " + command + "\n");

        String response = engine.processCommand(command);

        output.append(response);

        input.setText("");
    }

    /*
    ---------------- SALVATAGGIO ----------------
    */

    private void salvaPartita() {

        JFileChooser fileChooser = new JFileChooser();
        int scelta = fileChooser.showSaveDialog(null);

        if (scelta == JFileChooser.APPROVE_OPTION) {

            File file = fileChooser.getSelectedFile();

            try (PrintWriter writer = new PrintWriter(file)) {

                writer.write(output.getText());

            } catch (Exception e) {

                JOptionPane.showMessageDialog(null, "Errore nel salvataggio");

            }
        }
    }
}