/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package gui;

import engine.Engine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class GameGUI extends JFrame {

    private final JTextArea console = new JTextArea();
    private final JTextField input = new JTextField();
    private final JButton sendBtn = new JButton("Invia");
    private final JLabel imageLabel = new JLabel();

    private static final double IMAGE_OVERSCALE = 1.90;

    private final Engine engine;
    private String currentRoomName = null;

    public GameGUI() {

        super("Adventure Game");

        engine = new Engine(new impl.FireHouseGame());

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setOpaque(true);
        imageLabel.setBackground(Color.WHITE);

        console.setEditable(false);
        console.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 15));
        console.setBackground(Color.BLACK);
        console.setForeground(Color.WHITE);
        console.setCaretColor(Color.WHITE);
        console.setLineWrap(true);
        console.setWrapStyleWord(true);

        JScrollPane scroll = new JScrollPane(console);

        JPanel bottom = new JPanel(new BorderLayout(6, 6));
        bottom.add(input, BorderLayout.CENTER);
        bottom.add(sendBtn, BorderLayout.EAST);

        JPanel right = new JPanel(new BorderLayout(6, 6));
        right.add(scroll, BorderLayout.CENTER);
        right.add(bottom, BorderLayout.SOUTH);

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, imageLabel, right);
        split.setResizeWeight(0.65);

        JPanel topButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        topButtons.setOpaque(false);

        JButton btnSave = new JButton("Salva");
        btnSave.addActionListener(e -> console.append("[Salvataggio non implementato]\n"));

        JButton btnExit = new JButton("Esci");
        btnExit.addActionListener(e -> System.exit(0));

        topButtons.add(btnSave);
        topButtons.add(btnExit);

        JPanel root = new JPanel(new BorderLayout());
        root.add(topButtons, BorderLayout.NORTH);
        root.add(split, BorderLayout.CENTER);

        setContentPane(root);

        sendBtn.addActionListener(e -> submit());
        input.addActionListener(e -> submit());

        console.append(engine.getInitialMessage());
        updateRoomImage();

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (currentRoomName != null) {
                    updateRoomImage();
                }
            }
        });
        setVisible(true);
    }

    public void println(String text) {
        console.append(text + "\n");
        console.setCaretPosition(console.getDocument().getLength());
    }

    private void submit() {

        String cmd = input.getText().trim();

        if (!cmd.isEmpty()) {

            println("> " + cmd);

            String response = engine.processCommand(cmd);

            console.append(response);

            input.setText("");

            updateRoomImage();
        }
    }

    private void updateRoomImage() {

        String roomName = engine.getCurrentRoomName();

        currentRoomName = roomName;

        String fileName = roomName.toLowerCase() + ".png";

        drawFramedImage("/images/" + fileName);
    }

    private void drawFramedImage(String classpathImage) {

        try {

            BufferedImage original = ImageIO.read(getClass().getResource(classpathImage));

            if (original == null) {
                imageLabel.setIcon(null);
                imageLabel.setText("[Immagine non trovata]");
                return;
            }

            int panelW = Math.max(1, imageLabel.getWidth());
            int panelH = Math.max(1, imageLabel.getHeight());

            double scale = Math.min(
                    (double) panelW / original.getWidth(),
                    (double) panelH / original.getHeight()
            );

            scale *= IMAGE_OVERSCALE;

            int newW = (int) Math.round(original.getWidth() * scale);
            int newH = (int) Math.round(original.getHeight() * scale);

            BufferedImage framed = new BufferedImage(panelW, panelH, BufferedImage.TYPE_INT_RGB);

            Graphics2D g2d = framed.createGraphics();
            g2d.setColor(Color.BLACK);
            g2d.fillRect(0, 0, panelW, panelH);

            g2d.setRenderingHint(
                    RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR
            );

            int x = (panelW - newW) / 2;
            int y = (panelH - newH) / 2;

            g2d.drawImage(original, x, y, newW, newH, null);

            g2d.dispose();

            imageLabel.setIcon(new ImageIcon(framed));
            imageLabel.setText(null);

        } catch (Exception e) {

            imageLabel.setIcon(null);
            imageLabel.setText("[Errore immagine]");
        }
    }
}