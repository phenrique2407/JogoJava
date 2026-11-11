package com.exemplo.cobra;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ScreenUtils;

public class MenuScreen extends ScreenAdapter {
    private final SnakeGame game;
    private int selected = 0;

    public MenuScreen(SnakeGame game) { this.game = game; }

    @Override
    public void render(float delta) {
        Level[] levels = Level.values();
        if (Gdx.input.isKeyJustPressed(Keys.UP) || Gdx.input.isKeyJustPressed(Keys.W))
            selected = (selected + levels.length - 1) % levels.length;
        if (Gdx.input.isKeyJustPressed(Keys.DOWN) || Gdx.input.isKeyJustPressed(Keys.S))
            selected = (selected + 1) % levels.length;
        if (Gdx.input.isKeyJustPressed(Keys.ENTER) || Gdx.input.isKeyJustPressed(Keys.SPACE)) {
            game.setScreen(new GameScreen(game, levels[selected]));
            return;
        }
        if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) Gdx.app.exit();

        ScreenUtils.clear(0.05f, 0.08f, 0.05f, 1);
        game.beginFrame();
        game.batch.begin();
        game.textCentered("COBRA FAMINTA", 440, 3f, Color.GREEN);
        game.textCentered("Sua cobra encolhe com o tempo: coma para não sumir!", 370, 1.2f, Color.WHITE);
        game.textCentered("Uma regra aleatória é sorteada a cada partida.", 345, 1.1f, Color.LIGHT_GRAY);

        for (int i = 0; i < levels.length; i++) {
            Level l = levels[i];
            Color c = i == selected ? Color.YELLOW : Color.GRAY;
            String prefix = i == selected ? "> " : "  ";
            int rec = Gdx.app.getPreferences("cobra-faminta").getInteger("high_" + l.name(), 0);
            game.textCentered(prefix + l.nome + "   (recorde: " + rec + ")", 270 - i * 40, 1.6f, c);
        }
        game.textCentered("Setas/WASD: escolher e mover   ENTER: jogar   P: pausa   ESC: sair", 60, 1.0f, Color.LIGHT_GRAY);
        game.batch.end();
    }
}
