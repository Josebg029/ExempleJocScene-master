package com.badlogic.drop;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.StretchViewport;


public class Drop extends Game {

    public SpriteBatch batch;
    public BitmapFont font;
    public StretchViewport viewport;


    public void create() {
        batch = new SpriteBatch();
        // Use StretchViewport with 800x480 world coords but stretch to fill screen
        viewport = new StretchViewport(800, 480);

        // Try to load a TTF font from assets/fonts/arcade.ttf using FreeType; fall back to default font.
        if (Gdx.files.internal("fonts/arcade.ttf").exists()) {
            FreeTypeFontGenerator gen = new FreeTypeFontGenerator(Gdx.files.internal("fonts/arcade.ttf"));
            FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();
            // Choose a reasonable size; scale will be applied below to match viewport
            param.size = 20;
            font = gen.generateFont(param);
            gen.dispose();
            font.setUseIntegerPositions(false);
            // Scale font so it maps correctly into the 800x480 world coordinates
            font.getData().setScale(viewport.getWorldHeight() / Gdx.graphics.getHeight());
        } else {
            // use libGDX's default font
            font = new BitmapFont();
            font.setUseIntegerPositions(false);
            font.getData().setScale(viewport.getWorldHeight() / Gdx.graphics.getHeight());
        }

        this.setScreen(new SplashScreen(this));
    }

    public void render() {
        super.render(); // important!
    }

    public void dispose() {
        batch.dispose();
        font.dispose();
    }

}
