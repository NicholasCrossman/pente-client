package gui.frames;

import controller.UserListener;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class UserFrame extends JFrame {

    //private JTextField recordField;
    private JPanel panel;
    private JPanel southPanel;
    private JPanel northPanel;
    private JButton listGamesButton;
    private JButton createGameButton;
    private JLabel userLabel;
    private JLabel recordLabel;
    private ArrayList<UserListener> listeners;
    private JMenuBar menu;
    private JMenuItem leaderboard;

    public UserFrame(){
        super("Team 4\'s Game");
        listeners = new ArrayList<UserListener>();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(813, 656);

        
        
        panel = new JPanel();
        northPanel = new JPanel();
        southPanel = new JPanel();
        //recordField = new JTextField("");
        listGamesButton = new JButton("Join Game");
        listGamesButton.addActionListener(new ListGameButtonListener());
        createGameButton = new JButton("Create Game");
        createGameButton.addActionListener(new CreateGameButtonListener());
        userLabel = new JLabel("Hello User!");
        recordLabel = new JLabel("Record:");
        panel.setLayout(new BorderLayout(5,5));
        northPanel.setLayout(new BorderLayout(5,5));
        southPanel.setLayout(new BorderLayout(5,5));
        northPanel.add(userLabel, BorderLayout.SOUTH);
        userLabel.setHorizontalAlignment(JLabel.CENTER);
        userLabel.setFont(userLabel.getFont().deriveFont(32f));
        panel.add(recordLabel, BorderLayout.CENTER);
        recordLabel.setHorizontalAlignment(JLabel.CENTER);
        recordLabel.setFont(userLabel.getFont().deriveFont(16f));
        //panel.add(recordField,BorderLayout.SOUTH);
        //recordField.setHorizontalAlignment(JTextField.CENTER);
        southPanel.add(listGamesButton, BorderLayout.WEST);
        southPanel.add(createGameButton,BorderLayout.EAST);
        panel.add(southPanel, BorderLayout.SOUTH);
        menu = new JMenuBar();
        leaderboard = new JMenuItem("Leaderboard");
        leaderboard.addActionListener(new LeaderboardMenuListener());
        menu.add(leaderboard);
        northPanel.add(menu,BorderLayout.NORTH);
        panel.add(northPanel, BorderLayout.NORTH);

        this.add(panel);
        this.setLocationRelativeTo(null);
    }

    public void setUser(String leaderBoard) {
        userLabel.setText("Hello, " + leaderBoard);
    }
    public void setUserRecord(int win, int loss,int tie) {
        recordLabel.setText("Record: W " + win + " | L " + loss + " | T " + tie);
    }


    public void addUserListener(UserListener l){
        listeners.add(l);
    }

    public void toCreateGame(){
        for(UserListener l : listeners) {
            l.nextFrame();
        }
    }
    public void toListGame(){
        for(UserListener l : listeners) {
            l.nextFrame2();
        }
    }
    public void openLeaderboard(){
        for(UserListener l : listeners) {
            l.openLeaderboard();
        }
    }

    private class CreateGameButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent l) {
            toCreateGame();
        }
    }
    private class ListGameButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent l) {
            toListGame();
        }
    }
    private class LeaderboardMenuListener implements ActionListener {
        public void actionPerformed(ActionEvent l) {
            openLeaderboard();
        }
    }
    public void generateUser(String[] record ) {
        if(record.length >=3) {
            userLabel.setText("Hello, " + record[0]);
            recordLabel.setText(" W: " + record[1] + " L: " + record[2] + " T: " + record[3]);
        }
        else{
            userLabel.setText("userLabel: Placeholder Text");
            recordLabel.setText("recordLabel: Placeholder Text");
        }
    }
}
