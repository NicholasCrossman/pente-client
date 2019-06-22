package controller;


import com.google.gson.Gson;
import gui.frames.UserFrame;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.InvocationCallback;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.concurrent.Future;

public class UserController implements UserListener{
    UserFrame frame;
    String userName;
    Client client;
    String url = "http://152.117.176.5:5004";

    public UserController(Client client, String userName) {
        frame = new UserFrame();
        this.client = client;
        frame.setVisible(true);
        this.userName = userName;
        frame.addUserListener(this);
        generateUser();
    }
    public String setUser(){
        return "";
    }
    public String setRecord(){
        return "";
    }
    @Override
    public void nextFrame(){
        CreateGameController createGameController = new CreateGameController(client, userName,this);
        frame.setVisible(false);
    }
    @Override
    public void nextFrame2(){
        JoinGameController gameController = new JoinGameController(client, userName,this);
        frame.setVisible(false);
    }
    @Override
    public void openLeaderboard(){
        LeaderboardController gameController = new LeaderboardController(client);
    }
    public void generateUser(){
        Gson gson = new Gson();
        final Future<Response> future = client.target(url)
                .path("/user/"+userName)
                .request(MediaType.APPLICATION_JSON)
                .async()
                .get(new InvocationCallback<Response>() {
                    @Override
                    public void completed(Response response) {
                        if(response.getStatus() == 200) {
                            // login successful, username and password authenticated
                            String json = response.readEntity(String.class);
                            String r = gson.fromJson(json, String.class);
                            frame.generateUser(r.split("_"));
                        }
                        else if(response.getStatus() == 401) {
                        }
                        else if(response.getStatus() == 404) {

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
}

