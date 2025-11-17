package com.example.projetopoo;// Importa as classes necessárias da biblioteca jSerialComm
// SerialPort é a classe principal para representar e interagir com uma porta serial
import com.fazecast.jSerialComm.SerialPort;
// SerialPortMessageListener é uma interface que nos permite ouvir por "mensagens" completas
import com.fazecast.jSerialComm.SerialPortMessageListener;
// SerialPortEvent contém a informação sobre o evento que ocorreu (ex: dados recebidos)
import com.fazecast.jSerialComm.SerialPortEvent;

// Importa as classes necessárias do JavaFX
// Application é a classe base para qualquer app JavaFX
import javafx.application.Application;
// Platform nos permite executar código na "thread" principal da UI (Interface Gráfica)
import javafx.application.Platform;
// Scene é o "palco" onde todos os elementos visuais são colocados
import javafx.scene.Scene;
// Label é um componente de texto simples
import javafx.scene.control.Label;
// VBox é um layout que organiza os componentes verticalmente
import javafx.scene.layout.VBox;
// Stage é a "janela" principal da aplicação
import javafx.stage.Stage;

// Importa classes para lidar com conversão de bytes para texto
import java.nio.charset.StandardCharsets;

// Nossa classe principal. Ela "herda" da classe Application do JavaFX.
public class LeitorArduino extends Application {

    // Declara uma variável para guardar a referência da nossa porta serial
    private SerialPort portaSerial;

    // Declara e já inicializa um Label (texto) do JavaFX.
    // Este Label será atualizado com os dados do Arduino.
    private Label dadosLabel = new Label("Aguardando dados...");

    // O método 'main' é o ponto de entrada padrão de qualquer aplicação Java.
    // O 'launch(args)' é o método do JavaFX que inicia a aplicação gráfica.
    public static void main(String[] args) {
        launch(args);
    }

    // Este método 'start' é chamado automaticamente pelo 'launch'
    // Ele é o ponto de entrada da parte *gráfica* do JavaFX.
    @Override
    public void start(Stage primaryStage) {

        // Cria um layout VBox (Vertical Box) com espaçamento de 20 pixels
        // e já adiciona nosso 'dadosLabel' dentro dele.
        VBox root = new VBox(20, dadosLabel);

        // Cria a "cena" principal, definindo o tamanho da janela (300x200)
        // e colocando nosso layout 'root' nela.
        Scene scene = new Scene(root, 300, 200);

        // Define o título que aparece na barra superior da janela
        primaryStage.setTitle("Leitor Arduino");

        // Informa à "janela" (primaryStage) qual "cena" (scene) ela deve mostrar
        primaryStage.setScene(scene);

        // Finalmente, torna a janela visível
        primaryStage.show();

        // Chama nosso método customizado para configurar a comunicação serial
        iniciarConexaoSerial();

        // Adiciona um "ouvinte" para o evento de fechar a janela
        // Isso é MUITO importante para liberar a porta serial quando o app fechar.
        primaryStage.setOnCloseRequest(event -> {
            // Verifica se a porta foi inicializada e se está aberta
            if (portaSerial != null && portaSerial.isOpen()) {
                // Fecha a porta serial
                portaSerial.closePort();
                System.out.println("Porta serial fechada.");
            }
        });
    }

