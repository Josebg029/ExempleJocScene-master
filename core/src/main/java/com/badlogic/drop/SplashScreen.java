package com.badlogic.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

public class SplashScreen implements Screen {
    final Drop game;
    Texture splash;
    float time;

    public SplashScreen(final Drop game) {
        this.game = game;
        splash = new Texture(Gdx.files.internal("Splash1.png"));
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        time += delta;

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.viewport.apply();
        game.batch.setProjectionMatrix(game.viewport.getCamera().combined);
        game.batch.begin();

        // Fondo pantalla completa (coordenadas mundo 800x480)
        game.batch.draw(splash, 0, 0, 800, 480);

        // Texto "Loading..."
        game.font.getData().setScale(1.5f);
        GlyphLayout layout = new GlyphLayout(game.font, "Loading...");
        float textX = (800f - layout.width) / 2f;
        game.font.draw(game.batch, "Loading...", textX, 60f);

        game.batch.end();

        if (time > 3f) {
            game.setScreen(new MainMenuScreen(game));
            dispose();
        }
    }

    @Override public void resize(int w, int h) { game.viewport.update(w, h, true); }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        if (splash != null) splash.dispose();
    }
}
