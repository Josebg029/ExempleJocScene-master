package com.badlogic.drop;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector3;

/**
 * Ës la classe que processa l'entrada
 */
public class InputHandler implements InputProcessor {
    private GameScreen gameScreen;


    public InputHandler(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    /**
     * Aquest mètode es crida en tocar la pantalla
     * @param screenX posició horitzontal, en píxels
     * @param screenY posició vertical, en píxels
     * @param pointer
     * @param button
     * @return
     */
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        // Obtenim la posició en la càmera 800x480 a partir de la posició tocada de la pantalla
       /* Vector3 vector = gameScreen.stage.getCamera().unproject(new Vector3(screenX, screenY, 0));
        float x = vector.x - 64/2;
        // Rectifiquem en cas que la galleda surti per la dreta o esquerra de la pantalla
        if ( x < 0 )
            x = 0;
        if (x > 800 - 64)
            x = 800-64;
        gameScreen.bucket.setX(x);*/
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer)
    {
        float worldWidth = gameScreen.stage.getWidth();
        float worldHeight = gameScreen.stage.getHeight();

        // Obtenim la posició en la càmera 800x480 a partir de la posició tocada de la pantalla
        Vector3 vector = gameScreen.stage.getCamera().unproject(new Vector3(screenX, screenY, 0));
        float x = vector.x - 100/2;
        // Rectifiquem en cas que la galleda surti per la dreta o esquerra de la pantalla
        if ( x < 0 )
            x = 0;
        if (x > worldWidth - 100)
            x = worldWidth-100;
        gameScreen.bucket.setX(x);
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
