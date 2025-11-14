package com.example.projetopoo;

import java.util.*;

public class HighScoreManager {

    private static final HighScoreManager INSTANCE = new HighScoreManager();

    // chave = id da música ("allstar", "numb", "bmtl")
    private final Map<String, List<ScoreEntry>> scores = new HashMap<>();

    private HighScoreManager() {
        // DADOS FAKE PARA TESTE INICIAL
        addScore("allstar", "Quint", 98765);
        addScore("allstar", "Player2", 85000);
        addScore("allstar", "AAA", 70000);

        addScore("numb", "Eu", 123456);
        addScore("numb", "XYZ", 95000);

        addScore("bmtl", "Larica", 110000);
        addScore("bmtl", "Noob", 50000);
    }

    public static HighScoreManager getInstance() {
        return INSTANCE;
    }

    public synchronized void addScore(String songId, String playerName, int score) {
        List<ScoreEntry> list = scores.computeIfAbsent(songId, k -> new ArrayList<>());
        list.add(new ScoreEntry(playerName, score));
        Collections.sort(list); // usa compareTo (maior primeiro)
        // se quiser guardar só os top 100, 50, etc., corta aqui
    }

    /** Retorna uma lista imutável com todos os scores da música (ordenados) */
    public synchronized List<ScoreEntry> getScores(String songId) {
        return Collections.unmodifiableList(scores.getOrDefault(songId, Collections.emptyList()));
    }

    /** Versão já limitada (ex: top 10) */
    public synchronized List<ScoreEntry> getTopScores(String songId, int limit) {
        List<ScoreEntry> list = scores.getOrDefault(songId, Collections.emptyList());
        int toIndex = Math.min(limit, list.size());
        return Collections.unmodifiableList(list.subList(0, toIndex));
    }

    // Futuro: métodos load()/save() para persistir em arquivo.
}
