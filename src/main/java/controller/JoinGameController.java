package controller;

import com.google.gson.Gson;
import gui.frames.JoinGameFrame;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.InvocationCallback;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.concurrent.Future;

public class JoinGameController implements JoinGameListener {
    private String url = "http://152.117.176.5:5004";
    private Client client;
    private String userName;
    private JoinGameFrame frame;
    private String[] reviewIdList;
    private String[] spectateIdList;
    private String[] joinIdList;
    UserController userController;

    public JoinGameController(Client c, String user,UserController uc){
        client = c;
        userName = user;
        userController=uc;
        frame = new JoinGameFrame();
        frame.addJoinGameListener(this);
        frame.setVisible(true);
        getReviewGameList();
        getJoinGameList();
        getSpectateGameList();
    }

    @Override
    public void joinGame(String gameSelection) {
        String[] selection = gameSelection.split("\t");
        nextFrame(selection[0]);
    }

    @Override
    public void openLeaderboard(){
        LeaderboardController leaderboardController = new LeaderboardController(client);
    }

    public void nextFrame(String gameID){
        GameController g = new GameController(client, userName,userController);
        frame.setVisible(false);
        g.enterGame(gameID);

    }

    private void getJoinGameList() {
        Gson gson = new Gson();
        final Future<Response> future = client.target(url)
                .path("/user/"+userName+"/games")
                .request(MediaType.APPLICATION_JSON)
                .async()
                .get(new InvocationCallback<Response>() {
                    @Override
                    public void completed(Response response) {
                        if(response.getStatus() == 200) {
                            // login successful, username and password authenticated
                            String json = response.readEntity(String.class);
                            String list = gson.fromJson(json, String.class);
                            joinIdList = formatList(list);
                            frame.generateJoinList(joinIdList);
                        }
                        else if(response.getStatus() == 401) {
                            System.out.println("401 error");
                        }
                        else if(response.getStatus() == 404) {
                            System.out.println("404 error");
                        }
                    }

                    @Override
                    public void failed(Throwable throwable) {
                        // internal client error, exit program
                        // TODO add better error messages
                        throwable.printStackTrace();
                        System.exit(1);
                    }
                });
    }

    private void getSpectateGameList() {
        Gson gson = new Gson();
        final Future<Response> future = client.target(url)
                .path("/user/"+userName+"/games/spectate")
                .request(MediaType.APPLICATION_JSON)
                .async()
                .get(new InvocationCallback<Response>() {
                    @Override
                    public void completed(Response response) {
                        if(response.getStatus() == 200) {
                            // login successful, username and password authenticated
                            String json = response.readEntity(String.class);
                            String list = gson.fromJson(json, String.class);
                            spectateIdList = formatList(list);
                            frame.generateSpectateList(spectateIdList);
                        }
                        else if(response.getStatus() == 401) {
                            System.out.println("401 error");
                        }
                        else if(response.getStatus() == 404) {
                            System.out.println("404 error");
                        }
                    }

                    @Override
                    public void failed(Throwable throwable) {
                        // internal client error, exit program
                        // TODO add better error messages
                        throwable.printStackTrace();
                        System.exit(1);
                    }
                });
    }

    private void getReviewGameList() {
        Gson gson = new Gson();
        final Future<Response> future = client.target(url)
                .path("/user/"+userName+"/games/completed")
                .request(MediaType.APPLICATION_JSON)
                .async()
                .get(new InvocationCallback<Response>() {
                    @Override
                    public void completed(Response response) {
                        if(response.getStatus() == 200) {
                            // login successful, username and password authenticated
                            String json = response.readEntity(String.class);
                            String list = gson.fromJson(json, String.class);
                            reviewIdList = formatList(list);
                            frame.generateReviewList(reviewIdList);
                        }
                        else if(response.getStatus() == 401) {
                            System.out.println("401 error");
                        }
                        else if(response.getStatus() == 404) {
                            System.out.println("404 error");
                        }
                    }

                    @Override
                    public void failed(Throwable throwable) {
                        // internal client error, exit program
                        // TODO add better error messages
                        throwable.printStackTrace();
                        System.exit(1);
                    }
                });
    }

    private String[] formatList(String list){
        String[] s=list.split("\n");

        return s;
    }

    @Override
    public void previousFrame() {
        userController.frame.setVisible(true);
        frame.setVisible(false);
    }
}
