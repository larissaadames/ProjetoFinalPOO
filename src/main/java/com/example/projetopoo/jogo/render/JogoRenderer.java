package com.example.projetopoo.jogo.render;

import com.example.projetopoo.jogo.core.JogoEstado;
import com.example.projetopoo.jogo.core.Layout;
import com.example.projetopoo.jogo.logica.JogoLogica;
import com.example.projetopoo.jogo.logica.Julgamento;
import com.example.projetopoo.jogo.notas.Nota;
import com.example.projetopoo.jogo.notas.NotaHold;
import com.example.projetopoo.jogo.notas.NotaTap;
import javafx.animation.*;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.*;

public class JogoRenderer {
    private final List<HitDot> hitDots = new ArrayList<>();

    private final Map<Nota, INotaSprite> spritesMap = new HashMap<>();

    private final Queue<NotaTapSprite> tapPool = new LinkedList<>();
    private final Queue<NotaHoldSprite> holdPool = new LinkedList<>();

    private final Group root = new Group();
    private final Group grupoNotas = new Group();

    private final Text textScore = new Text("000000");
    private final Text textCombo = new Text("0");
    private final Text textFeedback = new Text("");
    private final Text textMultiplicador = new Text("1x");
    private final Text textFPS = new Text("FPS: 0");

    private final ImageView imageViewEfeito = new ImageView();

    public JogoRenderer(JogoEstado estado) {
        root.resize(1920, 1080);

        Rectangle hitLine = new Rectangle(Layout.INICIO_X - 20, Layout.HIT_LINE, Layout.AREA_JOGO_SIZE, 20);
        hitLine.setFill(Color.GOLDENROD);

        // Ordem de adição: Fundo -> Notas -> Interface -> Mascote
        root.getChildren().add(hitLine);
        root.getChildren().add(grupoNotas);

        criarHitDots();
        configurarFPS();

        // Configura a imagem (invisível no início)
        configurarImageViewEfeito();

        // Configura os textos e listeners (CHAMAR APENAS UMA VEZ!)
        configurarInterface(estado);
    }

    private void configurarFPS() {
        // Tente usar a fonte carregada se existir, senão usa padrão
        textFPS.setFont(Font.font("8BitPusab", FontWeight.BOLD, 20));
        textFPS.setFill(Color.LIME);
        textFPS.setX(10);
        textFPS.setY(30);
        root.getChildren().add(textFPS);
    }

    private void configurarInterface(JogoEstado estado) {
        // 1. Placar
        textScore.setFont(Font.font("Consolas", FontWeight.BOLD, 40));
        textScore.setFill(Color.WHITE);
        textScore.setX(1600);
        textScore.setY(100);
        textScore.textProperty().bind(estado.scoreProperty().asString("%06d"));

        // 2. Combo
        textCombo.setFont(Font.font("Verdana", FontWeight.BOLD, 60));
        textCombo.setFill(Color.WHITE);
        textCombo.setX(200);
        textCombo.setY(500);
        textCombo.setVisible(false);

        estado.comboProperty().addListener((obs, velho, novo) -> {
            int val = novo.intValue();
            textCombo.setText(val + "x");
            textCombo.setVisible(val > 1);
            if (val > 1) animarTexto(textCombo, 1.05);
        });

        // 3. Feedback
        textFeedback.setFont(Font.font("Impact", 80));
        textFeedback.setStroke(Color.BLACK);
        textFeedback.setStrokeWidth(2);
        textFeedback.setX(Layout.INICIO_X + 200);
        textFeedback.setY(600);
        textFeedback.setOpacity(0);

        // 4. Multiplicador
        textMultiplicador.setFont(Font.font("Impact", 50));
        textMultiplicador.setFill(Color.WHITE);
        textMultiplicador.setX(1600);
        textMultiplicador.setY(160);
        textMultiplicador.setVisible(false);

        estado.multiplicadorProperty().addListener((obs, velho, novo) -> {
            int mult = novo.intValue();

            textMultiplicador.setText(mult + "x");
            textMultiplicador.setVisible(mult > 1);

            mudarCoresPeloMultiplicador(mult);

            // Se subiu de nível (combo milestone), solta a animação
            if (mult > velho.intValue()) {
                animarTexto(textMultiplicador, 1.5);

                // CORREÇÃO: Chama a animação Osu! (Mascote) em vez do Pulso simples
                animarMascote(imageViewEfeito);
            }
        });

        // ADICIONA TUDO DE UMA VEZ
        root.getChildren().addAll(textScore, textCombo, textFeedback, textMultiplicador);
    }

    // Animação estilo Osu! (Pop-in -> Espera -> Fade-out)
    private void animarMascote(ImageView imageView) {
        // Reseta para garantir que a animação comece limpa
        imageView.setOpacity(0.0);
        imageView.setScaleX(0.8);
        imageView.setScaleY(0.8);

        // Entrada
        FadeTransition fadeIn = new FadeTransition(Duration.millis(200), imageView);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);

        ScaleTransition scaleUp = new ScaleTransition(Duration.millis(200), imageView);
        scaleUp.setFromX(0.8);
        scaleUp.setFromY(0.8);
        scaleUp.setToX(1.0);
        scaleUp.setToY(1.0);

        ParallelTransition entrada = new ParallelTransition(fadeIn, scaleUp);

        // Fica na tela
        PauseTransition ficarNaTela = new PauseTransition(Duration.seconds(2.0));

