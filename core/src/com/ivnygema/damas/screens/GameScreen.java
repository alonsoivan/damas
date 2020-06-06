package com.ivnygema.damas.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.ivnygema.damas.models.Piece;

import static com.ivnygema.damas.util.Constantes.*;

public class GameScreen implements Screen {

    // TiledMap
    private Batch batch;
    public static OrthographicCamera camera;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    //
    Array<Piece> blancas;
    Array<Piece> negras;

    Piece [][] piezasTablero = new Piece[8][8];
    Piece [][] casillasTablero = new Piece[8][8];

    @Override
    public void show() {
        // Tiledmap
        camera = new OrthographicCamera();
        camera.setToOrtho(false, TILES_IN_CAMERA_WIDTH * TILE_WIDTH, TILES_IN_CAMERA_HEIGHT * TILE_WIDTH);
        camera.update();

        map = new TmxMapLoader().load("tablero.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1);
        batch = renderer.getBatch();

        //
        blancas = new Array<>();
        negras = new Array<>();

        generarBlancas();
        generarNegras();
        obtenerCasillas();
    }

    private void generarBlancas(){
        MapLayer collisionsLayer = map.getLayers().get("blancas");

        for (MapObject object : collisionsLayer.getObjects()){
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
            RectangleMapObject rectangleObject = (RectangleMapObject) object;


            blancas.add(new Piece(new Vector2(rectangle.x,rectangle.y),"b",(int)rectangleObject.getProperties().get("i"),(int)rectangleObject.getProperties().get("j")));

        }

    }

    private void generarNegras(){
        MapLayer collisionsLayer = map.getLayers().get("negras");

        for (MapObject object : collisionsLayer.getObjects()){
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
            RectangleMapObject rectangleObject = (RectangleMapObject) object;


            negras.add(new Piece(new Vector2(rectangle.x,rectangle.y),"n",(int)rectangleObject.getProperties().get("i"),(int)rectangleObject.getProperties().get("j")));
        }

    }

    private void obtenerCasillas(){
        MapLayer collisionsLayer = map.getLayers().get("casillas");

        for (MapObject object : collisionsLayer.getObjects()){
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();

            RectangleMapObject rectangleObject = (RectangleMapObject) object;
            System.out.println( rectangleObject.getProperties().get("j"));
        }

    }


    @Override
    public void render(float delta) {
        actualizar();
        pintar();
    }

    public void pintar(){
        handleCamera();

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        renderer.render();



        batch.begin();

        for(Piece piece : blancas)
            piece.draw(batch);

        for(Piece piece : negras)
            piece.draw(batch);

        batch.end();
    }

    public void actualizar(){

        //comprobarcolisiones();

    }

    private void handleCamera() {
        // These values likely need to be scaled according to your world coordinates.

        // The top boundary of the map (y + height)
        float mapTop = 0 + TILES_IN_CAMERA_HEIGHT * TILE_WIDTH * 4f;

        // The bottom boundary of the map (y)
        int mapBottom = 0;

        // The left boundary of the map (x)
        int mapLeft = 0;

        // The right boundary of the map (x + width)
        float mapRight = TILES_IN_CAMERA_WIDTH * TILE_WIDTH * 4f ;

        // The camera dimensions, halved
        float cameraHalfWidth = camera.viewportWidth * .5f;
        float cameraHalfHeight = camera.viewportHeight * .5f;

        // Move camera after player as normal

        float cameraLeft = camera.position.x - cameraHalfWidth;
        float cameraRight = camera.position.x + cameraHalfWidth;
        float cameraBottom = camera.position.y - cameraHalfHeight;
        float cameraTop = camera.position.y + cameraHalfHeight;

        // Horizontal axis
        if(mapRight < camera.viewportWidth)
        {
            camera.position.x = mapRight / 2;
        }
        else if(cameraLeft <= mapLeft)
        {
            camera.position.x = mapLeft + cameraHalfWidth;
        }
        else if(cameraRight >= mapRight)
        {
            camera.position.x = mapRight - cameraHalfWidth;
        }


        // Vertical axis
        if(mapTop < camera.viewportHeight)
        {
            camera.position.y = mapTop / 2;
        }
        else if(cameraBottom <= mapBottom)
        {
            camera.position.y = mapBottom + cameraHalfHeight;
        }
        else if(cameraTop >= mapTop)
        {
            camera.position.y = mapTop - cameraHalfHeight;
        }


        camera.update();
        renderer.setView(camera);
    }

    @Override
    public void resize(int width, int height) {

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

    }
}
