package com.badlogic.drop;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Explosion extends Image {

    private static Texture explosionTexture;

    public Explosion(float centerX, float centerY) {
        super(getTexture());
        float size = 130f;
        setSize(size, size);
        setPosition(centerX - size / 2f, centerY - size / 2f);
        setOrigin(size / 2f, size / 2f);
        setScale(0.2f);

        addAction(Actions.sequence(
            Actions.parallel(
                Actions.scaleTo(1.6f, 1.6f, 0.45f),
                Actions.fadeOut(0.45f)
            ),
            Actions.removeActor()
        ));
    }

    private static Texture getTexture() {
        if (explosionTexture == null) {
            int size = 128;
            Pixmap pixmap = new Pixmap(size, size, Pixmap.Format.RGBA8888);
            float cx = size / 2f;
            float cy = size / 2f;
            float r  = size / 2f;
            for (int px = 0; px < size; px++) {
                for (int py = 0; py < size; py++) {
                    float dist = (float) Math.sqrt((px - cx) * (px - cx) + (py - cy) * (py - cy));
                    if (dist <= r) {
                        float t = 1f - dist / r;
                        pixmap.setColor(1f, t * 0.75f, 0f, t * t);
                        pixmap.drawPixel(px, py);
                    }
                }
            }
            explosionTexture = new Texture(pixmap);
            pixmap.dispose();
        }
        return explosionTexture;
    }
}