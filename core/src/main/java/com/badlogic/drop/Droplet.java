package com.badlogic.drop;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;


/**
 * Una gota, un actor de tipus Image
 */
public class Droplet extends Image {

    /**
     * El constructor rep per paràmetre la textura de la gota, que es passa al constructor de
     * la sueprclasse Image. Estableix la posició inicial de la gota a la part superior de la pantalla
     * en una posició horitzontal aleatòria.
     * @param texture textura amb la imatge de la gota carregada
     */
    public Droplet(Texture texture) {
        super(texture);
        setPosition(MathUtils.random(0, 800-64), 480-64);
        setSize(64, 64);
    }


    /**
     * Si la seva posició arriba a la part inferior de la pantalla, s'elimina
     * @param delta
     */
    @Override
    public void act(float delta) {
        super.act(delta);
        if ( getY() == 0 )
            getParent().removeActor(this);
    }

    /**
     * Comprova si la gota ha entrat a la galleda
     * @param bucket la galleda
     * @return cert si ha entrat, fals en cas contrari
     */
    public boolean inBucket(Bucket bucket) {
        // Calculem els rectangles de col·lisió dels dos objectes
        Rectangle rectangleDroplet = new Rectangle(getX(), getY(), getWidth(), getHeight());
        Rectangle rectangleBucket = new Rectangle(bucket.getX(), bucket.getY(), bucket.getWidth(), bucket.getHeight());
        //El mètode overlaps indica si hi ha solapament entre els dos rectangles calculats
        return rectangleDroplet.overlaps(rectangleBucket);
    }
}
