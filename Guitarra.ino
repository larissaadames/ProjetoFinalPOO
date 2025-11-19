// Define o pino do botão
const int selectCima = 10; // full verde
const int selectBaixo = 11; // full azul
const int pedalCima = 2; // verde e branco com isolante  
const int pedalBaixo = 3; //verde e branco
const int botaoRedondo = 4; // branco e azul com isolante
const int b1 = 5; // laranja e branco // baixo pra cima
const int b2 = 6; // azul e branco
const int b3 = 7; // verde com isolante
const int b5 = 8; // full azul com fita crepe
const int b4 = 9; // full laranja

// --- 2. VARIÁVEIS DE ESTADO ---
// Cada botão DEVE ter sua própria variável de estado anterior.
int estadoBotao1Anterior = HIGH;
int estadoBotao2Anterior = HIGH;
int estadoBotao3Anterior = HIGH;
int estadoBotao4Anterior = HIGH;
int estadoBotao5Anterior = HIGH;


void setup() {
  Serial.begin(115200);

  // --- 3. CONFIGURAÇÃO DOS PINOS ---
  // Configura todos os 5 pinos como entrada com resistor PULLUP
  pinMode(b1, INPUT_PULLUP);
  pinMode(b2, INPUT_PULLUP);
  pinMode(b3, INPUT_PULLUP);
  pinMode(b4, INPUT_PULLUP);
  pinMode(b5, INPUT_PULLUP);
}


void loop() {
  // --- 4. CHAMADA DAS FUNÇÕES ---
  // O loop principal agora apenas chama as funções de checagem
  checarBotao1();
  checarBotao2();
  checarBotao3();
  checarBotao4();
  checarBotao5();
}


// --- 5. BLOCO DE FUNÇÕES DE CHECAGEM ---

/**
 * Checa o Botão 1
 */
void checarBotao1() {
  int estadoAtual = digitalRead(b1);
  if (estadoAtual != estadoBotao1Anterior) {
    if (estadoAtual == LOW) { // LOW = Pressionado (devido ao PULLUP)
      Serial.println("L");
    } else {
      Serial.println("");
    }
    estadoBotao1Anterior = estadoAtual; // Atualiza o estado APENAS deste botão
  }
}

/**
 * Checa o Botão 2
 */
void checarBotao2() {
  int estadoAtual = digitalRead(b2);
  if (estadoAtual != estadoBotao2Anterior) {
    if (estadoAtual == LOW) {
      Serial.println("K");
    } else {
      Serial.println("B2_SOLTO");
    }
    estadoBotao2Anterior = estadoAtual;
  }
}

/**
 * Checa o Botão 3
 */
void checarBotao3() {
  int estadoAtual = digitalRead(b3);
  if (estadoAtual != estadoBotao3Anterior) {
    if (estadoAtual == LOW) {
      Serial.println("J");
    } else {
      Serial.println("B3_SOLTO");
    }
    estadoBotao3Anterior = estadoAtual;
  }
}

/**
 * Checa o Botão 4
 */
void checarBotao4() {
  int estadoAtual = digitalRead(b4);
  if (estadoAtual != estadoBotao4Anterior) {
    if (estadoAtual == LOW) {
      Serial.println("F");
    } else {
      Serial.println("B4_SOLTO");
    }
    estadoBotao4Anterior = estadoAtual;
  }
}

/**
 * Checa o Botão 5
 */
void checarBotao5() {
  int estadoAtual = digitalRead(b5);
  if (estadoAtual != estadoBotao5Anterior) {
    if (estadoAtual == LOW) {
      Serial.println("D");
    } else {
      Serial.println("B5_SOLTO");
    }
    estadoBotao5Anterior = estadoAtual;
  }
}
