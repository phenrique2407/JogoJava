package com.exemplo.cobra;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameOverScreen extends ScreenAdapter {
    private final SnakeGame game;
    private final Level level;
    private final Constraint constraint;
    private final int score, foods;
    private final String cause;
    private final int highScore;
    private final boolean newRecord;

    public GameOverScreen(SnakeGame game, Level level, Constraint constraint, int score, int foods, String cause) {
        this.game = game;
        this.level = level;
        this.constraint = constraint;
        this.score = score;
        this.foods = foods;
        this.cause = cause;

        Preferences prefs = Gdx.app.getPreferences("cobra-faminta");
        String key = "high_" + level.name();
        int old = prefs.getInteger(key, 0);
        newRecord = score > old;
        if (newRecord) {
            prefs.putInteger(key, score);
            prefs.flush();
        }
        highScore = Math.max(old, score);
    }

    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyJustPressed(Keys.ENTER) || Gdx.input.isKeyJustPressed(Keys.SPACE)) {
            game.setScreen(new GameScreen(game, level));
            return;
        }
        if (Gdx.input.isKeyJustPressed(Keys.ESCAPE) || Gdx.input.isKeyJustPressed(Keys.M)) {
            game.setScreen(new MenuScreen(game));
            return;
        }

        ScreenUtils.clear(0.12f, 0.03f, 0.03f, 1);
        game.beginFrame();
        game.batch.begin();
        game.textCentered("GAME OVER", 430, 3f, Color.RED);
        game.textCentered(cause, 360, 1.4f, Color.WHITE);
        game.textCentered("Pontos: " + score + "   Comidas: " + foods, 300, 1.6f, Color.YELLOW);
        game.textCentered("Recorde (" + level.nome + "): " + highScore + (newRecord ? "  NOVO RECORDE!" : ""), 260, 1.3f, Color.ORANGE);
        game.textCentered("Regra da partida: " + constraint.nome, 215, 1.2f, Color.LIGHT_GRAY);
        game.textCentered("ENTER: jogar de novo   M/ESC: menu", 110, 1.3f, Color.WHITE);
        game.batch.end();
    }
}
