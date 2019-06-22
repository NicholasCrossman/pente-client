package gui;


import controller.*;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

public class Main {
    ///game/Jim/games/Jim_Player1_

    public static void main(String[] args) {
        String url = "http://152.117.176.5:5004";
        Client client = ClientBuilder.newClient();
        WelcomeController welcomeController = new WelcomeController(client);
    }
}
