package gui;

import adventure.Engine;

import javax.swing.*;
import java.awt.*;

public class GameGUI extends JFrame {

    private final Engine engine;

    private final JTextArea console = new JTextArea();
    private final JTextField input = new JTextField();
    private final JButton sendBtn = new JButton("Invia");

    private final JLabel imageLabel = new JLabel();
    private final JLabel roomLabel = new JLabel();

    public GameGUI() {

        super("Adventure Game");

        engine = new Engine(new impl.FireHouseGame());

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        createMenuBar();

        // ===== LABEL STANZA =====
        roomLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        roomLabel.setBorder(BorderFactory.createEmptyBorder(5,10,5,10));

        // ===== AREA TESTO =====
        console.setEditable(false);
        console.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        console.setBackground(Color.BLACK);
        console.setForeground(Color.WHITE);
        console.setCaretColor(Color.WHITE);
        console.setLineWrap(true);
        console.setWrapStyleWord(true);

        JScrollPane scroll = new JScrollPane(console);

        // ===== IMMAGINE =====
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setOpaque(true);
        imageLabel.setBackground(Color.BLACK);

        // ===== INPUT =====
        JPanel bottom = new JPanel(new BorderLayout(6,6));
        bottom.add(input, BorderLayout.CENTER);
        bottom.add(sendBtn, BorderLayout.EAST);

        // ===== LATO SINISTRO (console + input) =====
        JPanel left = new JPanel(new BorderLayout());
        left.add(scroll, BorderLayout.CENTER);
        left.add(bottom, BorderLayout.SOUTH);

        // ===== SPLIT PANE (testo sinistra, immagine destra) =====
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, left, imageLabel);
        split.setResizeWeight(0.6);

        JPanel root = new JPanel(new BorderLayout());
        root.add(roomLabel, BorderLayout.NORTH);
        root.add(split, BorderLayout.CENTER);

        setContentPane(root);

        // ===== EVENTI =====
        sendBtn.addActionListener(e -> submit());
        input.addActionListener(e -> submit());

        // ===== MESSAGGIO INIZIALE =====
        console.append(engine.getInitialMessage());
        updateRoomInfo();

        setVisible(true);
    }

    private void createMenuBar() {

        JMenuBar menuBar = new JMenuBar();

        JMenu gameMenu = new JMenu("Gioco");

        JMenuItem saveItem = new JMenuItem("Salva...");
        JMenuItem loadItem = new JMenuItem("Carica...");
        JMenuItem exitItem = new JMenuItem("Esci");

        saveItem.addActionListener(e -> saveGame());
        loadItem.addActionListener(e -> loadGame());
        exitItem.addActionListener(e -> System.exit(0));

        gameMenu.add(saveItem);
        gameMenu.add(loadItem);
        gameMenu.addSeparator();
        gameMenu.add(exitItem);

        menuBar.add(gameMenu);

        setJMenuBar(menuBar);
    }

    private void submit() {

        String command = input.getText().trim();

        if (!command.isEmpty()) {

            console.append("\n> " + command + "\n");

            String response = engine.processCommand(command);

            console.append(response);

            input.setText("");

            updateRoomInfo();
        }
    }

    private void updateRoomInfo() {

        String roomName = engine.getCurrentRoomName();

        roomLabel.setText("Stanza: " + roomName);

        String imageName = roomName.toLowerCase() + ".png";

        imageLabel.setIcon(
                ImageLoader.load(imageName, imageLabel.getWidth(), imageLabel.getHeight())
        );
    }

    private void saveGame() {
        console.append("\n[Salvataggio non ancora implementato]\n");
    }

    private void loadGame() {
        console.append("\n[Caricamento non ancora implementato]\n");
    }
}