package com.badlogic.drop;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

/**
 * El coche del jugador (coche rojo F1), posicionado en la parte inferior central de la pista.
 */
public class Bucket extends Image {

    // Tamaño normalizado para todos los coches (ancho x alto en coords mundo)
    public static final float CAR_W = 70f;
    public static final float CAR_H = 110f;

    // Límites horizontales de la pista (excluye franjas laterales ~80px)
    public static final float TRACK_LEFT  = 80f;
    public static final float TRACK_RIGHT = 720f;

    private static TextureRegion flipped(Texture t) {
        TextureRegion r = new TextureRegion(t);
        r.flip(false, true);
        return r;
    }

    public Bucket(Texture texture) {
        super(flipped(texture));
        // Centrado horizontalmente en la pista, parte inferior
        float startX = (TRACK_LEFT + TRACK_RIGHT) / 2f - CAR_W / 2f;
        setPosition(startX, 20);
        setSize(CAR_W, CAR_H);
    }
}
