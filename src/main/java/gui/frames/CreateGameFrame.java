package gui.frames;

import controller.CreateGameListener;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class CreateGameFrame extends JFrame {

    private JTextField userChallengeField;
    private JLabel userChallengeLabel;
    private JComboBox ruleSelectionBox;
    private JTextArea leaderboardTextArea;
    private JButton createGameButton;
    private JLabel leaderboardLabel;
    private JTextArea errorResponseArea;
    private ButtonGroup visibilitySelect;
    private JRadioButton privateVisibility;
    private JRadioButton publicVisibility;
    private ArrayList<CreateGameListener> listeners;
    private JButton backButton;

    public CreateGameFrame(){
        // set up frame
        super("Team 4\'s Game");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(833, 656);

        //initialize elements
        listeners = new ArrayList<CreateGameListener>();
        userChallengeLabel = new JLabel("Enter the username you wish to challenge: ");
        userChallengeField = new JTextField(16);
        leaderboardLabel = new JLabel("Leaderboard");
        String[] ruleSelections = {"Pente","Other Rules"};
        ruleSelectionBox = new JComboBox(ruleSelections);
        leaderboardTextArea = new JTextArea();


        errorResponseArea = new JTextArea(2, 15);
        errorResponseArea.setEditable(false);
        errorResponseArea.setLineWrap(true);
        errorResponseArea.setWrapStyleWord(true);

        // set up the radio buttons to select public or private visibility
        publicVisibility = new JRadioButton("Public");
        publicVisibility.setActionCommand("Public");
        publicVisibility.setSelected(true);
        publicVisibility.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // set publicVisibility to true for all listeners
                publishVisibility(true);
            }
        });

        privateVisibility = new JRadioButton("Private");
        privateVisibility.setActionCommand("Private");
        privateVisibility.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // set publicVisibility to false for all listeners
                publishVisibility(false);
            }
        });

        visibilitySelect = new ButtonGroup();
        visibilitySelect.add(publicVisibility);
        visibilitySelect.add(privateVisibility);
        // a panel to hold the buttons
        JPanel radioPanel = new JPanel(new GridLayout(0,1));
        radioPanel.add(publicVisibility);
        radioPanel.add(privateVisibility);

        backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                for (CreateGameListener l : listeners) {
                    l.previousFrame();
                }
            }
        });


        createGameButton = new JButton("Create Game");
        createGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createGame(userChallengeField.getText());
            }
        });
        // create a Border around errorResponseArea
        Border border = BorderFactory.createLineBorder(Color.BLACK);
        errorResponseArea.setBorder(BorderFactory.createCompoundBorder(border,
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        //Main panel init
        JPanel mainPanel = new JPanel(new BorderLayout(5,5));
        //Sub Panel init and components
        JPanel leaderboardPanel = new JPanel(new BorderLayout(5,5));
        leaderboardPanel.add(leaderboardLabel, BorderLayout.NORTH);
        leaderboardPanel.add(leaderboardTextArea, BorderLayout.CENTER);
        JPanel inputPanel = new JPanel(new BorderLayout(5,5));
        inputPanel.add(ruleSelectionBox, BorderLayout.BEFORE_FIRST_LINE);

        // inner JPanel to hold the selections for a new game
        JPanel innerInputPanel = new JPanel(new BorderLayout(5, 5));
        innerInputPanel.add(userChallengeLabel, BorderLayout.BEFORE_FIRST_LINE);
        innerInputPanel.add(userChallengeField, BorderLayout.CENTER);
        innerInputPanel.add(radioPanel, BorderLayout.PAGE_END);
        inputPanel.add(innerInputPanel, BorderLayout.AFTER_LINE_ENDS);

        inputPanel.add(createGameButton, BorderLayout.AFTER_LAST_LINE);

        JPanel backAndErrorPanel = new JPanel(new BorderLayout());
        backAndErrorPanel.add(errorResponseArea, BorderLayout.BEFORE_FIRST_LINE);
        backAndErrorPanel.add(backButton, BorderLayout.AFTER_LAST_LINE);
        //Add main panel components
        mainPanel.add(leaderboardPanel, BorderLayout.WEST);
        mainPanel.add(inputPanel, BorderLayout.EAST);
        mainPanel.add(backAndErrorPanel, BorderLayout.AFTER_LAST_LINE);
        this.add(mainPanel);
        this.setLocationRelativeTo(null);
    }
    public void addCreateGameListener(CreateGameListener l){
        listeners.add(l);
    }

    public void createGame(String user2) {
        for (CreateGameListener l : listeners) {
            l.createGame(user2);
        }
    }

    public void publishVisibility(boolean vis) {
        for(CreateGameListener l : listeners) {
            l.setGameVisibility(vis);
        }
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


}
