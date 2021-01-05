package com.ivnygema.damas.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.io.File;

public class ResourceManager {

    public static AssetManager assets = new AssetManager();

    public static Texture blancasTexture;
    public static Texture damabTexture;
    public static Texture damanTexture;
    public static Texture negrasTexture;
    public static Texture selection;
    public static Texture posibles;
    public static Texture danger;
    public static Texture hint;


    // SOUNDS
    public static Sound selectionSound;
    public static Sound moveSound;
    public static Sound comerSound;
    public static Sound damaSound;

    public static void loadAllResources() {
        blancasTexture = new Texture("blanca.png");
        damabTexture = new Texture("damab.png");
        damanTexture = new Texture("daman.png");
        negrasTexture = new Texture("negra.png");
        selection = new Texture("selection.png");
        posibles = new Texture("posibles.png");
        danger = new Texture("danger.png");
        hint = new Texture("hint.png");
        selectionSound = Gdx.audio.newSound(Gdx.files.internal("sounds/selected.wav"));
        moveSound = Gdx.audio.newSound(Gdx.files.internal("sounds/moved.wav"));
        comerSound = Gdx.audio.newSound(Gdx.files.internal("sounds/comer.mp3"));
        damaSound = Gdx.audio.newSound(Gdx.files.internal("sounds/dama.mp3"));
    }

    /** Actualiza la carga de recursos */
    public static boolean update() {
        return assets.update();
    }

    /**
     * Carga los sonidos
     */
    public static void loadSounds() {
        assets.load("sounds" + File.separator + "game_begin.wav", Sound.class);
    }

    /**
     * Carga las músicas
     */
    public static void loadMusics() {
        assets.load("musics" + File.separator + "bso.mp3", Music.class);
    }

    /**
     * Obtiene una región de textura o la primera de una animación
     * @param name
     * @return
     */
    public static TextureRegion getRegion(String name, String texture) {
        return assets.get(texture, TextureAtlas.class).findRegion(name);
    }

    /**
     * Obtiene una región de textura determinada de las que forman una animación
     */
    /*
    public static TextureRegion getRegion(String name, int position) {
        return assets.get(TEXTURE_ATLAS, TextureAtlas.class).findRegion(name, position);
    }
*/
    /**
     * Obtiene todas las regiones de textura que forman una misma animación
     * @param name
     * @return
     *//*
    public static Array<TextureAtlas.AtlasRegion> getRegions(String name) {
        return assets.get(TEXTURE_ATLAS, TextureAtlas.class).findRegions(name);
    }
*/
    /**
     * Obtiene un sonido determinado
     */
    public static Sound getSound(String name) {
        return assets.get(name, Sound.class);
    }

    /**
     * Obtiene una música determinada
     */
    public static Music getMusic(String name) {
        return assets.get(name, Music.class);
    }
}
