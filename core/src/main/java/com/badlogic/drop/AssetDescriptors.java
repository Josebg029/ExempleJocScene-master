package com.badlogic.drop;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;


public class AssetDescriptors {

    // Track
    public static final AssetDescriptor<Texture> carretera = new AssetDescriptor("Carretera.png", Texture.class);
    public static final AssetDescriptor<Texture> izq = new AssetDescriptor("IZQ.png", Texture.class);
    public static final AssetDescriptor<Texture> der = new AssetDescriptor("DER.png", Texture.class);

    // Player car
    public static final AssetDescriptor<Texture> cocheRojo   = new AssetDescriptor("Coche_Rojo.png",    Texture.class);

    // Enemy cars
    public static final AssetDescriptor<Texture> cocheAmarillo = new AssetDescriptor("Coche_Amarillo.png", Texture.class);
    public static final AssetDescriptor<Texture> cocheAzul     = new AssetDescriptor("Coche_Azul.png",    Texture.class);
    public static final AssetDescriptor<Texture> cocheBlanco   = new AssetDescriptor("Coche_Blanco.png",  Texture.class);
    public static final AssetDescriptor<Texture> cocheGris     = new AssetDescriptor("Coche_Gris.png",    Texture.class);
    public static final AssetDescriptor<Texture> cocheGris2    = new AssetDescriptor("Coche_Gris2.png",   Texture.class);
    public static final AssetDescriptor<Texture> cocheNegro    = new AssetDescriptor("Coche_Negro.png",   Texture.class);

    public static final AssetDescriptor<Music>   rainMusic      = new AssetDescriptor("Musica.mp3",    Music.class);
    public static final AssetDescriptor<Sound>   explosionSound = new AssetDescriptor("Explosion.mp3", Sound.class);
    public static final AssetDescriptor<Sound>   fahSound       = new AssetDescriptor("Fah.mp3",       Sound.class);
}
