package controller;

import com.google.gson.Gson;
import gui.frames.LoginFrame;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.InvocationCallback;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

public class LoginController implements LoginListener {
    Client client;
    LoginFrame frame;
    String url = "http://152.117.176.5:5004";
    WelcomeController welcome;

    public LoginController(Client c,WelcomeController w) {
        frame = new LoginFrame();
        client = c;
        welcome=w;
        frame.addLoginListener(this);
        frame.setVisible(true);
    }

    public void exitLogin() {
        frame.setVisible(false);
        welcome.frame.setVisible(false);
    }

    @Override
    public void loginChanged(String username, String password) {
        // put the username and password in a HashMap and convert it to JSON
        Map<String, String> info = new HashMap<>();
        info.put("userName", username);
        info.put("password", password);
        Gson gson = new Gson();
        Entity data = Entity.entity(gson.toJson(info), MediaType.APPLICATION_JSON);

        Client client = ClientBuilder.newClient();
        final Future<Response> future = client.target(url)
                .path("user/login")
                .request()
                .async()
                .put(data, new InvocationCallback<Response>() {
                    @Override
                    public void completed(Response response) {
                        if(response.getStatus() == 200) {
                            // login successful, username and password authenticated
                            exitLogin(); // set the current window to invisible
                            // call the controller of the next frame
                            UserController userController = new UserController(client, username);
                        }
                        else if(response.getStatus() == 401) {
                            // User exists, but password incorrect
                            // Set error response field and clear password
                            frame.incorrectPasswordMessage();
                        }
                        else if(response.getStatus() == 404) {
                            // User not found, clear both input fields
                            frame.incorrectUsernameMessage();
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

    @Override
    public void newUserChanged(String username, String password) {
        // put the username and password in a HashMap and convert it to JSON
        Map<String, String> info = new HashMap<>();
        info.put("userName", username);
        info.put("password", password);
        Gson gson = new Gson();
        Entity data = Entity.entity(gson.toJson(info), MediaType.APPLICATION_JSON);

        Client client = ClientBuilder.newClient();
        final Future<Response> future = client.target(url)
                .path("user")
                .request()
                .async()
                .post(data, new InvocationCallback<Response>() {
                    @Override
                    public void completed(Response response) {
                        if(response.getStatus() == 200) {
                            // user creation successful, username and password authenticated
                            exitLogin(); // set the current window to invisible
                            // call the controller of the next frame
                            UserController userController = new UserController(client, username);
                        }
                        else if(response.getStatus() == 401) {
                            // User with that name already exists
                            frame.incorrectNewUsernameMessage();
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
