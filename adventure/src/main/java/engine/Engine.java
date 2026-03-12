/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package engine;

import gui.GameGUI;
import parser.Parser;
import parser.ParserOutput;
import type.CommandType;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.File;
import java.io.IOException;
import java.util.Set;

import javax.swing.SwingUtilities;

public class Engine {

    private final GameDescription game;
    private Parser parser;

    public Engine(GameDescription game) {

        this.game = game;

        try {
            this.game.init();
        } catch (Exception ex) {
            System.err.println(ex);
        }

        try {
            Set<String> stopwords = Utils.loadFileListInSet(new File("./resources/stopwords"));
            parser = new Parser(stopwords);
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }

    /**
     * Metodo chiamato dalla GUI per eseguire i comandi del giocatore
     */
    public String processCommand(String command) {

        ParserOutput p = parser.parse(
                command,
                game.getCommands(),
                game.getCurrentRoom().getObjects(),
                game.getInventory()
        );

        if (p == null || p.getCommand() == null) {
            return "Non capisco quello che mi vuoi dire.\n";
        }

        if (p.getCommand().getType() == CommandType.END) {
            return "Sei un fifone, addio!\n";
        }

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        PrintStream output = new PrintStream(buffer);

        game.nextMove(p, output);

        return buffer.toString();
    }

    /**
     * Messaggio iniziale del gioco (usato dalla GUI)
     */
    public String getInitialMessage() {

        StringBuilder sb = new StringBuilder();

        sb.append(game.getWelcomeMsg()).append("\n\n");
        sb.append("Ti trovi qui: ")
          .append(game.getCurrentRoom().getName())
          .append("\n\n");
        sb.append(game.getCurrentRoom().getDescription())
          .append("\n");

        return sb.toString();
    }
    
    public String getCurrentRoomName() {
        return game.getCurrentRoom().getName();
    }

    /**
     * Avvio dell'applicazione con GUI Swing
     */
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new GameGUI();
        });

    }

}