package gui.frames.gameFrame.gameViewPieces;

import controller.GameController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This panel displays the rules of Pente
 */
public class PenteRulesPanel extends JPanel {

    private JTextPane pane;
    private JLabel turn;


    /**
     * Construct a new history panel.
     */
    public PenteRulesPanel() {
        this.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        this.setLayout(new BorderLayout());
        pane=new JTextPane();
        pane.setText("Welcome to the game of Pente!\n" +
                     "The objective of Pente is to \n" +
                     "create a string of five pieces\n" +
                     "You can remove two of your \n" +
                     "opponents pieces by having \n" +
                     "your pieces around them. \n");
        pane.setEditable(false);
        turn=new JLabel();
        setTurn(1);
        JPanel borderPanel = new JPanel(new GridLayout(3,0));
        borderPanel.setBorder(BorderFactory.createTitledBorder("Pente"));
        borderPanel.add(pane);
        borderPanel.add(turn);
        this.add(borderPanel, BorderLayout.CENTER);
    }
    public void setTurn(int x){
        if (x==0){
            turn.setText("Player one's turn");
        }
        else if (x==1){
            turn.setText("Player two's turn");
        }
        else {
            turn.setText("Game Over");
        }
    }
}
