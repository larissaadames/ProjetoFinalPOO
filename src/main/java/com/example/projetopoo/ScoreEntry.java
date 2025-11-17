package com.example.projetopoo;

public class ScoreEntry {

    private final String playerName;
    private final int score;
    private final String songId;

    public ScoreEntry(String playerName, int score, String songId) {
        this.playerName = playerName;
        this.score = score;
        this.songId = songId;
    }

    public String getPlayerName() { return playerName; }
    public int getScore() { return score; }
    public String getSongId() { return songId; }
}
