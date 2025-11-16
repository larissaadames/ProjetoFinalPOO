package com.example.projetopoo;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

public class ScoreStorageCSV {

    private static final String CSV_PATH = "/data/scores.csv";

    /** Lê o CSV e retorna uma lista de entradas */
    public static List<ScoreEntry> loadScores() {
        List<ScoreEntry> lista = new ArrayList<>();

        try {
            InputStream in = ScoreStorageCSV.class.getResourceAsStream(CSV_PATH);

            if (in == null) {
                System.out.println("[CSV] Arquivo não encontrado, criando vazio.");
                saveScores(lista);   // cria arquivo vazio
                return lista;
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));

            String line;
            boolean first = true;

            while ((line = reader.readLine()) != null) {

                if (first) { // pula cabeçalho
                    first = false;
                    continue;
                }

                String[] parts = line.split(",");

                if (parts.length != 3) continue;

                String song = parts[0].trim();
                String player = parts[1].trim();
                int score = Integer.parseInt(parts[2].trim());

                lista.add(new ScoreEntry(player, score, song));
            }

            reader.close();
            return lista;

        } catch (Exception e) {
            e.printStackTrace();
            return lista;
        }
    }

    /** Sobrescreve o arquivo CSV com todas as entradas */
    public static void saveScores(List<ScoreEntry> entries) {

        try {
            // resolve caminho absoluto dentro do target
            String path = Objects.requireNonNull(
                    ScoreStorageCSV.class.getResource("/data")
            ).getPath() + "/scores.csv";

            BufferedWriter writer = new BufferedWriter(new FileWriter(path, false));

            writer.write("songId,player,score\n");

            for (ScoreEntry e : entries) {
                writer.write(String.format("%s,%s,%d\n",
                        e.getSongId(),
                        e.getPlayerName(),
                        e.getScore()
                ));
            }

            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
