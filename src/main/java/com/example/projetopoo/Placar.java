package com.example.projetopoo;

public class Placar implements Comparable<Placar> {
    public String name;
    public int score;
    public String date;

    public Placar(String name, int score, String date) {
        this.name = name;
        this.score = score;
        this.date = date;
    }

    @Override
    public int compareTo(Placar o) {
        return Integer.compare(o.score, this.score); // maior primeiro
    }
}

