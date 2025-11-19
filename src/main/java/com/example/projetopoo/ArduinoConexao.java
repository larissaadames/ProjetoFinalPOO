package com.example.projetopoo;

import com.example.projetopoo.jogo.core.InputHandler;
import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener; // Importante: DataListener genérico
import com.fazecast.jSerialComm.SerialPortEvent;
import javafx.application.Platform;

public class ArduinoConexao {

    private SerialPort portaSerial;
    private final InputHandler inputHandler;

    public ArduinoConexao(InputHandler inputHandler) {
        this.inputHandler = inputHandler;
    }

    public void iniciar() {
        SerialPort[] portas = SerialPort.getCommPorts();

        if (portas.length == 0) {
            System.out.println("Nenhuma porta serial encontrada.");
            return;
        }

        portaSerial = portas[0]; // Pega a primeira porta (ou force "COM6" se precisar)
        portaSerial.setBaudRate(115200); // Deve bater com o Arduino

        // Configurações para latência mínima
        portaSerial.setComPortTimeouts(SerialPort.TIMEOUT_NONBLOCKING, 0, 0);

        if (portaSerial.openPort()) {
            System.out.println("Arduino conectado (Modo Rápido): " + portaSerial.getSystemPortName());
            configurarOuvinte();
        } else {
            System.out.println("Falha ao abrir a porta.");
        }
    }

    public void fechar() {
        if (portaSerial != null && portaSerial.isOpen()) {
            portaSerial.closePort();
        }
    }

    private void configurarOuvinte() {
        // Usamos a interface genérica para ler dados brutos assim que chegam
        portaSerial.addDataListener(new SerialPortDataListener() {
            @Override
            public int getListeningEvents() {
                return SerialPort.LISTENING_EVENT_DATA_RECEIVED;
            }

            @Override
            public void serialEvent(SerialPortEvent event) {
                byte[] dados = event.getReceivedData();

                // Processa cada byte individualmente
                for (byte b : dados) {
                    char comando = (char) b;

                    // Executa no JavaFX Thread
                    Platform.runLater(() -> processarByte(comando));
                }
            }
        });
    }

    private void processarByte(char comando) {
        // Mapeamento:
        // Maiúsculas = Pressionar (true)
        // Minúsculas = Soltar (false)

        switch (comando) {
            // Pressionar
            case 'D' -> inputHandler.receberInputArduino(1, true);
            case 'F' -> inputHandler.receberInputArduino(2, true);
            case 'J' -> inputHandler.receberInputArduino(3, true);
            case 'K' -> inputHandler.receberInputArduino(4, true);
            case 'L' -> inputHandler.receberInputArduino(5, true);

            // Soltar
            case 'd' -> inputHandler.receberInputArduino(1, false);
            case 'f' -> inputHandler.receberInputArduino(2, false);
            case 'j' -> inputHandler.receberInputArduino(3, false);
            case 'k' -> inputHandler.receberInputArduino(4, false);
            case 'l' -> inputHandler.receberInputArduino(5, false);
        }
    }
}