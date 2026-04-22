package com.badlogic.drop;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;

import com.badlogic.gdx.utils.ScreenUtils;

import com.badlogic.gdx.utils.viewport.FitViewport;


public class GameScreen implements Screen {
    final Drop game;
    final Stage stage;
    final AssetManager assetManager = new AssetManager();

    BitmapFont font;
    Music rainMusic;
    Bucket bucket;
    Texture fons;
    DropletHandler raindrops;

    int dropsGathered;

    public GameScreen(final Drop game) {
        this.game = game;
        font = new BitmapFont();

        OrthographicCamera camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        // Creem l'escena amb un viewport equivalent a la càmera sencera
        stage = new Stage(new FitViewport(800, 480, camera)) ;
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

        // Creem el cubell
        bucket = new Bucket(assetManager.get(AssetDescriptors.bucketTexture));

        // Afegim els actors a l'escena
        stage.addActor(raindrops);
        stage.addActor(bucket);

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
        fons = assetManager.get(AssetDescriptors.background);

        ScreenUtils.clear(0, 0, 0.2f, 1);

        // Si alguna gota cau en la galleda incrementem el comptador
        if ( raindrops.collectDroplet(bucket) ) {
            dropsGathered++;
        }

        // Mostrem un missatge de text amb les gotes que s'han recollit
        stage.getBatch().begin();

        stage.getBatch().draw(fons ,0,0);

        //System.out.println(game.font.getColor());
        font.draw(stage.getBatch(), "Drops gathered " + dropsGathered, 10,460);
        stage.getBatch().end();

        stage.act(delta);
        // A continuació dibuixem l'escena, el mètode draw cridarà als mètodes draw de tots els actors
        stage.draw();
    }

    private void loadAssets() {
        assetManager.load(AssetDescriptors.bucketTexture);
        assetManager.load(AssetDescriptors.dropletTexture);
        assetManager.load(AssetDescriptors.background);
        assetManager.load(AssetDescriptors.dropSound);
        assetManager.load(AssetDescriptors.rainMusic);
        assetManager.finishLoading();
    }

    @Override
    public void resize(int width, int height) {
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
        assetManager.dispose(); ;
    }
}
