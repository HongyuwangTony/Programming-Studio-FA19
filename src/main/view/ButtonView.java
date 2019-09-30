package view;

import controller.GameController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * ButtonView Class that initializes the view of the buttons
 */
public class ButtonView extends JPanel {
    private static final Dimension buttonSize = new Dimension(200, 40);
    private static final Font buttonFont = new Font("Dialog", Font.PLAIN, 18);

    /**
     * Constructor of ButtonView
     * @param gameController The Game controller that listens on the buttons
     */
    public ButtonView(GameController gameController) {
        this.setLayout(new GridLayout(3,1));
        addButton("Start / Restart", gameController);
        addButton("Forfeit", gameController);
        addButton("Undo", gameController);
    }

    /**
     * Adds a button with the given text to the view
     * @param text The text shown on the button
     * @param gameController The Game controller that listens on the button
     */
    private void addButton(String text, GameController gameController) {
        JButton button = new JButton(text);
        button.setPreferredSize(buttonSize);
        button.setFont(buttonFont);
        button.addActionListener((ActionEvent e) -> gameController.clickOnButton(text));
        this.add(button);
    }
}
