package com.ivnygema.damas.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
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
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.ivnygema.damas.managers.ResourceManager;
import com.ivnygema.damas.models.Casilla;
import com.ivnygema.damas.models.Piece;

import static com.ivnygema.damas.util.Constantes.*;

public class GameScreen implements Screen, InputProcessor {

    // TiledMap
    private Batch batch;
    public static OrthographicCamera camera;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    //
    Piece [][] piezasTablero = new Piece[8][8];
    Casilla[][] casillasTablero = new Casilla[8][8];

    protected int auxi = 0,auxj = 0;

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

        generarBlancas();
        generarNegras();
        obtenerCasillas();


        Gdx.input.setInputProcessor(this);
    }

    private void generarBlancas(){
        MapLayer collisionsLayer = map.getLayers().get("blancas");

        for (MapObject object : collisionsLayer.getObjects()){
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
            RectangleMapObject rectangleObject = (RectangleMapObject) object;

            piezasTablero[(int)rectangleObject.getProperties().get("i")][(int)rectangleObject.getProperties().get("j")] = new Piece(new Vector2(rectangle.x,rectangle.y),"b",(int)rectangleObject.getProperties().get("i"),(int)rectangleObject.getProperties().get("j"));
        }

    }

    private void generarNegras(){
        MapLayer collisionsLayer = map.getLayers().get("negras");

        for (MapObject object : collisionsLayer.getObjects()){
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
            RectangleMapObject rectangleObject = (RectangleMapObject) object;

            piezasTablero[(int)rectangleObject.getProperties().get("i")][(int)rectangleObject.getProperties().get("j")] = new Piece(new Vector2(rectangle.x,rectangle.y),"n",(int)rectangleObject.getProperties().get("i"),(int)rectangleObject.getProperties().get("j"));
        }

    }

    private void obtenerCasillas(){
        MapLayer collisionsLayer = map.getLayers().get("casillas");

        for (MapObject object : collisionsLayer.getObjects()){
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();

            RectangleMapObject rectangleObject = (RectangleMapObject) object;
            casillasTablero[(int)rectangleObject.getProperties().get("i")][(int)rectangleObject.getProperties().get("j")] = new Casilla(rectangle,(int)rectangleObject.getProperties().get("i"),(int)rectangleObject.getProperties().get("j"));
        }
    }


    @Override
    public void render(float delta) {

        
        pintar();
    }

    public void pintar(){
        handleCamera();

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.render();

        batch.begin();


        batch.draw(ResourceManager.selection,piezasTablero[auxi][auxj].getX(),piezasTablero[auxi][auxj].getY());

        for(int i = 0 ; i < piezasTablero.length; i++)
            for(int j = 0 ; j < piezasTablero[0].length; j++)
                if(piezasTablero[i][j] != null)
                    piezasTablero[i][j].draw(batch);

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

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        //new Vector2(getMousePosInGameWorld().x,getMousePosInGameWorld().y)

        Vector2 pulsacion = new Vector2(getMousePosInGameWorld().x, getMousePosInGameWorld().y);

        for(int i = 0 ; i < piezasTablero.length; i++)
            for(int j = 0 ; j < piezasTablero[0].length; j++)
                if(piezasTablero[i][j] != null)
                    if(piezasTablero[i][j].getRect().contains(pulsacion)) {
                        auxi = i;
                        auxj = j;
                    }

        System.out.println(auxi+" - "+auxj);

        return false;
    }

    public static Vector3 getMousePosInGameWorld() {
        return camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
