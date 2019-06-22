package gui.frames.gameFrame.gameViewPieces;

import controller.GameController;
import controller.MoveListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;


public class BoardView extends JPanel implements MouseListener {

    private int[] cords;
    private ArrayList<MoveListener> listeners=new ArrayList<MoveListener>();
    private int boardSize;

    private Color boardBackgroundColor = new Color(206, 133, 12);
    private Color panelBackground = new Color(214, 215, 213);
    private Color gridColor = new Color(43, 40, 43);
    private BasicStroke gridStroke = new BasicStroke(2.0f);
    private Font labelFont = new Font("SansSerif", Font.PLAIN, 15);

    private static final int BORDER_SIZE = 25;

    public List<Piece> pieces;

    public enum PieceColor { WHITE, BLACK }

    private class Piece {
        public PieceColor color;
        public int row, col;

        public Piece( int row, int col, PieceColor color ) {
            this.color = color;
            this.row = row;
            this.col = col;
        }
    }

    /**
     * Constructor for BoardView panel.
     *
     * @param size width/height in pixels
     */
    public BoardView(int size ) {
        this.boardSize = 19;
        this.setPreferredSize(new Dimension(size,size) );
        this.setBackground(panelBackground);
        this.pieces = new ArrayList<>();
        this.addMouseListener(this);
    }

    /**
     * Add a piece to the board view.
     * @param row the row
     * @param col the column
     * @param color the color of the piece
     */
    public void addPiece( int row, int col, PieceColor color ) {
        pieces.add( new Piece(row, col, color) );
    }

    public void removePiece(ArrayList<int[]> empty){
        int[] pieceCords=new int[2];
        for(int i=0;i<pieces.size();i++){
            Piece p = pieces.get(i);
            pieceCords[0]=p.row;
            pieceCords[1]=p.col;
            if (empty.contains(pieceCords)){
                pieces.remove(p);
            }
        }
    }

    /**
     * Draws the board.
     * @param g Graphics
     */
    @Override
    public void paintComponent( Graphics g ) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;
        g2d.setFont(labelFont);

        int w = this.getWidth();
        int size = w - (2 * BORDER_SIZE);

        drawBackground(g, size);
        drawGrid(g2d, size);
        drawLabels(g2d, size);
        drawPieces(g2d, size);
    }

    private void drawBackground( Graphics g, int size ) {
        g.setColor( boardBackgroundColor );
        g.fillRect(BORDER_SIZE, BORDER_SIZE, size, size );
    }

    private void drawGrid( Graphics2D g, int size ) {
        GeneralPath path = new GeneralPath();

        double cellSize = (double)size / boardSize;
        double start = BORDER_SIZE, end = size + BORDER_SIZE;

        for( int i = 0; i < boardSize; i++ ) {
            double x = (i * cellSize) + BORDER_SIZE + (cellSize / 2.0);
            double y = x;
            path.moveTo(start,y);
            path.lineTo(end,y);
            path.moveTo(x,start);
            path.lineTo(x,end);
        }

        g.setStroke(gridStroke);
        g.setColor( gridColor );
        g.draw(path);

    }

    private void drawLabels( Graphics2D g, int sz ) {
        Rectangle2D.Double rect = new Rectangle2D.Double();
        rect.width = (double)sz / boardSize;
        rect.height = BORDER_SIZE;

        rect.x = BORDER_SIZE;
        rect.y = sz + BORDER_SIZE;
        for( int i = 0; i < boardSize; i++ ) {
            rect.x = BORDER_SIZE + (i * rect.width);
            drawCenteredString(g, "" + (char)(i  + 'A'), rect);
        }
        rect.x = 0;
        rect.height = rect.width;
        rect.width = BORDER_SIZE;
        for( int i = 0; i < boardSize; i++ ) {
            rect.y = (BORDER_SIZE + sz) - ((i + 1) * rect.height);
            drawCenteredString(g, "" + (i + 1), rect);
        }
    }

    private void drawPieces( Graphics2D g, int size ) {
        Ellipse2D.Double el = new Ellipse2D.Double();
        double cellSize = (double)size / boardSize;
        el.width = cellSize;
        el.height = cellSize;
        for (int i=0;i<pieces.size();i++){
            Piece p=pieces.get(i);
            el.x = BORDER_SIZE + (p.col * cellSize);
            el.y = BORDER_SIZE + ( (boardSize - p.row - 1) * cellSize );
            if( p.color == PieceColor.BLACK ) {
                g.setColor(Color.black);
            } else {
                g.setColor(Color.white);
            }
            g.fill(el);
        }

    }

    /**
     * Mouse clicked event.  Determines the cell where the mouse
     * was clicked and prints the row/column to the console.
     */
    public void mouseClicked(MouseEvent e) {
        Point p = findBoardCell(e.getX(), e.getY());
        if( p == null ) {
            System.out.println("Mouse is not on the board");
        } else {
            int [] mouse= new int[2];
            mouse[0]=p.y;
            mouse[1]=p.x;
            cords=mouse;
            notifyListeners();
        }
    }
    public void addListeners(MoveListener m){
        listeners.add(m);
    }
    private void notifyListeners(){
        for (int i=0;i<listeners.size();i++)
            listeners.get(i).moveChanged(cords);
    }


    /**
     * Given mouse coordinates, find the corresponding cell location.  Note the location is
     * zero-based, the row/column are off by one from the labels on the board.
     * @param mouseX the x coordinate of the mouse
     * @param mouseY the y coordinate of the mouse
     * @return a Point object containing the cell location row = y, col = x, or null if the mouse
     *    is not on the board.
     */
    public Point findBoardCell( int mouseX, int mouseY ) {
        int size = this.getWidth();
        mouseY = size - mouseY;  // Flip y

        if( mouseX >= BORDER_SIZE && mouseX <= size - BORDER_SIZE &&
                mouseY >= BORDER_SIZE && mouseY <= size - BORDER_SIZE ) {

            double bSize = size - (2.0 * BORDER_SIZE);
            double cellSize = bSize / boardSize;
            int cellRow = (int)Math.floor( (mouseY - BORDER_SIZE) / cellSize );
            int cellCol = (int)Math.floor( (mouseX - BORDER_SIZE) / cellSize );
            return new Point(cellCol, cellRow);
        }

        return null;
    }

    public void mousePressed(MouseEvent e) { }
    public void mouseReleased(MouseEvent e) { }
    public void mouseEntered(MouseEvent e) { }
    public void mouseExited(MouseEvent e) { }

    /**
     * Draw a String centered in the middle of a Rectangle.
     * @param g2d Graphics
     * @param text The String to draw.
     * @param rect The Rectangle to center the text in.
     */
    private void drawCenteredString(Graphics2D g2d, String text, Rectangle2D.Double rect) {
        Font font = g2d.getFont();
        FontRenderContext frc = g2d.getFontRenderContext();
        GlyphVector gv = font.createGlyphVector(frc, text);
        Rectangle2D box = gv.getVisualBounds();

        int x = (int)Math.round((rect.getWidth() - box.getWidth()) / 2.0 - box.getX() + rect.x);
        int y = (int)Math.round((rect.getHeight() - box.getHeight()) / 2.0 - box.getY() + rect.y);
        g2d.drawString(text, x, y);
    }
}
