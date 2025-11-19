// Classe insana pra testar o arduino



package com.example.projetopoo;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortMessageListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import java.nio.charset.StandardCharsets;

public class LeitorArduino {

    // A porta serial precisa ser 'static' para ser acessível pelo 'main'
    // e pelo 'ShutdownHook' (que fecha o programa).
    private static SerialPort portaSerial;

    public static void main(String[] args) {

        // --- 1. Encontrar e Configurar a Porta ---

        // !!! MUDE "COM3" PARA A PORTA DO SEU ARDUINO !!!
        String nomePorta = "COM6";

        portaSerial = SerialPort.getCommPort(nomePorta);

        if (portaSerial == null) {
            System.out.println("Erro: Porta " + nomePorta + " nao encontrada.");
            return; // Encerra o programa
        }

        // Define o Baud Rate (deve ser igual ao 'Serial.begin(9600)' do Arduino)
        portaSerial.setBaudRate(115200);
        portaSerial.setNumDataBits(8);
        portaSerial.setNumStopBits(1);
        portaSerial.setParity(SerialPort.NO_PARITY);

        // --- 2. Abrir a Porta ---
        if (!portaSerial.openPort()) {
            System.out.println("Erro: Falha ao abrir a porta (verifique se ja esta em uso).");
            return;
        }

        System.out.println("Porta " + nomePorta + " aberta com sucesso.");
        System.out.println("Aguardando dados do Arduino...");

        // --- 3. Criar o "Ouvinte" (Listener) ---
        // Este é o núcleo do programa. Ele reage a eventos da porta serial.
        portaSerial.addDataListener(new SerialPortMessageListener() {

            @Override
            public int getListeningEvents() {
                // Define que queremos ser notificados quando dados chegarem
                return SerialPort.LISTENING_EVENT_DATA_RECEIVED;
            }

            @Override
            public byte[] getMessageDelimiter() {
                // Define que uma "mensagem completa" termina com o caractere '\n'
                // (quebra de linha), enviado pelo 'Serial.println()' do Arduino.
                return new byte[]{'\n'};
            }

            @Override
            public boolean delimiterIndicatesEndOfMessage() {
                return true;
            }

            @Override
            public void serialEvent(SerialPortEvent event) {
                // Este método é chamado automaticamente quando uma mensagem completa (com '\n') chega

                // Pega os bytes da mensagem
                byte[] dadosRecebidos = event.getReceivedData();

                // Converte os bytes para texto (String) e remove espaços/quebras de linha extras
                String mensagem = new String(dadosRecebidos, StandardCharsets.UTF_8).trim();

                // Ação principal: Imprime a mensagem recebida diretamente no terminal
                System.out.println(mensagem);
            }
        });

        // --- 4. Garantir que a porta feche ao sair ---
        // Adiciona um "gancho de desligamento" (Shutdown Hook).
        // Este código é executado quando você fecha o programa (ex: Ctrl+C no terminal).
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (portaSerial.isOpen()) {
                portaSerial.closePort();
                System.out.println("\nPorta serial fechada.");
            }
        }));

        // --- 5. Manter o programa principal "vivo" ---
        // O Listener acima roda em sua própria thread (plano de fundo).
        // A thread 'main' (principal) terminaria aqui e o programa fecharia.
        // Este loop 'while(true)' impede que a thread 'main' termine,
        // mantendo o programa rodando para sempre e ouvindo a porta serial.
        try {
            while (true) {
                // Apenas "dorme" para não consumir CPU
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            // Se a thread for interrompida, o programa sai do loop e termina
            System.out.println("Programa interrompido.");
        }
    }
}