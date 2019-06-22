package controller;

public interface LoginListener {
    void loginChanged(String username, String password);
    void newUserChanged(String username, String password);
}
