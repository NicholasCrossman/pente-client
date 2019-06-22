package gui.frames;

import controller.JoinGameListener;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class JoinGameFrame extends JFrame{
    private ArrayList<JoinGameListener> listeners;
    private JButton joinButton;
    private JButton spectateButton;
    private JButton reviewButton;
    private JButton backButton;
    private JTabbedPane tabbedPane;
    private JScrollPane scroll1;
    private JScrollPane scroll2;
    private JScrollPane scroll3;
    private JList<String> joinGamesList;
    private JList<String> spectateGamesList;
    private JList<String> reviewGamesList;
    private JTextArea errorResponseArea;
    private JMenuBar menu;
    private JMenuItem leaderboard;

    public JoinGameFrame(){
        // set up frame
        super("Team 4\'s Game");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(813, 656);

        //initialize components
        tabbedPane = new JTabbedPane();
        listeners = new ArrayList<JoinGameListener>();
        errorResponseArea = new JTextArea(2, 15);
        errorResponseArea.setEditable(false);
        errorResponseArea.setLineWrap(true);
        errorResponseArea.setAlignmentX(400);
        errorResponseArea.setWrapStyleWord(true);
        //Init Buttons
        joinButton = new JButton("Join");
        joinButton.addActionListener(new JoinButtonListener());
        spectateButton = new JButton("Spectate");
        spectateButton.addActionListener(new SpectateButtonListener());
        reviewButton = new JButton("Review");
        reviewButton.addActionListener(new ReviewButtonListener());
        backButton = new JButton("Back");
        backButton.addActionListener(new BackButtonListener());
        //initialize lists and ScrollPanes
        joinGamesList = new JList<>(new String[]{"No Data"});
        joinGamesList.setLayoutOrientation(JList.VERTICAL);
        spectateGamesList = new JList<>(new String[]{"No Data"});
        spectateGamesList.setLayoutOrientation(JList.VERTICAL);
        reviewGamesList = new JList<>(new String[]{"No Data"});
        reviewGamesList.setLayoutOrientation(JList.VERTICAL);
        scroll1 = new JScrollPane(joinGamesList);
        scroll2 = new JScrollPane(spectateGamesList);
        scroll3 = new JScrollPane(reviewGamesList);

        //tabbedPane arrangement
        tabbedPane.setTabPlacement(JTabbedPane.TOP);
        tabbedPane.setPreferredSize(new Dimension(810,550));
        JPanel joinPanel = new JPanel(new BorderLayout());
        joinPanel.add(scroll1, BorderLayout.CENTER);
        joinPanel.add(joinButton, BorderLayout.PAGE_END);
        tabbedPane.add("Join Games",joinPanel);

        //panel setup
        JPanel spectatePanel = new JPanel(new BorderLayout());
        spectatePanel.add(scroll2, BorderLayout.CENTER);
        spectatePanel.add(spectateButton, BorderLayout.PAGE_END);
        tabbedPane.add("Spectate Games",spectatePanel);

        JPanel reviewPanel = new JPanel(new BorderLayout());
        reviewPanel.add(scroll3, BorderLayout.CENTER);
        reviewPanel.add(reviewButton, BorderLayout.PAGE_END);
        tabbedPane.add("Review Completed Games",reviewPanel);

        //Leader board
        JPanel leaderBordPanel = new JPanel(new BorderLayout(5, 5));
        menu = new JMenuBar();
        leaderboard = new JMenuItem("Leaderboard");
        leaderboard.addActionListener(new LeaderboardMenuListener());
        menu.add(leaderboard);

        leaderBordPanel.add(menu, BorderLayout.NORTH);
        //mainPanel arrangement
        JPanel mainPanel = new JPanel(new BorderLayout(5, 5));
        mainPanel.add(tabbedPane, BorderLayout.PAGE_START);
        mainPanel.add(errorResponseArea, BorderLayout.CENTER);
        mainPanel.add(backButton, BorderLayout.PAGE_END);
        mainPanel.add(menu, BorderLayout.WEST);



        this.add(mainPanel);
        this.setLocationRelativeTo(null);
    }

    public void addJoinGameListener(JoinGameListener l){
        listeners.add(l);
    }

    private void joinGame(String id) {
        for (JoinGameListener l : listeners) {
            l.joinGame(id);
        }
    }


    public void generateReviewList(String[] list){
        if(list!=null)
            reviewGamesList.setListData(list);
    }

    public void generateJoinList(String[] list){
        if(list!=null)
            joinGamesList.setListData(list);
    }

    public void generateSpectateList(String[] list){
        if(list!=null)
            spectateGamesList.setListData(list);
    }

    public void openLeaderboard(){
        for(JoinGameListener l : listeners) {
            l.openLeaderboard();
        }
    }

    private class JoinButtonListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            if(joinGamesList.getSelectedValue()!=null&&joinGamesList.getSelectedValue()!="No Data"){
                joinGame(joinGamesList.getSelectedValue());
            }
            else errorResponseArea.setText("Please select a game or click \"Back\" to return to the previous screen.");
        }
    }

    private class SpectateButtonListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            if(spectateGamesList.getSelectedValue()!=null&&spectateGamesList.getSelectedValue()!="No Data"){
                joinGame(spectateGamesList.getSelectedValue());
            }
            else errorResponseArea.setText("Please select a game or click \"Back\" to return to the previous screen.");
        }
    }

    private class ReviewButtonListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            if(reviewGamesList.getSelectedValue()!=null&&reviewGamesList.getSelectedValue()!="No Data"){
                joinGame(reviewGamesList.getSelectedValue());
            }
            else errorResponseArea.setText("Please select a game or click \"Back\" to return to the previous screen.");
        }
    }

    private class BackButtonListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            for (JoinGameListener l : listeners) {
            l.previousFrame();
            }

        }
    }
    private class LeaderboardMenuListener implements ActionListener {
        public void actionPerformed(ActionEvent l) {
            openLeaderboard();
        }
    }
}