    // Nosso método customizado para cuidar de toda a lógica da porta serial
    private void iniciarConexaoSerial() {

        // 1. Encontrar a porta
        // !!! ATENÇÃO: MUDE "COM3" PARA A PORTA DO SEU ARDUINO !!!
        // (Ex: "COM4" no Windows, "/dev/ttyUSB0" ou "/dev/ttyACM0" no Linux)
        String nomePorta = "COM6";

        // Pega a instância da porta serial com base no nome
        portaSerial = SerialPort.getCommPort(nomePorta);

        // Se a porta não for encontrada (ou o nome estiver errado), 'portaSerial' será null
        if (portaSerial == null) {
            // Atualiza o Label com o erro
            dadosLabel.setText("Não foi possível encontrar a porta do Arduino.");
            // Encerra o método, pois não há mais o que fazer
            return;
        }

        // 2. Configurar a porta
        // Define o Baud Rate. Deve ser EXATAMENTE O MESMO do 'Serial.begin(9600)' no Arduino.
        portaSerial.setBaudRate(115200);
        // Configurações padrão da comunicação serial
        portaSerial.setNumDataBits(8);
        portaSerial.setNumStopBits(1);
        portaSerial.setParity(SerialPort.NO_PARITY);

        // 3. Abrir a porta
        // Tenta abrir a porta. Retorna 'false' se falhar (ex: porta já em uso)
        if (!portaSerial.openPort()) {
            // Atualiza o Label com o erro
            dadosLabel.setText("Falha ao abrir a porta serial.");
            return;
        }

        // Se chegou até aqui, a porta abriu com sucesso
        System.out.println("Porta serial aberta: " + portaSerial.getSystemPortName());
        dadosLabel.setText("Conectado. Aguardando...");

        // 4. Criar o "Ouvinte" (Listener)
        // Adicionamos um "ouvinte" à porta. Usamos a versão "MessageListener"
        // que é mais inteligente e espera por uma "mensagem completa".
        portaSerial.addDataListener(new SerialPortMessageListener() {

            // Este método diz ao listener quais eventos nos interessam.
            // Queremos o 'LISTENING_EVENT_DATA_RECEIVED'.
            @Override
            public int getListeningEvents() {
                return SerialPort.LISTENING_EVENT_DATA_RECEIVED;
            }

            // Este é o método mais importante do MessageListener.
            // Ele define o "delimitador" da mensagem.
            // Como usamos 'Serial.println()' no Arduino, ele envia um '\n' (quebra de linha) no final.
            // Estamos dizendo: "uma mensagem completa termina com o byte '\n'".
            @Override
            public byte[] getMessageDelimiter() {
                return new byte[]{'\n'};
            }

            // Confirma que o delimitador (o '\n') indica o fim da mensagem.
            @Override
            public boolean delimiterIndicatesEndOfMessage() {
                return true;
            }

            // Este método é chamado AUTOMATICAMENTE pela jSerialComm
            // toda vez que uma mensagem completa (terminada em '\n') é recebida.
            @Override
            public void serialEvent(SerialPortEvent event) {

                // Pega os dados que foram recebidos (já sem o delimitador '\n')
                byte[] dadosRecebidos = event.getReceivedData();

                // Converte o array de bytes para uma String legível, usando o padrão UTF-8
                // .trim() remove espaços em branco ou caracteres de controle no início/fim
                String mensagem = new String(dadosRecebidos, StandardCharsets.UTF_8).trim();

                // Imprime no console (terminal) do Java para fins de debug
                System.out.println("Recebido: " + mensagem);

                // ### PONTO CRÍTICO ###
                // Este código (serialEvent) roda em uma "thread" separada,
                // a thread da porta serial.
                // O JavaFX proíbe que threads "de fora" mexam na interface gráfica (UI).
                // Para atualizar o Label com segurança, usamos 'Platform.runLater()'.
                // Isso coloca a atualização do Label na "fila de tarefas" da
                // thread principal do JavaFX.
                Platform.runLater(() -> {
                    // Este código aqui dentro é executado com segurança na thread da UI
                    dadosLabel.setText("Dado do Arduino: " + mensagem);
                });
            }
        });
    }

    // (Opcional) Esta é a função que tenta adivinhar a porta do Arduino
    private SerialPort encontrarPortaArduino() {
        // Pega um array com todas as portas seriais disponíveis no PC
        SerialPort[] portas = SerialPort.getCommPorts();
        System.out.println("Procurando Arduino nas portas:");

        // Faz um loop por todas as portas encontradas
        for (SerialPort porta : portas) {
            // Imprime o nome "amigável" da porta (ex: "USB-SERIAL CH340 (COM3)")
            System.out.println("- " + porta.getDescriptivePortName());

            // Verifica se o nome amigável contém "Arduino" ou "USB-SERIAL"
            if (porta.getDescriptivePortName().contains("Arduino") ||
                    porta.getDescriptivePortName().contains("USB-SERIAL")) {

                // Se sim, encontramos!
                System.out.println("Arduino encontrado em: " + porta.getSystemPortName());
                // Retorna a porta encontrada
                return porta;
            }
        }
        // Se o loop terminar e não encontrar nada, retorna null
        return null;
    }
} // Fim da classe ArduinoListener