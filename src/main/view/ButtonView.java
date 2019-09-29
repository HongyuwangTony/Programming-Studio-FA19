package view;

import controller.GameController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class ButtonView extends JPanel {
    private static final Dimension buttonSize = new Dimension(200, 40);
    private static final Font buttonFont = new Font("Dialog", Font.PLAIN, 18);

    public ButtonView(GameController gameController) {
        this.setLayout(new GridLayout(3,1));
        addButton("Start / Restart", gameController);
        addButton("Forfeit", gameController);
        addButton("Undo", gameController);
    }

    private void addButton(String text, GameController gameController) {
        JButton button = new JButton(text);
        button.setPreferredSize(buttonSize);
        button.setFont(buttonFont);
        button.addActionListener((ActionEvent e) -> gameController.clickOnButton(text));
        this.add(button);
    }
}
