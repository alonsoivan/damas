package com.ivnygema.damas.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.ivnygema.damas.Aplication;
import com.ivnygema.damas.managers.ResourceManager;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisImage;
import com.kotcrab.vis.ui.widget.VisTable;

import static com.ivnygema.damas.managers.ResourceManager.*;
import static com.ivnygema.damas.util.Constantes.TILE_WIDTH;

public class MainScreen implements Screen {

    private Stage stage;

    Game game;
    public MainScreen(Game game){
        this.game = game;
    }


    private Batch batch;
    public static OrthographicCamera camera;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    @Override
    public void show() {

        Aplication.adService.showAds(true);

        camera = new OrthographicCamera();
        //camera.setToOrtho(false, TILES_IN_CAMERA_WIDTH * TILE_WIDTH, TILES_IN_CAMERA_HEIGHT * TILE_WIDTH);

        float widthS = TILE_WIDTH * 8;
        float heightS = widthS * Gdx.graphics.getHeight() / Gdx.graphics.getWidth();
        camera.setToOrtho(false, widthS , heightS);
        camera.update();

        float porcentajeBannerEnPantalla = Aplication.adService.getBannerHeight()/2 * 100 / Gdx.graphics.getHeight();
        //camera.position.y = casillasTablero[3][1].rect.y - camera.viewportHeight*((porcentajeBannerEnPantalla+0.5f)/100);


        map = new TmxMapLoader().load("tablero.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1);
        batch = renderer.getBatch();

        if (!VisUI.isLoaded())
            VisUI.load();


        stage = new Stage();

        Texture textureLogo = new Texture(Gdx.files.internal("logo.png"),true);
        textureLogo.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        VisImage logo = new VisImage(textureLogo);

        VisTable table = new VisTable(true);
        //table.setFillParent(true);
        table.setPosition(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/1.75f);
        stage.addActor(table);


        Skin skin = new Skin(Gdx.files.internal("skins/flat-earth/skin/flat-earth-ui.json"));
        //TextButton.TextButtonStyle textButtonStyle = skin.get("default", TextButton.TextButtonStyle.class);
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle(table.getSkin().get(TextButton.TextButtonStyle.class));
        textButtonStyle.font = ResourceManager.titleFont;
        textButtonStyle.downFontColor =  new Color(Integer.parseInt("483e37ff", 16));
        textButtonStyle.up = new TextureRegionDrawable(new TextureRegion(fondo1));
        textButtonStyle.down = new TextureRegionDrawable(new TextureRegion(fondo2));

        TextButton playButton = new TextButton("PLAYER VS PLAYER",textButtonStyle);
        playButton.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                clickSound.play(0.7f);
                game.setScreen(new GameScreen(false,game));
                dispose();
            }
        });

        TextButton configButton = new TextButton("PLAYER VS CPU",textButtonStyle);
        configButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clickSound.play(0.7f);
                game.setScreen(new GameScreen(true,game));
                dispose();
            }
        });

        float width = Gdx.graphics.getWidth()*0.85f;
        float height = Gdx.graphics.getHeight()*0.10f;
        float pad = Gdx.graphics.getHeight()*0.030f;

        float logoWidth = Gdx.graphics.getWidth()*0.75f;


        // Añade filas a la tabla y añade los componentes
        table.row();
        table.add(logo).center().width(logoWidth).height(logoWidth).pad(pad*2.5f);
        table.row();
        table.add(playButton).center().width(width).height(height).pad(pad);
        table.row();
        table.add(configButton).center().width(width).height(height).pad(pad);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        camera.update();
        renderer.setView(camera);

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.render();

        batch.begin();
        batch.end();

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
        //VisUI.dispose();
    }
}
