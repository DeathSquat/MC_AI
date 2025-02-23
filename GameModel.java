package com.eyantra.mind_cure_ai;

public class GameModel {
    private String gameName;
    private int gameImage;
    private Class<?> gameActivity;

    public GameModel(String gameName, int gameImage, Class<?> gameActivity) {
        this.gameName = gameName;
        this.gameImage = gameImage;
        this.gameActivity = gameActivity;
    }

    public String getGameName() {
        return gameName;
    }

    public int getGameImage() {
        return gameImage;
    }

    public Class<?> getGameActivity() {
        return gameActivity;
    }
}
