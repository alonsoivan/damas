package com.ivnygema.damas.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

import java.io.File;

public class ResourceManager {

    public static FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Aqum.otf"));
    public static FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
    public static BitmapFont titleFont;

    public static AssetManager assets = new AssetManager();

    public static Texture blancasTexture;
    public static Texture damabTexture;
    public static Texture damanTexture;
    public static Texture negrasTexture;
    public static Texture selection;
    public static Texture posibles;
    public static Texture movibles;
    public static Texture danger;
    public static Texture hint;
    public static Texture quit;
    public static Texture fondo1;
    public static Texture fondo2;


    // SOUNDS
    public static Sound selectionSound;
    public static Sound moveSound;
    public static Sound comerSound;
    public static Sound damaSound;
    public static Sound clickSound;
    public static Sound winSound;
    public static Sound quitSound;
    public static Sound resetSound;

    public static void loadAllResources() {
        selectionSound = Gdx.audio.newSound(Gdx.files.internal("sounds/click.mp3"));
        moveSound = Gdx.audio.newSound(Gdx.files.internal("sounds/moved.wav"));
        comerSound = Gdx.audio.newSound(Gdx.files.internal("sounds/comer2.ogg"));
        damaSound = Gdx.audio.newSound(Gdx.files.internal("sounds/dama.mp3"));
        clickSound = Gdx.audio.newSound(Gdx.files.internal("sounds/click.mp3"));
        winSound = Gdx.audio.newSound(Gdx.files.internal("sounds/win.wav"));
        quitSound = Gdx.audio.newSound(Gdx.files.internal("sounds/exit.wav"));
        resetSound = Gdx.audio.newSound(Gdx.files.internal("sounds/reset.wav"));


        blancasTexture = new Texture(Gdx.files.internal("blanca.png"),true);
        blancasTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        damabTexture = new Texture(Gdx.files.internal("damab.png"),true);
        damabTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        damanTexture = new Texture(Gdx.files.internal("daman.png"),true);
        damanTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        negrasTexture = new Texture(Gdx.files.internal("negra.png"),true);
        negrasTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        selection = new Texture(Gdx.files.internal("selection.png"),true);
        selection.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        posibles = new Texture(Gdx.files.internal("posibles.png"),true);
        posibles.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        movibles = new Texture(Gdx.files.internal("movibles.png"),true);
        movibles.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        danger = new Texture(Gdx.files.internal("danger.png"),true);
        danger.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        hint = new Texture(Gdx.files.internal("hint.png"),true);
        hint.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        quit = new Texture(Gdx.files.internal("quit.png"),true);
        quit.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        fondo1 = new Texture(Gdx.files.internal("fondo1.png"),true);
        fondo1.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        fondo2 = new Texture(Gdx.files.internal("fondo2.png"),true);
        fondo2.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        // font
        parameter.size = (int)(Gdx.graphics.getWidth()*0.07);
        //parameter.borderWidth = (int)(Gdx.graphics.getWidth()*0.01);
        parameter.color = Color.WHITE;

        titleFont = generator.generateFont(parameter);
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
