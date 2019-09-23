package main.view;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.*;
import javax.swing.border.Border;


public class GameView implements ActionListener {

    public GameView(){
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch(Exception e) {
            //silently ignore
        }
        JFrame window = new JFrame("New Chess Game");
        window.setSize(500, 600);
        initializeButton(window);
        window.add(new BoardPanel(), BorderLayout.CENTER); // Adds chess board to the center
        window.setVisible(true);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * Add Start Button forthis window
     * @param window The window of whole GUI
     */
    private void initializeButton(JFrame window) {
        JButton button = new JButton("Start");
        button.addActionListener(this);
        window.add(button, BorderLayout.SOUTH);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JOptionPane.showMessageDialog(null,
                "I was clicked by "+e.getActionCommand(),
                "Title here", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        new GameView();
    }
}
