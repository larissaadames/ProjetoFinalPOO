package com.example.projetopoo;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class HighScoreManager {

    private static HighScoreManager instance;

    private final Map<String, List<ScoreEntry>> scores = new HashMap<>();
    private final String basePath = "scores"; // pasta na raiz do projeto

    public static HighScoreManager getInstance() {
        if (instance == null)
            instance = new HighScoreManager();
        return instance;
    }

    private HighScoreManager() {
        criarPastaSeNaoExiste();
        carregarTodosOsScores();
    }

    // ===========================================
    // CRIA A PASTA /scores SE NÃO EXISTIR
    // ===========================================
    private void criarPastaSeNaoExiste() {
        try {
            Files.createDirectories(Paths.get(basePath));
        } catch (IOException e) {
            System.err.println("Erro ao criar pasta de scores: " + e.getMessage());
        }
    }

    // ===========================================
    // CARREGA TODOS OS ARQUIVOS
    // ===========================================
    private void carregarTodosOsScores() {
        carregar("allstar");
        carregar("numb");
        carregar("bmtl");
    }

    // ===========================================
    // LER UM ARQUIVO CSV
    // ===========================================
    private void carregar(String musicaId) {

        String path = basePath + "/" + musicaId + ".csv";
        List<ScoreEntry> lista = new ArrayList<>();

        File file = new File(path);

        if (!file.exists()) {
            scores.put(musicaId, lista);
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            String linha;
            while ((linha = br.readLine()) != null) {
                String[] partes = linha.split(",");
                if (partes.length == 2) {
                    String nome = partes[0].trim();
                    int score = Integer.parseInt(partes[1].trim());

                    // COMPATÍVEL COM ScoreEntry(player, score, songId)
                    lista.add(new ScoreEntry(nome, score, musicaId));
                }
            }

        } catch (Exception e) {
            System.err.println("Erro lendo CSV de " + musicaId + ": " + e.getMessage());
        }

        lista.sort(Comparator.comparingInt(ScoreEntry::getScore).reversed());
        scores.put(musicaId, lista);
    }

    // ===========================================
    // SALVAR PARA CSV
    // ===========================================
    private void salvar(String musicaId) {

        String path = basePath + "/" + musicaId + ".csv";

        try (PrintWriter pw = new PrintWriter(new FileWriter(path))) {

            for (ScoreEntry se : scores.get(musicaId)) {
                pw.println(se.getPlayerName() + "," + se.getScore());
            }

        } catch (Exception e) {
            System.err.println("Erro escrevendo CSV de " + musicaId + ": " + e.getMessage());
        }
    }

    // ===========================================
    // ADICIONAR UM SCORE + SALVAR
    // ===========================================
    public void addScore(String musicaId, String player, int score) {

        List<ScoreEntry> lista = scores.get(musicaId);

        if (lista == null) {
            lista = new ArrayList<>();
            scores.put(musicaId, lista);
        }

        // AGORA COMPATÍVEL COM SEU CONSTRUTOR REAL
        lista.add(new ScoreEntry(player, score, musicaId));

        // mantém só top 10
        lista.sort(Comparator.comparingInt(ScoreEntry::getScore).reversed());
        if (lista.size() > 10)
            lista.subList(10, lista.size()).clear();

        salvar(musicaId);
    }

    // ===========================================
    // OBTER SCORES (usado pelo seletor)
    // ===========================================
    public List<ScoreEntry> getScores(String musicaId) {
        return scores.getOrDefault(musicaId, new ArrayList<>());
    }
}
