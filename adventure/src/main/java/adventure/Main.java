/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package adventure;

import javax.swing.SwingUtilities;
import gui.MenuFrame;
/**
 *
 * @author alessandropace
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            MenuFrame menu = new MenuFrame();
            menu.setVisible(true);
         });
    }    
    
}
