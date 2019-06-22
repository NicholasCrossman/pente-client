package controller;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import gui.frames.CreateGameFrame;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.InvocationCallback;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

public class CreateGameController implements CreateGameListener {
    CreateGameFrame cgf;
    Client client;
    String url = "http://127.0.0.1:5004";
    String userName;
    boolean publicVisibility = true;
    UserController userController;

    public CreateGameController(Client client, String userName,UserController uc) {
        cgf = new CreateGameFrame();
        this.client = client;
        this.userName = userName;
        userController=uc;
        cgf.setVisible(true);
        cgf.addCreateGameListener(this);
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
                            cgf.generateLeaderboard(leaderboard);
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

    public void createGame(String challengeUserName){
        Map<String, String> info = new HashMap<>();
        info.put("user2",challengeUserName);
        info.put("publicVisibility", Boolean.toString(publicVisibility));
        Gson gson = new Gson();
        Entity data = Entity.entity(gson.toJson(info), MediaType.APPLICATION_JSON);
        client.target(url)
                .path("/user/"+userName+"/games")
                .request()
                .async()
                .post(data, new postGame());
    }

    @Override
    public void setGameVisibility(boolean isPublic) {
        publicVisibility = isPublic;
    }


    @Override
    public void previousFrame() {
        userController.frame.setVisible(true);
        cgf.setVisible(false);
    }

    private class postGame implements InvocationCallback<Response>{

        @Override
        public void completed(Response response) {
            if (response.getStatus()==200) {
                Gson gson = new Gson();
                String gameID=gson.fromJson(response.readEntity(String.class),String.class);
                nextFrame(gameID);
            }
            else if (response.getStatus()==404){
                System.out.println("CreateGame Parameters Invalid");
            }
            //TODO any other error codes
        }

        @Override
        public void failed(Throwable throwable) {
            // internal client error, exit program
            // TODO add better error messages
            throwable.printStackTrace();
            System.exit(1);
        }

        public void nextFrame(String gameID){
            GameController g = new GameController(client, userName,userController);
            cgf.setVisible(false);
            g.enterGame(gameID);

        }

    }
}
