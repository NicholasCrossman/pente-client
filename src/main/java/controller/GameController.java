package controller;

import com.google.gson.Gson;
import gui.frames.gameFrame.GameFrame;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.InvocationCallback;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.sse.InboundSseEvent;
import javax.ws.rs.sse.SseEventSource;
import java.util.ArrayList;

public class GameController implements GameListener{
    private GameFrame g;
    private Client client;
    private String url = "http://152.117.176.5:5004";
    private String userName;
    private int userNumber;
    private String gameID;
    private int x;
    private int y;
    private SseEventSource eventSource;
    private ArrayList<GameListener> listeners;
    UserController userController;

    public GameController(Client client, String userName,UserController uc) {
        g = new GameFrame();
        this.client = client;
        this.userName = userName;
        userController=uc;
        g.addGameListeners(this);
        g.addListeners(cords -> makeMove(cords[0],cords[1]));
    }
    private void connectToSseSource(){
        WebTarget target=client.target(url+"/game/"+gameID);
        eventSource= SseEventSource.target(target).build();
        eventSource.register(this::update);
        eventSource.open();
    }
    private void update(InboundSseEvent event){
            Gson gson= new Gson();
            UpdateObject message = gson.fromJson(event.readData(), UpdateObject.class);
            g.setTurn(message.turn);
            ArrayList<int[]> empty=new ArrayList<>();
            g.getBoardView().pieces.clear();
            for(int i=0;i<message.board.length;i++){
                for(int j=0;j<message.board.length;j++){
                    int[] piece=new int[2];
                    if(message.board[i][j]==-1){
                        piece[0]=i;
                        piece[1]=j;
                        empty.add(piece);
                    }
                    else if(message.board[i][j]==0){
                        g.addPiece(i,j,0);

                    }
                    else if(message.board[i][j]==1){
                        g.addPiece(i,j,1);
                    }
                    else{
                        System.out.println("player number wrong");
                    }
                }
            }
            g.removePieces(empty);
            g.repaint();
        }
    public void enterGame(String gameID) {
        g.setVisible(true);
        this.gameID=gameID;
        client.target(url)
                .path("/game/"+userName+"/games/"+gameID)
                .request()
                .async()
                .get(new getGame());
        connectToSseSource();
    }
    public void quit(){
        g.setVisible(false);
        userController.frame.setVisible(true);
        eventSource.close();
    }
    @Override
    public void openLeaderboard(){
        LeaderboardController gameController = new LeaderboardController(client);
    }
    @Override
    public void next(){

    }
    @Override
    public void prev(){

    }

    /**
     * "creates" a move and sends it to the server
     * @param x x location of the move
     * @param y y location of the move
     */
    //move = date, player, locationX, locationY
    public void makeMove(int x,int y){
        //as long as no underscores are in usernames
        String[] s= gameID.split("_");
        if (s[0].equals(userName)){
            userNumber=0;
        }
        else if(s[1].equals(userName)){
            userNumber=1;
        }
        else{
            System.err.println("Username is not in game ID");
            userNumber=-1;
        }
        this.x=x;
        this.y=y;
        String input="{" +
                     "\"x\":"+x+","+
                     "\"y\":"+y+","+
                     "\"player\":"+userNumber+
                     "}";
        Entity ent=Entity.entity(input, MediaType.TEXT_PLAIN);
        client.target(url)
                .path("/game/" + userName + "/games/" + gameID)
                .request()
                .async()
                .put(ent,new putMove());
    }

    /**
     * the get game call back
     */
    private class getGame implements InvocationCallback<Response> {
        @Override
        public void completed(Response response) {
            if (response.getStatus()==200) {
                Gson gson=new Gson();
                int[][] board=gson.fromJson(response.readEntity(String.class),int[][].class);
                ArrayList<int[]> empty=new ArrayList<>();
                g.getBoardView().pieces.clear();
                for(int i=0;i<board.length;i++){
                    for(int j=0;j<board.length;j++){
                        int[] piece=new int[2];
                        if(board[i][j]==-1){
                            piece[0]=i;
                            piece[1]=j;
                            empty.add(piece);
                        }
                        else if(board[i][j]==0){
                            g.addPiece(i,j,0);

                        }
                        else if(board[i][j]==1){
                            g.addPiece(i,j,1);
                        }
                        else{
                            System.out.println("player number wrong");
                        }
                    }
                }
                g.removePieces(empty);
            }
            else if (response.getStatus()==404){
                System.out.println("Game not found");
            }
            else{
                System.out.println("Game gave other error code!\n"+response);
            }
        }

        @Override
        public void failed(Throwable throwable) {
            //TODO return to previous frame
            System.out.println("Game really wrong...\n"+throwable);
        }
    }

    /**
     * the put move call back
     */
    private class putMove implements InvocationCallback<Response> {
        @Override
        public void completed(Response response) {
            if (response.getStatus()==200) {

            }
            else if(response.getStatus()==401){
                System.out.println("Move not valid, Is it your turn?");
            }
            //TODO any other error codes
            else{
                System.out.println("Move Errored: "+response.toString());

            }
        }

        @Override
        public void failed(Throwable throwable) {
            System.out.println("Something really went wrong...\n"+throwable);
        }

    }

}
