package com.badlogic.drop;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

/**
 * Gestiona los coches enemigos que bajan por la pista.
 */
public class DropletHandler extends Group {
    private long lastDropletTime;
    private long dropletInterval = 900_000_000L; // 0.9s inicial entre coches

    private float elapsed = 0f; // tiempo total transcurrido

    private final Texture[] enemyTextures; // Texturas de coches enemigos aleatorios
    private final Sound explosionSound;

    public DropletHandler(AssetManager assetManager) {
        enemyTextures = new Texture[] {
            assetManager.get(AssetDescriptors.cocheAmarillo),
            assetManager.get(AssetDescriptors.cocheAzul),
            assetManager.get(AssetDescriptors.cocheBlanco),
            assetManager.get(AssetDescriptors.cocheGris),
            assetManager.get(AssetDescriptors.cocheGris2),
            assetManager.get(AssetDescriptors.cocheNegro)
        };
        explosionSound = assetManager.get(AssetDescriptors.explosionSound);
        lastDropletTime = TimeUtils.nanoTime();
        spawnDroplet();
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        elapsed += delta;
        // Cada 10s el intervalo entre coches se reduce un 8% (mínimo 300ms)
        dropletInterval = Math.max(300_000_000L, (long)(900_000_000L * Math.pow(0.92, elapsed / 10f)));
        spawnDroplet();
    }

    private void spawnDroplet() {
        if (TimeUtils.nanoTime() - lastDropletTime > dropletInterval) {
            lastDropletTime = TimeUtils.nanoTime();
            // Elegir textura de coche enemigo aleatoria
            Texture tex = enemyTextures[MathUtils.random(0, enemyTextures.length - 1)];
            Droplet car = new Droplet(tex);
            // La velocidad de bajada también aumenta con el tiempo (mínimo 0.8s)
            float travelTime = Math.max(0.8f, 2.5f - elapsed * 0.025f);
            car.addAction(Actions.moveTo(car.getX(), -Bucket.CAR_H, travelTime));
            addActor(car);
        }
    }

    public int[] updateAndCheck(Bucket bucket) {
        int collisions = 0;
        int evaded = 0;

        Iterator<Actor> it = getChildren().iterator();
        while (it.hasNext()) {
            Droplet car = (Droplet) it.next();
            if (car.inBucket(bucket)) {
                removeActor(car);
                collisions++;
                explosionSound.play();
                continue;
            }
            if (car.getY() <= -Bucket.CAR_H) {
                removeActor(car);
                evaded++;
            }
        }
        return new int[]{collisions, evaded};
    }
}
