package gui;


import controller.*;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

public class Main {

    public static void main(String[] args) {
        Client client = ClientBuilder.newClient();
        WelcomeController welcomeController = new WelcomeController(client);
    }
}
