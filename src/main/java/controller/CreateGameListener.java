package controller;

public interface CreateGameListener {
    void generateLeaderboard();
    void createGame(String user2);
    void setGameVisibility(boolean isPublic);
    void previousFrame();
}
