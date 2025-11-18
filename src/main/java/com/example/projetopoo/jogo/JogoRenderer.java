package com.example.projetopoo.jogo;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
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

    public JogoRenderer(JogoEstado estado) {
        root.resize(1920, 1080);

        Rectangle hitLine = new Rectangle(Layout.INICIO_X - 20, Layout.HIT_LINE, Layout.AREA_JOGO_SIZE, 20);
        hitLine.setFill(Color.GOLDENROD);

        root.getChildren().add(hitLine);
        root.getChildren().add(grupoNotas);
        configurarFPS();
        criarHitDots();

        configurarInterface(estado);
    }

    private void configurarFPS() {
        textFPS.setFont(Font.font("Consolas", FontWeight.BOLD, 20));
        textFPS.setFill(Color.LIME);
        textFPS.setX(10);
        textFPS.setY(30);
        root.getChildren().add(textFPS);
    }

    private void configurarInterface(JogoEstado estado) {
        // 1. Placar (Canto Superior Direito ou Esquerdo)
        textScore.setFont(Font.font("Consolas", FontWeight.BOLD, 40));
        textScore.setFill(Color.WHITE);
        textScore.setX(1600);
        textScore.setY(100);
        // Liga o texto à variável do estado automaticamente!
        textScore.textProperty().bind(estado.scoreProperty().asString("%06d"));

        // 2. Combo (Centro/Baixo)
        textCombo.setFont(Font.font("Verdana", FontWeight.BOLD, 60));
        textCombo.setFill(Color.WHITE);
        textCombo.setX(200); // Posição provisória
        textCombo.setY(500);
        textCombo.setVisible(false); // Só mostra se combo > 0

        // Listener: Se combo mudar, atualiza
        estado.comboProperty().addListener((obs, velho, novo) -> {
            int val = novo.intValue();
            textCombo.setText(val + "x");
            textCombo.setVisible(val > 1); // Esconde se for 0 ou 1

            // Efeito de "pulo" no combo
            if (val > 1) animarTexto(textCombo, 1.05);
        });

        // Feedback (PERFECT, MISS)
        textFeedback.setFont(Font.font("Impact", 80));
        textFeedback.setStroke(Color.BLACK);
        textFeedback.setStrokeWidth(2);
        textFeedback.setX(Layout.INICIO_X + 200); // Centralizado nas lanes
        textFeedback.setY(600);
        textFeedback.setOpacity(0); // Começa invisível

        // Adiciona tudo na tela (em cima das notas)
        root.getChildren().addAll(textScore, textCombo, textFeedback);

        // --- CONFIGURAÇÃO DO MULTIPLICADOR ---
        textMultiplicador.setFont(Font.font("Impact", 50));
        textMultiplicador.setFill(Color.WHITE);
        textMultiplicador.setX(1600);
        textMultiplicador.setY(160); // Embaixo do Score
        textMultiplicador.setVisible(false); // Esconde se for 1x

        // Listener Poderoso: Reage a mudanças de Nível (1x -> 2x -> 3x...)
        estado.multiplicadorProperty().addListener((obs, velho, novo) -> {
            int mult = novo.intValue();

            // 1. Atualiza o Texto
            textMultiplicador.setText(mult + "x");
            textMultiplicador.setVisible(mult > 1);

            // 2. Efeitos Visuais Baseados no Nível (Isso é a parte "abrangente")
            mudarCoresPeloMultiplicador(mult);

            // 3. Animação de "Level Up"
            if (mult > velho.intValue()) {
                animarTexto(textMultiplicador, 1.5); // Pop grande!
            }
        });

        root.getChildren().add(textMultiplicador);
        // ... addAll(textScore, textCombo, textFeedback) ...
    }

    private void mudarCoresPeloMultiplicador(int mult) {
        Color cor;
        switch (mult) {
            case 2 -> cor = Color.CYAN;
            case 3 -> cor = Color.LIME;
            case 4 -> cor = Color.ORANGE;
            case 5 -> cor = Color.MAGENTA; // Modo Frenético!
            default -> cor = Color.WHITE;
        }

        // Aplica a cor no texto do multiplicador
        textMultiplicador.setFill(cor);
        textMultiplicador.setEffect(new DropShadow(20, cor));

        // EXTRAS: Você pode mudar a cor do Combo também!
        textCombo.setEffect(new DropShadow(10 + (mult * 5), cor)); // Sombra aumenta com o mult

        // Se quiser mudar a HitLine (aquela linha dourada)
        // hitLine.setFill(cor);
    }

    // Metodo chamado quando acerta uma nota
    public void mostrarFeedback(Julgamento julgamento) {
        textFeedback.setText(julgamento.toString()); // "PERFEITO", "OTIMO"...
        textFeedback.setOpacity(1);
        textFeedback.setScaleX(1);
        textFeedback.setScaleY(1);

        // Cores diferentes
        switch (julgamento) {
            case PERFEITO -> textFeedback.setFill(Color.CYAN);
            case OTIMO -> textFeedback.setFill(Color.LIME);
            case RUIM -> textFeedback.setFill(Color.ORANGE);
            case ERRO -> textFeedback.setFill(Color.RED);
        }

        // Centraliza o texto baseado no tamanho da palavra
        double larguraTexto = textFeedback.getLayoutBounds().getWidth();
        textFeedback.setX((Layout.INICIO_X + (Layout.AREA_JOGO_SIZE / 2)) - (larguraTexto / 2));

        animarTexto(textFeedback, 1.5);
    }

    private void animarTexto(Text node, double escalaMax) {
        // Animação de "Pop" (Cresce e some)
        ScaleTransition st = new ScaleTransition(Duration.millis(100), node);
        st.setFromX(0.5);
        st.setFromY(0.5);
        st.setToX(escalaMax);
        st.setToY(escalaMax);

        FadeTransition ft = new FadeTransition(Duration.millis(300), node);
        ft.setFromValue(1.0);
        ft.setToValue(0.0);
        ft.setDelay(Duration.millis(100)); // Espera um pouco antes de sumir

        ParallelTransition pt = new ParallelTransition(st, ft);
        pt.play();
    }

    public void iniciarCena(Stage stage) {
        Scene cena = new Scene(root, 1920, 1080);
        cena.setFill(Color.web("010101"));
        stage.setScene(cena);
        stage.show();
    }

    public Group getRoot() {
        return root;
    }

    public void atualizar(JogoLogica logica, double tempoMusicaMs, double deltaTime, double fpsAtual) {
        textFPS.setText("FPS: " + (int) fpsAtual);
        if (fpsAtual < 59) textFPS.setFill(Color.RED);
        else textFPS.setFill(Color.LIME);

        for (Nota nota : logica.getNotasAtivas()) {

            INotaSprite sprite = spritesMap.get(nota);

            if (sprite == null) {
                sprite = nota.obterSprite(this);

                spritesMap.put(nota, sprite);

                if (!grupoNotas.getChildren().contains(sprite.getNode())) {
                    grupoNotas.getChildren().add(sprite.getNode());
                }
            }

            sprite.atualizar(tempoMusicaMs);
        }

        spritesMap.entrySet().removeIf(entry -> {
            INotaSprite sprite = entry.getValue();
            if (!sprite.getNota().isAtiva()) {
                sprite.devolverPara(this);

                return true; // Remove do mapa
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

    public List<HitDot> getHitDots() {
        return hitDots;
    }

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