# 🐍 Cobra Faminta (libGDX)

## Mecânica central

Você controla uma cobra que encolhe sozinha com o tempo — o tamanho dela é a sua vida.
Comer reconstrói o corpo e zera a fome, então você precisa arriscar para sobreviver.
A graça está em equilibrar risco e controle: quanto maior a cobra, mais difícil fica se mover, e quanto menor, mais próxima da morte ela fica.

## Restrição sorteada por partida

Cada partida começa com uma regra aleatória que altera a experiência de jogo e exige adaptação:

- Sem paredes: as bordas do mapa se conectam;
- Controles invertidos: os movimentos ficam opostos ao normal;
- Neblina: o campo de visão fica reduzido;
- Comida fugitiva: a comida muda de lugar a cada 4 segundos.

## Recursos do jogo

- Menu inicial com seleção de dificuldade;
- 3 níveis de desafio: Fácil, Médio (6 obstáculos) e Difícil (12 obstáculos);
- Tela de Game Over com pontuação final e causa da derrota;
- Sistema de recordes por nível salvo em Preferences;
- Jogo sem assets externos: todo o visual é desenhado com ShapeRenderer e uma fonte padrão.

## Controles

- Setas / WASD: movimentar a cobra;
- P: pausar;
- ESC: voltar ao menu;
- ENTER: confirmar ações do menu.

## Estrutura do código

O projeto está organizado em classes com responsabilidades bem definidas:

- `SnakeGame`: classe principal e recursos compartilhados da aplicação;
- `MenuScreen`: tela inicial e seleção de dificuldade;
- `GameScreen`: lógica principal da partida, renderização e atualização do jogo;
- `GameOverScreen`: tela final, pontuação e informações da derrota;
- `Snake`: jogador principal, movimento, crescimento e emagrecimento;
- `Direction`: enumeração das direções de movimento;
- `Level`: configuração e dificuldade do estágio;
- `Constraint`: restrições aleatórias que afetam a jogabilidade.

Esses blocos deixam o código modular, facilitando manutenção, evolução das regras e criação de novas mecânicas.

## Como rodar

1. Clone o repositório para sua máquina.
2. Certifique-se de ter o JDK instalado no computador.
3. Abra o terminal na pasta raiz do projeto.
4. Execute o jogo com o Gradle:

   Windows:
   `./gradlew.bat lwjgl3:run`

   Linux/macOS:
   `./gradlew lwjgl3:run`

5. Se preferir, você também pode executar a classe principal no IntelliJ IDEA ou VS Code:
   `com.exemplo.cobra.lwjgl3.Lwjgl3Launcher`

Observação: esta seção é para rodar o projeto já criado. Se você estiver montando o projeto do zero com o libGDX, é necessário criar o template inicial e então importar os arquivos do jogo.
