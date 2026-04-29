package com.badlogic.drop;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

public class GameOverScreen implements Screen {
    final Drop game;
    final int finalScore;

    public GameOverScreen(final Drop game, int finalScore) {
        this.game = game;
        this.finalScore = finalScore;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Apply viewport and set batch projection
        game.viewport.apply();
        game.batch.setProjectionMatrix(game.viewport.getCamera().combined);

        // Compute center positions from the game's viewport so text is placed properly
        float centerX = game.viewport.getWorldWidth() / 2f;
        float centerY = game.viewport.getWorldHeight() / 2f;

        game.batch.begin();
        // Font scale for Game Over screen
        game.font.getData().setScale(1.8f);
        game.font.draw(game.batch, "Game Over", centerX - 150f, centerY + 70f);
        game.font.draw(game.batch, "Score: " + finalScore, centerX - 135f, centerY - 10f);
        game.font.getData().setScale(1.3f);
        game.font.draw(game.batch, "Tap to retry", centerX - 150f, centerY - 150f);
        game.font.getData().setScale(1.0f); // Reset scale
        game.batch.end();

        if (Gdx.input.justTouched()) {
            game.setScreen(new MainMenuScreen(game));
            dispose();
        }
    }

    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {}
}
