package com.exemplo.cobra;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class SnakeGame extends Game {
    public static final int COLS = 30, ROWS = 20, CELL = 24, HUD = 40;
    public static final int WIDTH = COLS * CELL, HEIGHT = ROWS * CELL + HUD;

    public SpriteBatch batch;
    public ShapeRenderer shapes;
    public BitmapFont font;
    public FitViewport viewport;
    private final GlyphLayout layout = new GlyphLayout();

    @Override
    public void create() {
        batch = new SpriteBatch();
        shapes = new ShapeRenderer();
        font = new BitmapFont();
        viewport = new FitViewport(WIDTH, HEIGHT);
        setScreen(new MenuScreen(this));
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        viewport.update(width, height, true);
    }

    /** Aplica a viewport e a projeção. Chamar no início de todo render. */
    public void beginFrame() {
        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        shapes.setProjectionMatrix(viewport.getCamera().combined);
    }

    /** Desenha texto (o batch deve estar iniciado). */
    public void text(String s, float x, float y, float scale, Color c) {
        font.getData().setScale(scale);
        font.setColor(c);
        font.draw(batch, s, x, y);
        font.setColor(Color.WHITE);
        font.getData().setScale(1f);
    }

    public void textCentered(String s, float y, float scale, Color c) {
        font.getData().setScale(scale);
        layout.setText(font, s);
        float w = layout.width;
        font.getData().setScale(1f);
        text(s, (WIDTH - w) / 2f, y, scale, c);
    }

    @Override
    public void dispose() {
        batch.dispose();
        shapes.dispose();
        font.dispose();
    }
}
