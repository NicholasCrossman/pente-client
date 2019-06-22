package gui.frames.gameFrame;

import controller.LeaderBoardListener;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class LeaderboardFrame extends JFrame {
    private JPanel panel;
    private JTextArea leaderboardTextArea;
    private JButton closeButton;
    private ArrayList<LeaderBoardListener> listeners;

    public LeaderboardFrame() {
        super("Team 4\'s Game");
        listeners = new ArrayList<LeaderBoardListener>();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(813, 656);
        panel = new JPanel();
        closeButton = new JButton("Close");
        closeButton.addActionListener(new CloseButtonListener());
        leaderboardTextArea = new JTextArea();


        panel.setLayout(new BorderLayout(5,5));

        panel.add(leaderboardTextArea, BorderLayout.CENTER);
        leaderboardTextArea.setEditable(false);

        panel.add(closeButton, BorderLayout.SOUTH);
        this.add(panel);
        this.setLocationRelativeTo(null);

    }

    public void generateLeaderboard(String l) {
        StringBuilder output = new StringBuilder();
        output.append("User\t\tWins\t\tLosses\t\tTies\n");
        output.append(l);
        leaderboardTextArea.setText(output.toString());
    }

    public void addLeaderBoardListener(LeaderBoardListener l){
        listeners.add(l);
    }

    public void close(){
        for(LeaderBoardListener l : listeners) {
            l.closeFrame();
        }
    }

    private class CloseButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent l) {
           close();
        }
    }
}
