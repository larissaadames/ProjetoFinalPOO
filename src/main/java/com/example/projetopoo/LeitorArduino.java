package com.example.projetopoo;

import com.fazecast.jSerialComm.SerialPort;
import java.util.Scanner;

/*
 * CÓDIGO COMPLETO PARA O JAVA (com jSerialComm)
 * Se conecta a uma porta serial específica e imprime
 * tudo que o Arduino envia.
 * * O programa é mantido "vivo" por um loop no final da 'main'
 * para que a Thread de leitura possa continuar funcionando.
 */
public class LeitorArduino {

    // =================================================================
    //  IMPORTANTE: MUDE AQUI O NOME DA SUA PORTA SERIAL
    //  (Veja na IDE do Arduino qual é a porta correta)
    //  Exemplos: "COM3", "COM6"
    // =================================================================
    private static final String NOME_DA_PORTA = "COM6"; // <- MUDE AQUI!


    // Definimos a porta aqui para ser acessível pelo ShutdownHook
    private static SerialPort portaSerial;

    public static void main(String[] args) {

        System.out.println("Tentando conectar na porta: " + NOME_DA_PORTA);

        // 1. Obter a porta serial diretamente pelo nome
        portaSerial = SerialPort.getCommPort(NOME_DA_PORTA);

        // 2. Configurar a porta (igual ao Arduino)
        // O BaudRate DEVE ser 115200, igual ao Serial.begin(115200)
        portaSerial.setBaudRate(115200);

        // Esta é a configuração correta para ler linha por linha (println)


        // 3. Tentar abrir a porta
        if (!portaSerial.openPort()) {
            System.out.println("ERRO: Falha ao abrir a porta " + NOME_DA_PORTA);
            System.out.println("Verifique se o nome da porta está correto no código.");
            System.out.println("Verifique se o Arduino está conectado.");
            return;
        }

        System.out.println("Porta " + NOME_DA_PORTA+ " aberta com sucesso!");

        // 4. Iniciar a Thread de Leitura
        // Isso é essencial para não travar seu jogo
        Thread threadLeitura = new Thread(() -> {
            try (Scanner leitorSerial = new Scanner(portaSerial.getInputStream())) {

                // Esta thread fica "presa" aqui, esperando o Arduino
                while (leitorSerial.hasNextLine()) {
                    String linhaRecebida = leitorSerial.nextLine();

                    // Imprime a linha EXATA que o Arduino enviou
                    System.out.println("[ARDUINO]: " + linhaRecebida);
                }

            } catch (Exception e) {
                // Se você desconectar o Arduino, a porta fecha e dá um erro aqui
                System.out.println("Conexão com o Arduino perdida: " + e.getMessage());
            }
        });

        threadLeitura.start(); // Inicia a escuta

        // 5. Garantir que a porta feche ao sair
        // Isso é acionado quando você clica no 'Stop' do IntelliJ
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (portaSerial.isOpen()) {
                portaSerial.closePort();
                System.out.println("--- Porta serial fechada. ---");
            }
        }));

        // 6. Manter o programa principal vivo
        System.out.println("\n=======================================================");
        System.out.println("PROGRAMA PRONTO. OUVINDO O ARDUINO...");
        System.out.println("Pressione os botões para ver o input aparecer acima.");
        System.out.println("Para parar, clique no 'Stop' (quadrado vermelho) do IntelliJ.");
        System.out.println("=======================================================\n");

        // Este loop "dummy" impede a 'main' de terminar.
        while (portaSerial.isOpen()) {
            try {
                // Apenas "dorme" para não usar CPU
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                break;
            } catch (Exception e) {
                // Se a porta fechar por algum motivo (ex: desconectar o cabo)
                // o loop para.
                break;
            }
        }
    }
}