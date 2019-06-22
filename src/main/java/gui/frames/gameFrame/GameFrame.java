package gui.frames.gameFrame;

import controller.GameListener;
import controller.MoveListener;
import gui.frames.gameFrame.gameViewPieces.BoardView;
import gui.frames.gameFrame.gameViewPieces.PenteRulesPanel;
import gui.frames.gameFrame.gameViewPieces.PreserveAspectRatioLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * The main game window
 */
public class GameFrame extends JFrame {
    private BoardView boardView;
    private PenteRulesPanel rulesPanel;
    private ArrayList<GameListener> listeners;
    private JMenuBar menu;
    private JMenuItem leaderboard;
    /**
     * Constructs a new main window for the game.
     */
    public GameFrame() {
        setTitle("Game Example");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        listeners = new ArrayList<GameListener>();
        boardView = new BoardView(600);
        JPanel preserveAspectPanel = new JPanel(new PreserveAspectRatioLayout() );
        JPanel boardPanel = new JPanel( new BorderLayout() );
        preserveAspectPanel.add(boardView, BorderLayout.CENTER);
        boardPanel.add(preserveAspectPanel, BorderLayout.EAST);
        rulesPanel = new PenteRulesPanel();

        JButton prev= new JButton("<");
        JButton next= new JButton(">");
        JButton quit= new JButton("Leave Game");
        quit.addActionListener(new quitButtonListener());
        menu = new JMenuBar();
        leaderboard = new JMenuItem("Leaderboard");
        leaderboard.addActionListener(new LeaderboardMenuListener());
        menu.add(leaderboard);
        rulesPanel.add(menu,BorderLayout.NORTH);
        rulesPanel.add(quit,BorderLayout.SOUTH);

        this.add(rulesPanel, BorderLayout.WEST);
        this.add(boardPanel, BorderLayout.CENTER);

        this.pack();
        this.setLocationRelativeTo(null);
    }
    public void prevMove(){
        for(GameListener g:listeners){
            g.prev();
        }
    }
    public void nextMove(){
        for(GameListener g:listeners){
            g.next();
        }
    }
    public void addGameListeners(GameListener l){
        listeners.add(l);
    }
    public void addListeners(MoveListener m){
        boardView.addListeners(m);
    }
    public void addPiece(int x, int y, int player) {
        if (player == 0) {
            boardView.addPiece(x,y,BoardView.PieceColor.BLACK);
        }
        else {
            boardView.addPiece(x, y, BoardView.PieceColor.WHITE);
        }
    }
    public void openLeaderboard(){
        for(GameListener l : listeners) {
            l.openLeaderboard();
        }
    }
    public void removePieces(ArrayList<int[]> empty){
        boardView.removePiece(empty);
    }
    public void setTurn(int x){
        rulesPanel.setTurn(x);
    }
    public BoardView getBoardView(){
        return boardView;
    }

    private class prevButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent l) {
            prevMove();
        }
    }
    private class nextButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent l) {
            nextMove();
        }
    }
    private class quitButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent l) {
            for (GameListener gl : listeners) {
                gl.quit();
            }
        }
    }
    private class LeaderboardMenuListener implements ActionListener {
        public void actionPerformed(ActionEvent l) {
            openLeaderboard();
        }
    }
}
