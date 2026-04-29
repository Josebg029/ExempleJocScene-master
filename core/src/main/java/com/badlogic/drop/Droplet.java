package com.badlogic.drop;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

/**
 * Un coche enemigo que baja por la pista.
 */
public class Droplet extends Image {

    private static TextureRegion flipped(Texture t) {
        TextureRegion r = new TextureRegion(t);
        r.flip(false, true);
        return r;
    }

    public Droplet(Texture texture) {
        super(flipped(texture));
        // Spawn en posición horizontal aleatoria dentro de los límites de la pista
        float x = MathUtils.random(Bucket.TRACK_LEFT, Bucket.TRACK_RIGHT - Bucket.CAR_W);
        setPosition(x, 480);
        setSize(Bucket.CAR_W, Bucket.CAR_H);
    }

    /**
     * Comprueba si el coche enemigo colisiona con el coche del jugador (con margen reducido).
     */
    public boolean inBucket(Bucket bucket) {
        // Usamos hitbox reducida (20% margen) para colisiones más justas
        float margin = Bucket.CAR_W * 0.2f;
        Rectangle rectDroplet = new Rectangle(
            getX() + margin, getY() + margin,
            getWidth() - margin * 2, getHeight() - margin * 2
        );
        Rectangle rectBucket = new Rectangle(
            bucket.getX() + margin, bucket.getY() + margin,
            bucket.getWidth() - margin * 2, bucket.getHeight() - margin * 2
        );
        return rectDroplet.overlaps(rectBucket);
    }
}
