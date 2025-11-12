package com.example.projetopoo;

import java.time.LocalDateTime;

public class Jogador {
    private String nickName;
    private float scoreMax;
    private int ID;
    private LocalDateTime criadoEm;
    private LocalDateTime ultimaPartida;
    private int totalPartidas;

    public Jogador(String nickName, float scoreMax, int ID, LocalDateTime criadoEm, LocalDateTime ultimaPartida, int totalPartidas) {
        this.nickName = nickName;
        this.scoreMax = scoreMax;
        this.ID = ID;
        this.criadoEm = criadoEm;
        this.ultimaPartida = ultimaPartida;
        this.totalPartidas = totalPartidas;
    }



    public int getTotalPartidas() {
        return totalPartidas;
    }

    public void setTotalPartidas(int totalPartidas) {
        this.totalPartidas = totalPartidas;
    }

    public LocalDateTime getUltimaPartida() {
        return ultimaPartida;
    }

    public void setUltimaPartida(LocalDateTime ultimaPartida) {
        this.ultimaPartida = ultimaPartida;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public float getScoreMax() {
        return scoreMax;
    }

    public void setScoreMax(float scoreMax) {
        this.scoreMax = scoreMax;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

}
