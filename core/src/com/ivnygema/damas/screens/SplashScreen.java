package com.ivnygema.damas.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.ivnygema.damas.Aplication;
import com.ivnygema.damas.managers.ResourceManager;

public class SplashScreen implements Screen {
    private Texture splashTexture;
    private Image splashImage;
    private Stage stage;

    private boolean splashDone = false;

    private Aplication game;

    float width = Gdx.graphics.getHeight()*0.3f;
    public SplashScreen(Aplication game) {
        this.game = game;

        Aplication.adService.showAds(false);

        splashTexture = new Texture(Gdx.files.internal("logo.png"));
        splashImage = new Image(splashTexture);
        splashImage.setOrigin(width/2, width/2);

        stage = new Stage();
    }

    @Override
    public void show() {
        Table table = new Table();
        table.setFillParent(true);
        table.center();

        // Muestra la imagen de SplashScreen como una animación
        splashImage.addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(1f),
                Actions.delay(1.5f), Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        splashDone = true;
                    }
                })
        ));
        splashImage.addAction(Actions.repeat(RepeatAction.FOREVER, Actions.rotateBy(12, 0.2f)));


        table.row();
        table.add(splashImage).center().width(width).height(width);
        stage.addActor(table);

        // Lanza la carga de recursos
        ResourceManager.loadAllResources();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        //Gdx.gl.glClear(GL20.GL_COLOR_CLEAR_VALUE);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.draw();

        // Comprueba si se han cargado todos los recursos
        if (ResourceManager.update()) {
            // Si la animación ha terminado se muestra ya el menú principal
            if (splashDone)
                game.setScreen(new MainScreen(game));

        }
    }

    @Override
    public void resize ( int width, int height){

    }

    @Override
    public void pause () {

    }

    @Override
    public void resume () {

    }

    @Override
    public void hide () {
        dispose();
    }

    @Override
    public void dispose () {
        splashTexture.dispose();
        stage.dispose();
    }
}