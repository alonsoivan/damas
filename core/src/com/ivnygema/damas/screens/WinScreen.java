package com.ivnygema.damas.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.ivnygema.damas.managers.Animator;
import com.ivnygema.damas.managers.ResourceManager;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisImage;
import com.kotcrab.vis.ui.widget.VisTable;

import static com.ivnygema.damas.managers.ResourceManager.*;

public class WinScreen implements Screen {
    private Stage stage;

    private String ganador;
    Game game;
    public WinScreen(String ganador, Game game){
        super();
        this.ganador = ganador;
        this.game = game;
    }

    Drawable up;
    Drawable down;

    Animator animator;
    @Override
    public void show() {
        animator = new Animator();
        animator.create();

        if (!VisUI.isLoaded())
            VisUI.load();

        stage = new Stage();

        winSound.play(0.7f);

        final VisTable table = new VisTable(true);
        //table.setFillParent(true);
        table.setPosition(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/1.75f);
        stage.addActor(table);

        VisImage image = new VisImage();
        image.setDrawable(ganador.equals("b") ? ResourceManager.blancasTexture : ResourceManager.negrasTexture);

        up =  table.getSkin().get(TextButton.TextButtonStyle.class).up;
        down = table.getSkin().get(TextButton.TextButtonStyle.class).down;

        TextButton.TextButtonStyle textButtonStyle = table.getSkin().get(TextButton.TextButtonStyle.class);
        textButtonStyle.font = ResourceManager.titleFont;
        textButtonStyle.downFontColor =  new Color(Integer.parseInt("483e37ff", 16));
        textButtonStyle.up = new TextureRegionDrawable(new TextureRegion(fondo1));
        textButtonStyle.down = new TextureRegionDrawable(new TextureRegion(fondo2));


        TextButton playButton = new TextButton("OK",textButtonStyle);
        playButton.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {

                table.getSkin().get(TextButton.TextButtonStyle.class).up = up;
                table.getSkin().get(TextButton.TextButtonStyle.class).down = down;

                ((Game) Gdx.app.getApplicationListener()).setScreen(new MainScreen(game));
                dispose();
            }
        });

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = titleFont;

        Label aboutLabel = new Label("VICTORY!",labelStyle);
        aboutLabel.setAlignment(Align.center);

        float width = Gdx.graphics.getWidth()*0.85f;
        float pad = Gdx.graphics.getHeight()*0.030f;

        // Añade filas a la tabla y añade los componentes
        table.row();
        table.add(image).center().width(width).height(width).pad(pad*2f);
        table.row();
        table.add(aboutLabel).center().width(width).height(width/4).pad(pad);
        table.row();
        table.add(playButton).center().width(width).height(width/4).pad(pad);


        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        animator.render();

        // Pinta la UI en la pantalla
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        // Redimensiona la escena al redimensionar la ventana del juego
        stage.getViewport().update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        // Libera los recursos de la escena
        stage.dispose();
    }
}
