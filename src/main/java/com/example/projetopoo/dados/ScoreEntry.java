package com.example.projetopoo.dados;

public class ScoreEntry implements Comparable<ScoreEntry> {

    private final String playerName;
    private final int score;

    public ScoreEntry(String playerName, int score) {
        this.playerName = playerName;
        this.score = score;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getScore() {
        return score;
    }

    @Override
    public int compareTo(ScoreEntry other) {
        // maior score primeiro
        return Integer.compare(other.score, this.score);
    }
}