        // Saída
        FadeTransition fadeOut = new FadeTransition(Duration.millis(500), imageView);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);

        SequentialTransition sequencia = new SequentialTransition(entrada, ficarNaTela, fadeOut);
        sequencia.play();
    }

    private void mudarCoresPeloMultiplicador(int mult) {
        Color cor;
        switch (mult) {
            case 2 -> cor = Color.CYAN;
            case 3 -> cor = Color.LIME;
            case 4 -> cor = Color.ORANGE;
            case 5 -> cor = Color.MAGENTA;
            default -> cor = Color.WHITE;
        }
        textMultiplicador.setFill(cor);
        textMultiplicador.setEffect(new DropShadow(20, cor));
        textCombo.setEffect(new DropShadow(10 + (mult * 5), cor));
    }

    public void mostrarFeedback(Julgamento julgamento) {
        textFeedback.setText(julgamento.toString());
        textFeedback.setOpacity(1);
        textFeedback.setScaleX(1);
        textFeedback.setScaleY(1);

        switch (julgamento) {
            case PERFEITO -> textFeedback.setFill(Color.CYAN);
            case OTIMO -> textFeedback.setFill(Color.LIME);
            case RUIM -> textFeedback.setFill(Color.ORANGE);
            case ERRO -> textFeedback.setFill(Color.RED);
        }

        double larguraTexto = textFeedback.getLayoutBounds().getWidth();
        textFeedback.setX((Layout.INICIO_X + (Layout.AREA_JOGO_SIZE / 2)) - (larguraTexto / 2));

        animarTexto(textFeedback, 1.5);
    }

    private void animarTexto(Text node, double escalaMax) {
        ScaleTransition st = new ScaleTransition(Duration.millis(100), node);
        st.setFromX(0.5); st.setFromY(0.5);
        st.setToX(escalaMax); st.setToY(escalaMax);

        FadeTransition ft = new FadeTransition(Duration.millis(300), node);
        ft.setFromValue(1.0); ft.setToValue(0.0);
        ft.setDelay(Duration.millis(100));

        ParallelTransition pt = new ParallelTransition(st, ft);
        pt.play();
    }

    private void configurarImageViewEfeito() {
        try {
            // Certifique-se que a imagem existe nesse caminho!
            Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/polyphia.jpg")));
            imageViewEfeito.setImage(image);

            imageViewEfeito.setFitWidth(300); // Aumentei um pouco para destacar
            imageViewEfeito.setFitHeight(300);
            imageViewEfeito.setPreserveRatio(true);

            imageViewEfeito.setLayoutX(50);
            imageViewEfeito.setLayoutY(400);

            // IMPORTANTE: Começa invisível
            imageViewEfeito.setOpacity(0.0);

            root.getChildren().add(imageViewEfeito);
        } catch (Exception e) {
            System.err.println("Erro ao carregar a imagem de efeito.");
        }
    }

    public void iniciarCena(Stage stage) {
        Scene cena = new Scene(root, 1920, 1080);
        cena.setFill(Color.web("010101"));
        stage.setScene(cena);
        stage.show();
    }

    public Group getRoot() { return root; }

    public void atualizar(JogoLogica logica, double tempoMusicaMs, double deltaTime, double fpsAtual) {
        textFPS.setText("FPS: " + (int) fpsAtual);
        if (fpsAtual < 59) textFPS.setFill(Color.RED);
        else textFPS.setFill(Color.LIME);

        // --- POOLING SYSTEM ---
        for (Nota nota : logica.getNotasAtivas()) {
            INotaSprite sprite = spritesMap.get(nota);

            if (sprite == null) {
                sprite = nota.obterSprite(this);
                spritesMap.put(nota, sprite);

                // Adiciona ao grupo de notas, não ao root direto
                if (!grupoNotas.getChildren().contains(sprite.getNode())) {
                    grupoNotas.getChildren().add(sprite.getNode());
                }
            }
            sprite.atualizar(tempoMusicaMs);
        }

        spritesMap.entrySet().removeIf(entry -> {
            INotaSprite sprite = entry.getValue();
            if (!sprite.getNota().isAtiva()) {
                sprite.devolverPara(this); // Recicla
                return true;
            }
            return false;
        });
    }

    private void criarHitDots() {
        Color[] coresLanes = { Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.BLUE };
        double posY = Layout.HIT_LINE;
        double spacing = 150;

        for (int i = 0; i < 5; i++) {
            double x = Layout.INICIO_X + i * spacing;
            HitDot dot = new HitDot(i + 1, x, posY, coresLanes[i]);
            hitDots.add(dot);
            root.getChildren().add(dot.getCircle());
        }
    }

    public List<HitDot> getHitDots() { return hitDots; }

    // --- MÉTODOS DO POOL (Double Dispatch) ---
    public NotaTapSprite obterTapDaPool(NotaTap nota) {
        if (!tapPool.isEmpty()) {
            NotaTapSprite s = tapPool.poll();
            s.reusar(nota);
            s.getNode().setVisible(true);
            return s;
        }
        return new NotaTapSprite(nota);
    }

    public void reciclarTap(NotaTapSprite sprite) {
        sprite.getNode().setVisible(false);
        tapPool.add(sprite);
    }

    public NotaHoldSprite obterHoldDaPool(NotaHold nota) {
        if (!holdPool.isEmpty()) {
            NotaHoldSprite s = holdPool.poll();
            s.reusar(nota);
            s.getNode().setVisible(true);
            return s;
        }
        return new NotaHoldSprite(nota);
    }

    public void reciclarHold(NotaHoldSprite sprite) {
        sprite.getNode().setVisible(false);
        holdPool.add(sprite);
    }
}