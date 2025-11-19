package com.example.projetopoo;

import com.example.projetopoo.dados.ScoreEntry;
import com.example.projetopoo.exceptions.SongNotFoundException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class HighScoreManager {

    private static final HighScoreManager INSTANCE = new HighScoreManager();

    // 1: Definir apenas o nome do arquivo como String estática
    private static final String SCORES_FILENAME = "highscores.csv";

    // chave = id da música ("allstar", "numb", "bmtl")
    private final Map<String, List<ScoreEntry>> scores = new HashMap<>();

    private HighScoreManager() {
        loadScores();
    }

    public static HighScoreManager getInstance() {
        return INSTANCE;

    }

    // ==========================
    // MÉTODOS DE PERSISTÊNCIA
    // ==========================

    /**
     * Carrega os scores do arquivo CSV.
     * Trata exceções internamente para não quebrar a inicialização do JavaFX.
     */
    private void loadScores() {
        // MUDANÇA 2: Criar o objeto Path DENTRO do metodo (inicialização segura)
        Path scoresFile = Paths.get(SCORES_FILENAME);

        // Se a lista de scores já não estiver vazia, não carrega novamente
        if (!scores.isEmpty()) return;

        // Usa o Path criado no metodo
        if (!Files.exists(scoresFile)) {
            System.out.println("Arquivo de scores (" + scoresFile.toAbsolutePath() + ") não encontrado. Iniciando com scores vazios.");
            return;
        }

        try (BufferedReader reader = Files.newBufferedReader(scoresFile)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    try {
                        String songId = parts[0].trim();
                        String playerName = parts[1].trim();
                        int score = Integer.parseInt(parts[2].trim());

                        List<ScoreEntry> list = scores.computeIfAbsent(songId, k -> new ArrayList<>());
                        list.add(new ScoreEntry(playerName, score));

                    } catch (NumberFormatException e) {
                        System.err.println("Aviso: Ignorando linha com formato de score inválido: " + line);
                    }
                } else {
                    System.err.println("Aviso: Ignorando linha com número incorreto de campos: " + line);
                }
            }
            scores.values().forEach(Collections::sort);
            System.out.println("Scores carregados com sucesso.");

        } catch (IOException e) {
            System.err.println("ERRO FATAL ao ler o arquivo de scores. Continuando com scores vazios.");
            e.printStackTrace();
            scores.clear();
        }
    }

    // Salva todos os scores no arquivo CSV.
    private synchronized void saveScores() {

        Path scoresFile = Paths.get(SCORES_FILENAME);
        List<String> lines = new ArrayList<>();

        for (Map.Entry<String, List<ScoreEntry>> entry : scores.entrySet()) {
            String songId = entry.getKey();
            for (ScoreEntry scoreEntry : entry.getValue()) {
                lines.add(String.format("%s,%s,%d",
                        songId, scoreEntry.getPlayerName(), scoreEntry.getScore()));
            }
        }

        try (BufferedWriter writer = Files.newBufferedWriter(scoresFile)) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar o arquivo de scores: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ==========================
    // MÉTODOS PRINCIPAIS
    // ==========================

    public synchronized void addScore(String songId, String playerName, int score) {

        if (songId == null || songId.isBlank())
            throw new SongNotFoundException("songId nulo ou vazio ao adicionar score.");

        List<ScoreEntry> list = scores.computeIfAbsent(songId, k -> new ArrayList<>());
        list.add(new ScoreEntry(playerName, score));
        Collections.sort(list);

        saveScores();
    }

    public synchronized List<ScoreEntry> getScores(String songId) {

        if (songId == null || songId.isBlank())
            throw new SongNotFoundException("songId nulo ou vazio ao buscar scores.");

        return Collections.unmodifiableList(scores.getOrDefault(songId, Collections.emptyList()));
    }

    public synchronized List<ScoreEntry> getTopScores(String songId, int limit) {

        if (songId == null || songId.isBlank())
            throw new SongNotFoundException("songId nulo ou vazio ao buscar top scores.");

        List<ScoreEntry> list = scores.getOrDefault(songId, Collections.emptyList());
        int toIndex = Math.min(limit, list.size());
        return Collections.unmodifiableList(list.subList(0, toIndex));
    }

    // Metodo público para forçar o salvamento ao sair da aplicação.
    public synchronized void persistAll() {
        saveScores();
        System.out.println("Persistência final concluída.");
    }

}