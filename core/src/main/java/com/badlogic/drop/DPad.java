package com.badlogic.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * Controles táctiles: Volante (izquierda) para girar izq/der,
 * Pedales (derecha) para acelerador (delante) y freno (atrás).
 */
public class DPad {

    private final Stage stage;
    private final SpriteBatch batch;
    private final Texture volante;
    private final Texture pedales;

    // Volante: posición y tamaño en coords mundo
    private static final float VX    = 20f;
    private static final float VY    = 10f;
    private static final float VSIZE = 180f;

    // Ángulo actual del volante (grados)
    private float wheelAngle = 0f;
    private int   wheelPointer  = -1;
    private float wheelStartAngle = 0f;

    // Pedales: posición y tamaño en coords mundo
    private static final float PX = 630f;
    private static final float PY = 10f;
    private static final float PW = 140f;
    private static final float PH = 140f;

    public DPad(Stage stage) {
        this.stage = stage;
        batch = new SpriteBatch();
        volante = new Texture(Gdx.files.internal("Volante.png"));
        pedales = new Texture(Gdx.files.internal("Pedales.png"));
    }

    /** Izquierda si volante girado >25° a la izquierda */
    public boolean isLeft()  { return wheelAngle >  25f; }
    /** Derecha si volante girado >25° a la derecha */
    public boolean isRight() { return wheelAngle < -25f; }
    /** Acelerador = mitad derecha de Pedales.png */
    public boolean isAccel() { return isTouched(new Rectangle(PX + PW / 2f, PY, PW / 2f, PH)); }
    /** Freno = mitad izquierda de Pedales.png */
    public boolean isBrake() { return isTouched(new Rectangle(PX, PY, PW / 2f, PH)); }

    private boolean isTouched(Rectangle rect) {
        for (int i = 0; i < 5; i++) {
            if (!Gdx.input.isTouched(i)) continue;
            Vector3 w = stage.getCamera().unproject(
                new Vector3(Gdx.input.getX(i), Gdx.input.getY(i), 0));
            if (rect.contains(w.x, w.y)) return true;
        }
        return false;
    }

    /** Debe llamarse cada frame antes de isLeft()/isRight() */
    public void update() {
        float cx = VX + VSIZE / 2f;
        float cy = VY + VSIZE / 2f;
        float radius = VSIZE / 2f;

        boolean touching = false;
        for (int i = 0; i < 5; i++) {
            if (!Gdx.input.isTouched(i)) continue;
            Vector3 w = stage.getCamera().unproject(
                new Vector3(Gdx.input.getX(i), Gdx.input.getY(i), 0));
            float dx = w.x - cx, dy = w.y - cy;
            if (dx * dx + dy * dy > radius * radius) continue;

            float angle = MathUtils.atan2(dy, dx) * MathUtils.radiansToDegrees;
            if (wheelPointer == -1) {
                // Primer contacto: solo guardamos el ángulo actual como referencia
                wheelPointer    = i;
                wheelStartAngle = angle;
            } else if (wheelPointer == i) {
                // Delta incremental desde el frame anterior (evita inversión al girar >180°)
                float delta = angle - wheelStartAngle;
                while (delta >  180f) delta -= 360f;
                while (delta < -180f) delta += 360f;
                wheelAngle = MathUtils.clamp(wheelAngle + delta, -120f, 120f);
                // El nuevo ángulo de referencia es el ángulo actual
                wheelStartAngle = angle;
            }
            touching = true;
            break;
        }

        if (!touching) {
            wheelPointer = -1;
            // Volver al centro suavemente
            wheelAngle *= 0.75f;
            if (Math.abs(wheelAngle) < 1f) wheelAngle = 0f;
        }
    }

    public void draw() {
        batch.setProjectionMatrix(stage.getCamera().combined);
        batch.begin();

        // Dibujar volante rotado desde su centro
        batch.draw(
            volante,
            VX, VY,
            VSIZE / 2f, VSIZE / 2f,
            VSIZE, VSIZE,
            1f, 1f,
            wheelAngle,
            0, 0, volante.getWidth(), volante.getHeight(),
            false, false
        );

        // Dibujar pedales
        batch.draw(pedales, PX, PY, PW, PH);

        batch.end();
    }

    public void dispose() {
        batch.dispose();
        volante.dispose();
        pedales.dispose();
    }
}

