package gui.frames;

import controller.LoginController;
import controller.UserController;
import controller.WelcomeListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WelcomeFrame extends JFrame {
    private JPanel panel;
    private JButton startButton;
    private JTextArea leaderboardTextArea;
    private JLabel welcomeLabel;
    private ArrayList<WelcomeListener> listeners;

    public WelcomeFrame() {
        super("Team 4\'s Game");
        listeners = new ArrayList<WelcomeListener>();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(813, 656);
        panel = new JPanel();
        startButton = new JButton("Start!");
        startButton.addActionListener(new StartButtonListener());
        leaderboardTextArea = new JTextArea();
        welcomeLabel = new JLabel("Welcome!");




        panel.setLayout(new BorderLayout(5,5));

        panel.add(welcomeLabel, BorderLayout.NORTH);
        welcomeLabel.setHorizontalAlignment(JLabel.CENTER);
        welcomeLabel.setFont(welcomeLabel.getFont().deriveFont(32f));


        panel.add(leaderboardTextArea, BorderLayout.CENTER);
        leaderboardTextArea.setEditable(false);


        panel.add(startButton, BorderLayout.SOUTH);
        this.add(panel);
        this.setLocationRelativeTo(null);

    }

    public void generateLeaderboard(String l) {
        StringBuilder output = new StringBuilder();
        output.append("User\t\tWins\t\tLosses\t\tTies\n");
        for(int i=0; i < 80; i++) {
            output.append("_");
        }
        output.append("\n");
        output.append(l);
        leaderboardTextArea.setText(output.toString());
    }

    public void addWelcomeListener(WelcomeListener l){
        listeners.add(l);
    }

    public void toLogin(){
        for(WelcomeListener l : listeners) {
            l.nextFrame();
        }
    }

    private class StartButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent l) {
            toLogin();
        }
    }
}
