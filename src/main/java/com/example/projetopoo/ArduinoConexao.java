package com.example.projetopoo;

import com.example.projetopoo.jogo.core.InputHandler;
import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import javafx.application.Platform;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class ArduinoConexao {

    private static ArduinoConexao instancia; // Singleton para acessar de qualquer lugar
    private SerialPort portaSerial;

    // O InputHandler só existe durante a música. Quando no menu, ele é null.
    private InputHandler inputHandlerAtual;

    private ArduinoConexao() {
        // Construtor privado
    }

    public static ArduinoConexao getInstance() {
        if (instancia == null) {
            instancia = new ArduinoConexao();
        }
        return instancia;
    }

    public void setInputHandler(InputHandler handler) {
        this.inputHandlerAtual = handler;
    }

    public void iniciar() {
        if (portaSerial != null && portaSerial.isOpen()) return; // Já está aberto

        SerialPort[] portas = SerialPort.getCommPorts();
        if (portas.length == 0) {
            System.out.println("Nenhum Arduino encontrado.");
            return;
        }

        portaSerial = portas[0];
        portaSerial.setBaudRate(115200);
        portaSerial.setComPortTimeouts(SerialPort.TIMEOUT_NONBLOCKING, 0, 0);

        if (portaSerial.openPort()) {
            System.out.println("Arduino Conectado: " + portaSerial.getSystemPortName());
            configurarOuvinte();
        }
    }

    public void fechar() {
        if (portaSerial != null) portaSerial.closePort();
    }

    private void configurarOuvinte() {
        portaSerial.addDataListener(new SerialPortDataListener() {
            @Override
            public int getListeningEvents() { return SerialPort.LISTENING_EVENT_DATA_RECEIVED; }

            @Override
            public void serialEvent(SerialPortEvent event) {
                byte[] dados = event.getReceivedData();
                for (byte b : dados) {
                    char c = (char) b;
                    Platform.runLater(() -> processarComando(c));
                }
            }
        });
    }

    private void processarComando(char comando) {
        // --- 1. COMANDOS DE NAVEGAÇÃO (Funcionam sempre) ---
        switch (comando) {
            case 'C' -> simularTecla(KeyCode.UP);   // Botão Cima
            case 'B' -> simularTecla(KeyCode.DOWN); // Botão Baixo
            case 'S' -> simularTecla(KeyCode.ENTER);// Botão Redondo
            // Adicione Left/Right se quiser mapear mais botões futuramente
        }

        // --- 2. COMANDOS DE JOGO (Só funcionam se estiver jogando) ---
        if (inputHandlerAtual != null) {
            switch (comando) {
                case 'D' -> inputHandlerAtual.receberInputArduino(1, true);
                case 'F' -> inputHandlerAtual.receberInputArduino(2, true);
                case 'J' -> inputHandlerAtual.receberInputArduino(3, true);
                case 'K' -> inputHandlerAtual.receberInputArduino(4, true);
                case 'L' -> inputHandlerAtual.receberInputArduino(5, true);

                case 'd' -> inputHandlerAtual.receberInputArduino(1, false);
                case 'f' -> inputHandlerAtual.receberInputArduino(2, false);
                case 'j' -> inputHandlerAtual.receberInputArduino(3, false);
                case 'k' -> inputHandlerAtual.receberInputArduino(4, false);
                case 'l' -> inputHandlerAtual.receberInputArduino(5, false);
            }
        }
    }

    // Simula um aperto de tecla no JavaFX
    private void simularTecla(KeyCode code) {
        Stage stage = ControladorFluxo.getStageAtual();
        if (stage != null && stage.getScene() != null) {
            // Dispara evento de PRESSIONAR
            stage.getScene().getRoot().fireEvent(new KeyEvent(
                    KeyEvent.KEY_PRESSED, "", "", code,
                    false, false, false, false
            ));
            // Dispara evento de SOLTAR (para completar o clique)
            stage.getScene().getRoot().fireEvent(new KeyEvent(
                    KeyEvent.KEY_RELEASED, "", "", code,
                    false, false, false, false
            ));
        }
    }
}