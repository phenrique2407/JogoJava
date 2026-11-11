package com.exemplo.cobra;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameScreen extends ScreenAdapter {
    private static final float FUGITIVE_TIME = 4f;
    private static final int FOG_RADIUS = 4;
    private static final int FOOD_GROWTH = 3;

    private final SnakeGame game;
    private final Level level;
    private final Constraint constraint;
    private final Snake snake;
    private final Array<GridPoint2> obstacles = new Array<>();
    private final GridPoint2 food = new GridPoint2();

    private float stepTimer, shrinkTimer, foodTimer;
    private float introTimer = 3f;
    private int score, foodsEaten;
    private boolean paused, over;

    public GameScreen(SnakeGame game, Level level) {
        this.game = game;
        this.level = level;
        this.constraint = Constraint.sortear();
        this.snake = new Snake(SnakeGame.COLS / 2, SnakeGame.ROWS / 2, 5);
        createObstacles();
        placeFood();
    }

    // ---------- setup ----------

    private void createObstacles() {
        int cx = SnakeGame.COLS / 2, cy = SnakeGame.ROWS / 2;
        while (obstacles.size < level.obstacles) {
            int x = MathUtils.random(SnakeGame.COLS - 1);
            int y = MathUtils.random(SnakeGame.ROWS - 1);
            boolean nearStart = Math.abs(y - cy) <= 1 && Math.abs(x - cx) <= 9;
            if (!nearStart && !isObstacle(x, y)) obstacles.add(new GridPoint2(x, y));
        }
    }

    private void placeFood() {
        int x, y;
        do {
            x = MathUtils.random(SnakeGame.COLS - 1);
            y = MathUtils.random(SnakeGame.ROWS - 1);
        } while (snake.occupies(x, y) || isObstacle(x, y));
        food.set(x, y);
        foodTimer = 0;
    }

    private boolean isObstacle(int x, int y) {
        for (GridPoint2 o : obstacles) if (o.x == x && o.y == y) return true;
        return false;
    }

    // ---------- loop ----------

    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
            game.setScreen(new MenuScreen(game));
            return;
        }
        if (introTimer > 0) {
            introTimer -= delta;
        } else {
            if (Gdx.input.isKeyJustPressed(Keys.P)) paused = !paused;
            if (!paused) {
                readInput();
                update(delta);
            }
        }
        if (over) return; // a tela já foi trocada
        draw();
    }

    private void readInput() {
        Direction d = null;
        if (key(Keys.UP, Keys.W)) d = Direction.UP;
        else if (key(Keys.DOWN, Keys.S)) d = Direction.DOWN;
        else if (key(Keys.LEFT, Keys.A)) d = Direction.LEFT;
        else if (key(Keys.RIGHT, Keys.D)) d = Direction.RIGHT;
        if (d != null) {
            if (constraint == Constraint.CONTROLES_INVERTIDOS) d = d.opposite();
            snake.setDirection(d);
        }
    }

    private boolean key(int a, int b) {
        return Gdx.input.isKeyJustPressed(a) || Gdx.input.isKeyJustPressed(b);
    }

    private void update(float delta) {
        // movimento
        stepTimer += delta;
        while (stepTimer >= level.stepTime && !over) {
            stepTimer -= level.stepTime;
            step();
        }
        if (over) return;

        // fome: a cobra encolhe
        shrinkTimer += delta;
        if (shrinkTimer >= level.shrinkTime) {
            shrinkTimer = 0;
            snake.shrink();
            if (snake.length() <= 0) {
                die("Morreu de fome!");
                return;
            }
        }

        // regra: comida fugitiva
        if (constraint == Constraint.COMIDA_FUGITIVA) {
            foodTimer += delta;
            if (foodTimer >= FUGITIVE_TIME) placeFood();
        }
    }

    private void step() {
        GridPoint2 n = snake.nextHead(constraint == Constraint.SEM_PAREDES);
        if (n.x < 0 || n.x >= SnakeGame.COLS || n.y < 0 || n.y >= SnakeGame.ROWS) {
            die("Bateu na parede!");
            return;
        }
        if (snake.hitsSelf(n)) {
            die("Mordeu o próprio corpo!");
            return;
        }
        if (isObstacle(n.x, n.y)) {
            die("Bateu num obstáculo!");
            return;
        }
        boolean eat = n.equals(food);
        snake.advance(n);
        if (eat) {
            snake.grow(FOOD_GROWTH);
            shrinkTimer = 0; // comer mata a fome
            foodsEaten++;
            score += 10 * level.multiplier;
            placeFood();
        }
    }

    private void die(String cause) {
        over = true;
        game.setScreen(new GameOverScreen(game, level, constraint, score, foodsEaten, cause));
    }

    // ---------- desenho ----------

    private void draw() {
        ScreenUtils.clear(0.02f, 0.04f, 0.02f, 1);
        game.beginFrame();
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        int c = SnakeGame.CELL;
        game.shapes.begin(ShapeType.Filled);

        // campo
        game.shapes.setColor(0.08f, 0.12f, 0.08f, 1);
        game.shapes.rect(0, 0, SnakeGame.COLS * c, SnakeGame.ROWS * c);

        // obstáculos
        game.shapes.setColor(0.5f, 0.5f, 0.55f, 1);
        for (GridPoint2 o : obstacles) game.shapes.rect(o.x * c + 1, o.y * c + 1, c - 2, c - 2);

        // comida
        game.shapes.setColor(Color.RED);
        game.shapes.circle(food.x * c + c / 2f, food.y * c + c / 2f, c / 2f - 3);

        // cobra
        Array<GridPoint2> body = snake.getBody();
        for (int i = 0; i < body.size; i++) {
            GridPoint2 s = body.get(i);
            if (i == 0) game.shapes.setColor(0.6f, 1f, 0.4f, 1);
            else game.shapes.setColor(0.1f, 0.65f - 0.3f * i / Math.max(1, body.size), 0.15f, 1);
            game.shapes.rect(s.x * c + 1, s.y * c + 1, c - 2, c - 2);
        }

        // regra: neblina
        if (constraint == Constraint.NEBLINA) {
            GridPoint2 h = snake.head();
            game.shapes.setColor(0, 0, 0, 0.93f);
            for (int x = 0; x < SnakeGame.COLS; x++)
                for (int y = 0; y < SnakeGame.ROWS; y++) {
                    int dx = x - h.x, dy = y - h.y;
                    if (dx * dx + dy * dy > FOG_RADIUS * FOG_RADIUS) game.shapes.rect(x * c, y * c, c, c);
                }
        }

        // HUD
        float hudY = SnakeGame.ROWS * c;
        game.shapes.setColor(0.1f, 0.1f, 0.12f, 1);
        game.shapes.rect(0, hudY, SnakeGame.WIDTH, SnakeGame.HUD);
        float hunger = 1f - shrinkTimer / level.shrinkTime;
        game.shapes.setColor(0.25f, 0.25f, 0.25f, 1);
        game.shapes.rect(470, hudY + 12, 230, 16);
        if (hunger > 0.35f) game.shapes.setColor(Color.GREEN);
        else game.shapes.setColor(Color.RED);
        game.shapes.rect(470, hudY + 12, 230 * hunger, 16);

        // faixa de intro/pausa
        if (introTimer > 0 || paused) {
            game.shapes.setColor(0, 0, 0, 0.75f);
            game.shapes.rect(0, 0, SnakeGame.WIDTH, SnakeGame.ROWS * c);
        }
        game.shapes.end();

        game.batch.begin();
        float hy = hudY + SnakeGame.HUD;
        game.text("Pontos: " + score + "   Tamanho: " + snake.length(), 12, hy - 5, 1.2f, Color.WHITE);
        game.text("Regra: " + constraint.nome + " | " + level.nome, 12, hy - 22, 1.0f, Color.LIGHT_GRAY);
        game.text("FOME", 420, hy - 9, 1.1f, Color.ORANGE);

        if (introTimer > 0) {
            game.textCentered("REGRA SORTEADA", 380, 1.5f, Color.ORANGE);
            game.textCentered(constraint.nome.toUpperCase(), 330, 2.6f, Color.YELLOW);
            game.textCentered(constraint.descricao, 280, 1.2f, Color.WHITE);
            game.textCentered("Comece em " + (int) Math.ceil(introTimer) + "...", 200, 1.5f, Color.GREEN);
        } else if (paused) {
            game.textCentered("PAUSADO (P para continuar)", 260, 2f, Color.WHITE);
        }
        game.batch.end();
    }
}
