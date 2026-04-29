package com.badlogic.drop;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;

import com.badlogic.gdx.utils.ScreenUtils;

import com.badlogic.gdx.utils.viewport.StretchViewport;


public class GameScreen implements Screen {
    final Drop game;
    final Stage stage;
    final AssetManager assetManager = new AssetManager();

    BitmapFont font;
    Music rainMusic;
    Bucket bucket;
    DropletHandler raindrops;

    // Scrolling background
    float scrollY = 0f;
    static final float SCROLL_SPEED = 1000f;

    // New: lives and score
    int lives = 3;
    int score = 0;

    DPad dpad;

    public GameScreen(final Drop game) {
        this.game = game;
        font = new BitmapFont();

        OrthographicCamera camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        // Create stage with StretchViewport to fill entire screen
        stage = new Stage(new StretchViewport(800, 480, camera)) ;

        // Make Drop.viewport reference the same viewport used by the stage so HUD/font coords match
        game.viewport = (StretchViewport) stage.getViewport();
    }

    @Override
    public void show() {
        // Carreguem els assets
        loadAssets();

        // load the drop sound effect and the rain background "music"

        rainMusic = assetManager.get(AssetDescriptors.rainMusic);
        rainMusic.setLooping(true);


        // Creem el grup d'actors que gestionarà les gotes
        raindrops = new DropletHandler(assetManager);

        // Creem el coche del jugador (rojo)
        bucket = new Bucket(assetManager.get(AssetDescriptors.cocheRojo));

        // Afegim els actors a l'escena
        stage.addActor(raindrops);
        stage.addActor(bucket);

        dpad = new DPad(stage);

        //Assignem com a gestor d'entrada una instància de la classe InputHandler
        Gdx.input.setInputProcessor(new InputHandler(this));

        // start the playback of the background music
        // when the screen is shown
        rainMusic.play();
    }

    @Override
    public void render(float delta) {
        // Esperem a que estiguin carregats els assets
        while ( !assetManager.update() ) {
            assetManager.getProgress();
        }

        rainMusic = assetManager.get(AssetDescriptors.rainMusic);

        ScreenUtils.clear(0, 0, 0, 1);

        // Update raindrops and check collisions/evaded drops
        int[] result = raindrops.updateAndCheck(bucket);
        int collisions = result[0];
        int evaded = result[1];

        // GAMEPLAY MODE: Evade falling droplets or lose lives
        if (collisions > 0) {
            lives -= collisions;
            // Mostrar explosión en el centro del coche del jugador
            float cx = bucket.getX() + bucket.getWidth() / 2f;
            float cy = bucket.getY() + bucket.getHeight() / 2f;
            stage.addActor(new Explosion(cx, cy));
        }
        if (evaded > 0) {
            score += evaded * 10;
        }

        // --- Scrolling track background ---
        Texture carrTex = assetManager.get(AssetDescriptors.carretera);
        Texture izqTex  = assetManager.get(AssetDescriptors.izq);
        Texture derTex  = assetManager.get(AssetDescriptors.der);

        scrollY += delta * SCROLL_SPEED;
        float carrH = carrTex.getHeight();
        if (scrollY >= carrH) scrollY -= carrH;

        float sideW = izqTex.getWidth();
        float sideH = izqTex.getHeight();
        float sideScroll = scrollY % sideH;

        stage.getBatch().begin();
        // Carretera fullscreen, two copies for seamless vertical loop
        stage.getBatch().draw(carrTex, 0,      -scrollY,        800, carrH);
        stage.getBatch().draw(carrTex, 0,  carrH - scrollY,     800, carrH);
        // IZQ left strip, two copies
        stage.getBatch().draw(izqTex,  0,      -sideScroll,     sideW, sideH);
        stage.getBatch().draw(izqTex,  0,  sideH - sideScroll,  sideW, sideH);
        // DER right strip, two copies
        stage.getBatch().draw(derTex,  800 - sideW, -sideScroll,     sideW, sideH);
        stage.getBatch().draw(derTex,  800 - sideW,  sideH - sideScroll, sideW, sideH);
        stage.getBatch().end();

        // Update and draw actors
        stage.act(delta);
        stage.draw();

        // HUD: draw on top of everything. Make sure batch uses stage camera projection
        stage.getBatch().setProjectionMatrix(stage.getViewport().getCamera().combined);
        stage.getBatch().begin();
        // Font scale for better visibility
        game.font.getData().setScale(1.5f);
        game.font.draw(stage.getBatch(), "Lives: " + lives, 20, 430);
        game.font.draw(stage.getBatch(), "Score: " + score, 480, 430);
        game.font.getData().setScale(1.0f); // Reset scale for other uses
        stage.getBatch().end();

        // DPad movement (also arrow keys / WASD for desktop)
        float speed = 300f * delta;
        dpad.update();
        boolean goUp    = dpad.isAccel() || Gdx.input.isKeyPressed(Keys.UP)    || Gdx.input.isKeyPressed(Keys.W);
        boolean goDown  = dpad.isBrake() || Gdx.input.isKeyPressed(Keys.DOWN)  || Gdx.input.isKeyPressed(Keys.S);
        boolean goLeft  = dpad.isLeft()  || Gdx.input.isKeyPressed(Keys.LEFT)  || Gdx.input.isKeyPressed(Keys.A);
        boolean goRight = dpad.isRight() || Gdx.input.isKeyPressed(Keys.RIGHT) || Gdx.input.isKeyPressed(Keys.D);
        if (goLeft)  bucket.setX(Math.max(Bucket.TRACK_LEFT,                bucket.getX() - speed));
        if (goRight) bucket.setX(Math.min(Bucket.TRACK_RIGHT - Bucket.CAR_W, bucket.getX() + speed));
        if (goUp)    bucket.setY(Math.min(480 - Bucket.CAR_H,               bucket.getY() + speed));
        if (goDown)  bucket.setY(Math.max(0,                                 bucket.getY() - speed));

        // Draw controls on top of everything
        dpad.draw();

        if (lives <= 0) {
            rainMusic.stop();
            assetManager.get(AssetDescriptors.fahSound).play();
            // go to game over and show score
            game.setScreen(new GameOverScreen(game, score));
            dispose();
        }
    }

    private void loadAssets() {
        assetManager.load(AssetDescriptors.cocheRojo);
        assetManager.load(AssetDescriptors.cocheAmarillo);
        assetManager.load(AssetDescriptors.cocheAzul);
        assetManager.load(AssetDescriptors.cocheBlanco);
        assetManager.load(AssetDescriptors.cocheGris);
        assetManager.load(AssetDescriptors.cocheGris2);
        assetManager.load(AssetDescriptors.cocheNegro);
        assetManager.load(AssetDescriptors.carretera);
        assetManager.load(AssetDescriptors.izq);
        assetManager.load(AssetDescriptors.der);
        assetManager.load(AssetDescriptors.explosionSound);
        assetManager.load(AssetDescriptors.fahSound);
        assetManager.load(AssetDescriptors.rainMusic);
        assetManager.finishLoading();
    }

    @Override
    public void resize(int width, int height) {
        // Update stage viewport (and also game.viewport which references it)
        stage.getViewport().update(width, height, true);
        game.viewport.update(width, height, true);
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        if (dpad != null) dpad.dispose();
        assetManager.dispose();
    }
}
