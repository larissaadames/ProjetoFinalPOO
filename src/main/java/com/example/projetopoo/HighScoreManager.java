package com.example.projetopoo;

import java.util.*;

public class HighScoreManager {

    private static HighScoreManager instance;

    public static HighScoreManager getInstance() {
        if (instance == null) instance = new HighScoreManager();
        return instance;
    }

    // Mapa: songId → lista de scores
    private final Map<String, List<ScoreEntry>> scores = new HashMap<>();

    private HighScoreManager() {
        carregarDoCSV();
    }

    private void carregarDoCSV() {
        List<ScoreEntry> lista = ScoreStorageCSV.loadScores();

        for (ScoreEntry e : lista) {
            scores.computeIfAbsent(e.getSongId(), k -> new ArrayList<>()).add(e);
        }

        ordenarTudo();
    }

    private void ordenarTudo() {
        for (List<ScoreEntry> l : scores.values()) {
            l.sort((a,b) -> Integer.compare(b.getScore(), a.getScore())); // maior primeiro
        }
    }

    public List<ScoreEntry> getScores(String songId) {
        return scores.getOrDefault(songId, new ArrayList<>());
    }

    /** Adiciona score e salva no CSV */
    public void addScore(String songId, String player, int score) {

        ScoreEntry entry = new ScoreEntry(player, score, songId);
        scores.computeIfAbsent(songId, k -> new ArrayList<>()).add(entry);

        ordenarTudo();

        // salvar persistência
        salvarCSV();
    }

    private void salvarCSV() {
        List<ScoreEntry> all = new ArrayList<>();

        for (var list : scores.values()) {
            all.addAll(list);
        }

        ScoreStorageCSV.saveScores(all);
    }
}
