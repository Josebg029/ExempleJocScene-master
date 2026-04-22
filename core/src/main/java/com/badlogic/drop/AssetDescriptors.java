package com.badlogic.drop;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;


public class AssetDescriptors {

    public static final AssetDescriptor<Texture> bucketTexture = new AssetDescriptor("bucket.png",Texture.class);
    public static final AssetDescriptor<Texture> dropletTexture = new AssetDescriptor("drop.png", Texture.class);
    public static final AssetDescriptor<Texture> background = new AssetDescriptor("background.png", Texture.class);
    public static final AssetDescriptor<Music> rainMusic = new AssetDescriptor("music.mp3", Music.class);
    public static final AssetDescriptor<Sound> dropSound = new AssetDescriptor("drop.mp3", Sound.class);
}
