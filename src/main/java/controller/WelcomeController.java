package controller;


import com.google.gson.Gson;
import gui.frames.WelcomeFrame;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.InvocationCallback;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.concurrent.Future;

public class WelcomeController implements WelcomeListener{
    WelcomeFrame frame;

    Client client;
    String url = "http://152.117.176.5:5004";

    public WelcomeController(Client client) {
        frame = new WelcomeFrame();
        this.client = client;
        frame.setVisible(true);
        frame.addWelcomeListener(this);
        generateLeaderboard();
    }


    public void generateLeaderboard(){
        Gson gson = new Gson();
        final Future<Response> future = client.target(url)
                .path("/leaderboard")
                .request(MediaType.APPLICATION_JSON)
                .async()
                .get(new InvocationCallback<Response>() {
                    @Override
                    public void completed(Response response) {
                        if(response.getStatus() == 200) {
                            // login successful, username and password authenticated
                            String json = response.readEntity(String.class);
                            String leaderboard = gson.fromJson(json, String.class);
                            frame.generateLeaderboard(leaderboard);
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

        //frame.generateLeaderboard(leaderboard);
    }
    public void nextFrame(){
        LoginController loginController = new LoginController(client,this);
        //frame.setVisible(false);
    }
}

