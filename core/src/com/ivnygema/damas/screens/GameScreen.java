package com.ivnygema.damas.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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

    //HUD
    private SpriteBatch batch2 = new SpriteBatch();

    //
    Piece [][] piezasTablero = new Piece[8][8];
    Casilla[][] casillasTablero = new Casilla[8][8];
    Array<Casilla> posibles;
    Array<Casilla> peligros;
    Boolean selected;

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
        posibles = new Array<>();
        peligros = new Array<>();
        selected = false;

        generarBlancas();
        generarNegras();
        obtenerCasillas();


        Gdx.input.setInputProcessor(this);
    }

    private void generarBlancas(){
        MapLayer collisionsLayer = map.getLayers().get("blancas");

        for (MapObject object : collisionsLayer.getObjects()){
            RectangleMapObject rectangleObject = (RectangleMapObject) object;
            Rectangle rectangle = rectangleObject.getRectangle();

            int i = (int)rectangleObject.getProperties().get("i");
            int j = (int)rectangleObject.getProperties().get("j");
            piezasTablero[i][j] = new Piece(new Vector2(rectangle.x,rectangle.y),"b", i, j);
        }

    }

    private void generarNegras(){
        MapLayer collisionsLayer = map.getLayers().get("negras");

        for (MapObject object : collisionsLayer.getObjects()){
            RectangleMapObject rectangleObject = (RectangleMapObject) object;
            Rectangle rectangle = rectangleObject.getRectangle();

            int i = (int)rectangleObject.getProperties().get("i");
            int j = (int)rectangleObject.getProperties().get("j");
            piezasTablero[i][j] = new Piece(new Vector2(rectangle.x,rectangle.y),"n", i, j);
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

    private void calcularPosibles(){
        int ajuste = 0;
        posibles.clear();

        if(piezasTablero[auxi][auxj].isBlack())
            ajuste = 1;
        else
            ajuste = -1;

        if((piezasTablero[auxi][auxj].isWhite() && auxi == 0) || (piezasTablero[auxi][auxj].isBlack() && auxi == 7)){
            System.out.println("no puedes mover marinero");
        }else {
            if (auxj > 0 && auxj < 7) {
                if (piezasTablero[auxi + ajuste][auxj - 1] == null) {
                    posibles.add(new Casilla(new Rectangle(), auxi + ajuste, auxj - 1));
                } else {
                    // blancas comer IZQUIERDA
                    if (auxi > 1 && auxj > 1 && piezasTablero[auxi][auxj].isWhite()) {
                        if (piezasTablero[auxi + ajuste][auxj - 1].isBlack() && piezasTablero[auxi + ajuste * 2][auxj - 2] == null) {
                            posibles.add(new Casilla(new Rectangle(), auxi + ajuste * 2, auxj - 2));
                            peligros.add(new Casilla(new Rectangle(), auxi + ajuste, auxj - 1));
                            System.out.println("COMEMETA IZQ B");
                        }
                    }
                    // NEGRAS comer IZQUIERDA
                    if (auxi < 6 && auxj > 1 && piezasTablero[auxi][auxj].isBlack()) {
                        if (piezasTablero[auxi + ajuste][auxj - 1].isWhite() && piezasTablero[auxi + ajuste * 2][auxj - 2] == null) {
                            posibles.add(new Casilla(new Rectangle(), auxi + ajuste * 2, auxj - 2));
                            peligros.add(new Casilla(new Rectangle(), auxi + ajuste, auxj - 1));
                            System.out.println("COMEMETA IZQ N");
                        }
                    }
                }
                if (piezasTablero[auxi + ajuste][auxj + 1] == null) {
                    posibles.add(new Casilla(new Rectangle(), auxi + ajuste, auxj + 1));
                } else {
                    // blancas comer DERECHA
                    if (auxi > 1 && auxj < 6 && piezasTablero[auxi][auxj].isWhite()) {
                        if (piezasTablero[auxi + ajuste][auxj + 1].isBlack() && piezasTablero[auxi + ajuste * 2][auxj + 2] == null) {
                            posibles.add(new Casilla(new Rectangle(), auxi + ajuste * 2, auxj + 2));
                            peligros.add(new Casilla(new Rectangle(), auxi + ajuste , auxj + 1));
                            System.out.println("COMEMETA DER B");
                        }
                    }
                    // negras comer DERECHA
                    if (auxi < 6 && auxj < 6 && piezasTablero[auxi][auxj].isBlack()) {
                        if (piezasTablero[auxi + ajuste][auxj + 1].isWhite() && piezasTablero[auxi + ajuste * 2][auxj + 2] == null) {
                            posibles.add(new Casilla(new Rectangle(), auxi + ajuste * 2, auxj + 2));
                            peligros.add(new Casilla(new Rectangle(), auxi + ajuste, auxj + 1));
                            System.out.println("COMEMETA DER N");
                        }
                    }
                }
            } else {
                if (auxj == 0) {
                    if (piezasTablero[auxi + ajuste][auxj + 1] == null) {
                        posibles.add(new Casilla(new Rectangle(), auxi + ajuste, auxj + 1));
                    } else {
                        // blancas comer DERECHA
                        if (auxi > 1 && piezasTablero[auxi][auxj].isWhite()) {
                            if (piezasTablero[auxi + ajuste][auxj + 1].isBlack() && piezasTablero[auxi + ajuste * 2][auxj + 2] == null) {
                                posibles.add(new Casilla(new Rectangle(), auxi + ajuste * 2, auxj + 2));
                                peligros.add(new Casilla(new Rectangle(), auxi + ajuste , auxj + 1));
                                System.out.println("COMEMETA DER B");
                            }
                        }
                        // NEGRAS comer DERECHA
                        if (auxi < 6 && piezasTablero[auxi][auxj].isBlack()) {
                            if (piezasTablero[auxi + ajuste][auxj + 1].isWhite() && piezasTablero[auxi + ajuste * 2][auxj + 2] == null) {
                                posibles.add(new Casilla(new Rectangle(), auxi + ajuste * 2, auxj + 2));
                                peligros.add(new Casilla(new Rectangle(), auxi + ajuste , auxj + 1));
                                System.out.println("COMEMETA DER N");
                            }
                        }
                    }
                }
                if (auxj == 7) {
                    if (piezasTablero[auxi + ajuste][auxj - 1] == null) {
                        posibles.add(new Casilla(new Rectangle(), auxi + ajuste, auxj - 1));
                    } else {
                        // blancas comer IZQUIERDA
                        if (auxi > 1 && piezasTablero[auxi][auxj].isWhite()) {
                            if (piezasTablero[auxi + ajuste][auxj - 1].isBlack() && piezasTablero[auxi + ajuste * 2][auxj - 2] == null) {
                                posibles.add(new Casilla(new Rectangle(), auxi + ajuste * 2, auxj - 2));
                                peligros.add(new Casilla(new Rectangle(), auxi + ajuste , auxj - 1));
                                System.out.println("COMEMETA IZQ B");
                            }
                        }
                        // negras comer IZQUIERDA
                        if (auxi < 6 && piezasTablero[auxi][auxj].isBlack()) {
                            if (piezasTablero[auxi + ajuste][auxj - 1].isWhite() && piezasTablero[auxi + ajuste * 2][auxj - 2] == null) {
                                posibles.add(new Casilla(new Rectangle(), auxi + ajuste * 2, auxj - 2));
                                peligros.add(new Casilla(new Rectangle(), auxi + ajuste , auxj - 1));
                                System.out.println("COMEMETA IZQ N");
                            }
                        }
                    }
                }
            }
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

        if(selected)
            batch.draw(ResourceManager.selection,piezasTablero[auxi][auxj].getX(),piezasTablero[auxi][auxj].getY());

        for(Casilla casilla: posibles){
            batch.draw(ResourceManager.posibles, casillasTablero[casilla.getI()][casilla.getJ()].getRect().getX() ,casillasTablero[casilla.getI()][casilla.getJ()].getRect().getY());
        }

        for(Casilla casilla: peligros){
            batch.draw(ResourceManager.danger, casillasTablero[casilla.getI()][casilla.getJ()].getRect().getX() ,casillasTablero[casilla.getI()][casilla.getJ()].getRect().getY());
        }


        //batch.setProjectionMatrix(camera.projection);
        for(int i = 0 ; i < piezasTablero.length; i++)
            for(int j = 0 ; j < piezasTablero[0].length; j++)
                if(piezasTablero[i][j] != null)
                    piezasTablero[i][j].draw(batch);

        batch.end();
    }

    private void resetSelection(){
       selected = false;
       posibles.clear();
       peligros.clear();
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
                        selected = true;
                        calcularPosibles();

                    }

        for(Casilla casilla: posibles){
            Rectangle rect = new Rectangle(casillasTablero[casilla.getI()][casilla.getJ()].getRect().getX() ,casillasTablero[casilla.getI()][casilla.getJ()].getRect().getY(),64,64);
            if(rect.contains(pulsacion)){
                System.out.println("OKEYMARINERO");
                piezasTablero[casilla.getI()][casilla.getJ()]=piezasTablero[auxi][auxj];
                piezasTablero[auxi][auxj]=null;
                piezasTablero[casilla.getI()][casilla.getJ()].setI(casilla.getI());
                piezasTablero[casilla.getI()][casilla.getJ()].setJ(casilla.getJ());
                piezasTablero[casilla.getI()][casilla.getJ()].setRect(casillasTablero[casilla.getI()][casilla.getJ()].getRect());

                resetSelection();
            }
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
