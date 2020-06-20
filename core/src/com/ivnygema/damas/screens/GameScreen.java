package com.ivnygema.damas.screens;


import com.badlogic.gdx.Game;
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
import com.ivnygema.damas.managers.HUD;
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
    HUD hud = new HUD();
    private SpriteBatch batch2 = new SpriteBatch();

    //
    Piece [][] piezasTablero = new Piece[8][8];
    public static Casilla[][] casillasTablero = new Casilla[8][8];
    Array<Casilla> posibles;
    Array<Casilla> peligros;
    Boolean selected;

    int contB = 0;
    int contBD = 0;
    int contN = 0;
    int contND = 0;

    int contTurno = 0;
    protected int selecti = 0, selectj = 0;

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

    private void calcularPosibles() {
        posibles.clear();
        peligros.clear();

        if(piezasTablero[selecti][selectj].isDama())
            movimientoDama();
        else
            movimientoNormal();

    }

    public void movimientoDama() {
        int auxi = selecti;
        int auxj = selectj;
        boolean encontrado = false;

        // Diagonal arriba izq
        while(--auxi >= 0 && --auxj >= 0 && !encontrado){
            if(piezasTablero[auxi][auxj] == null)
                posibles.add(new Casilla(new Rectangle(), auxi, auxj));
            else
                if (piezasTablero[selecti][selectj].mismoColor(piezasTablero[auxi][auxj]))
                    encontrado = true;
                else{
                    if(auxi -1 >= 0 && auxj -1 >= 0)
                        if(piezasTablero[auxi -1][auxj -1] == null){
                            peligros.add(new Casilla(new Rectangle(), auxi, auxj));
                            posibles.add(new Casilla(new Rectangle(), auxi -1, auxj -1, auxi, auxj));
                        }
                    encontrado = true;
                }
        }

        auxi = selecti;
        auxj = selectj;
        encontrado = false;
        // Diagonal arriba der
        while(--auxi >= 0 && ++auxj <= 7 && !encontrado){
            if(piezasTablero[auxi][auxj] == null)
                posibles.add(new Casilla(new Rectangle(), auxi, auxj));
            else if (piezasTablero[selecti][selectj].mismoColor(piezasTablero[auxi][auxj]))
                encontrado = true;
            else{
                if(auxi -1 >= 0 && auxj +1 <= 7)
                    if(piezasTablero[auxi -1][auxj +1] == null){
                        peligros.add(new Casilla(new Rectangle(), auxi, auxj));
                        posibles.add(new Casilla(new Rectangle(), auxi -1, auxj +1, auxi, auxj));
                    }
                encontrado = true;
            }
        }

        auxi = selecti;
        auxj = selectj;
        encontrado = false;
        // Diagonal abajo izq
        while(++auxi <= 7 && --auxj >= 0 && !encontrado){
            if(piezasTablero[auxi][auxj] == null)
                posibles.add(new Casilla(new Rectangle(), auxi, auxj));
            else if (piezasTablero[selecti][selectj].mismoColor(piezasTablero[auxi][auxj]))
                encontrado = true;
            else{
                if(auxi +1 <=7 && auxj -1 >=0)
                    if(piezasTablero[auxi +1][auxj -1] == null){
                        peligros.add(new Casilla(new Rectangle(), auxi, auxj));
                        posibles.add(new Casilla(new Rectangle(), auxi +1, auxj -1, auxi, auxj));
                    }
                encontrado = true;
            }
        }

        auxi = selecti;
        auxj = selectj;
        encontrado = false;
        // Diagonal abajo der
        while(++auxi <= 7 && ++auxj <= 7 && !encontrado){
            if(piezasTablero[auxi][auxj] == null)
                posibles.add(new Casilla(new Rectangle(), auxi, auxj));
            else if (piezasTablero[selecti][selectj].mismoColor(piezasTablero[auxi][auxj]))
                encontrado = true;
            else{
                if(auxi +1 <=7 && auxj +1 <=7)
                    if(piezasTablero[auxi +1][auxj +1] == null){
                        peligros.add(new Casilla(new Rectangle(), auxi, auxj));
                        posibles.add(new Casilla(new Rectangle(), auxi +1, auxj +1, auxi, auxj));
                    }
                encontrado = true;
            }
        }
    }

    public void movimientoNormal(){
        int ajuste = 0;

        if(piezasTablero[selecti][selectj].isBlack())
            ajuste = 1;
        else
            ajuste = -1;

        if((piezasTablero[selecti][selectj].isWhite() && selecti == 0) || (piezasTablero[selecti][selectj].isBlack() && selecti == 7)){
            System.out.println("no puedes mover marinero");
        }else {
            if (selectj > 0 && selectj < 7) {
                if (piezasTablero[selecti + ajuste][selectj - 1] == null) {
                    posibles.add(new Casilla(new Rectangle(), selecti + ajuste, selectj - 1));
                } else {
                    // blancas comer IZQUIERDA
                    if (selecti > 1 && selectj > 1 && piezasTablero[selecti][selectj].isWhite()) {
                        if (piezasTablero[selecti + ajuste][selectj - 1].isBlack() && piezasTablero[selecti + ajuste * 2][selectj - 2] == null) {
                            posibles.add(new Casilla(new Rectangle(), selecti + ajuste * 2, selectj - 2, selecti + ajuste, selectj - 1));
                            peligros.add(new Casilla(new Rectangle(), selecti + ajuste, selectj - 1));
                            System.out.println("COMEMETA IZQ B");
                        }
                    }
                    // NEGRAS comer IZQUIERDA
                    if (selecti < 6 && selectj > 1 && piezasTablero[selecti][selectj].isBlack()) {
                        if (piezasTablero[selecti + ajuste][selectj - 1].isWhite() && piezasTablero[selecti + ajuste * 2][selectj - 2] == null) {
                            posibles.add(new Casilla(new Rectangle(), selecti + ajuste * 2, selectj - 2, selecti + ajuste, selectj - 1));
                            peligros.add(new Casilla(new Rectangle(), selecti + ajuste, selectj - 1));
                            System.out.println("COMEMETA IZQ N");
                        }
                    }
                }
                if (piezasTablero[selecti + ajuste][selectj + 1] == null) {
                    posibles.add(new Casilla(new Rectangle(), selecti + ajuste, selectj + 1));
                } else {
                    // blancas comer DERECHA
                    if (selecti > 1 && selectj < 6 && piezasTablero[selecti][selectj].isWhite()) {
                        if (piezasTablero[selecti + ajuste][selectj + 1].isBlack() && piezasTablero[selecti + ajuste * 2][selectj + 2] == null) {
                            posibles.add(new Casilla(new Rectangle(), selecti + ajuste * 2, selectj + 2, selecti + ajuste , selectj + 1));
                            peligros.add(new Casilla(new Rectangle(), selecti + ajuste , selectj + 1));
                            System.out.println("COMEMETA DER B");
                        }
                    }
                    // negras comer DERECHA
                    if (selecti < 6 && selectj < 6 && piezasTablero[selecti][selectj].isBlack()) {
                        if (piezasTablero[selecti + ajuste][selectj + 1].isWhite() && piezasTablero[selecti + ajuste * 2][selectj + 2] == null) {
                            posibles.add(new Casilla(new Rectangle(), selecti + ajuste * 2, selectj + 2, selecti + ajuste, selectj + 1));
                            peligros.add(new Casilla(new Rectangle(), selecti + ajuste, selectj + 1));
                            System.out.println("COMEMETA DER N");
                        }
                    }
                }
            } else {
                if (selectj == 0) {
                    if (piezasTablero[selecti + ajuste][selectj + 1] == null) {
                        posibles.add(new Casilla(new Rectangle(), selecti + ajuste, selectj + 1));
                    } else {
                        // blancas comer DERECHA
                        if (selecti > 1 && piezasTablero[selecti][selectj].isWhite()) {
                            if (piezasTablero[selecti + ajuste][selectj + 1].isBlack() && piezasTablero[selecti + ajuste * 2][selectj + 2] == null) {
                                posibles.add(new Casilla(new Rectangle(), selecti + ajuste * 2, selectj + 2, selecti + ajuste , selectj + 1));
                                peligros.add(new Casilla(new Rectangle(), selecti + ajuste , selectj + 1));
                                System.out.println("COMEMETA DER B");
                            }
                        }
                        // NEGRAS comer DERECHA
                        if (selecti < 6 && piezasTablero[selecti][selectj].isBlack()) {
                            if (piezasTablero[selecti + ajuste][selectj + 1].isWhite() && piezasTablero[selecti + ajuste * 2][selectj + 2] == null) {
                                posibles.add(new Casilla(new Rectangle(), selecti + ajuste * 2, selectj + 2, selecti + ajuste , selectj + 1));
                                peligros.add(new Casilla(new Rectangle(), selecti + ajuste , selectj + 1));
                                System.out.println("COMEMETA DER N");
                            }
                        }
                    }
                }
                if (selectj == 7) {
                    if (piezasTablero[selecti + ajuste][selectj - 1] == null) {
                        posibles.add(new Casilla(new Rectangle(), selecti + ajuste, selectj - 1));
                    } else {
                        // blancas comer IZQUIERDA
                        if (selecti > 1 && piezasTablero[selecti][selectj].isWhite()) {
                            if (piezasTablero[selecti + ajuste][selectj - 1].isBlack() && piezasTablero[selecti + ajuste * 2][selectj - 2] == null) {
                                posibles.add(new Casilla(new Rectangle(), selecti + ajuste * 2, selectj - 2, selecti + ajuste , selectj - 1));
                                peligros.add(new Casilla(new Rectangle(), selecti + ajuste , selectj - 1));
                                System.out.println("COMEMETA IZQ B");
                            }
                        }
                        // negras comer IZQUIERDA
                        if (selecti < 6 && piezasTablero[selecti][selectj].isBlack()) {
                            if (piezasTablero[selecti + ajuste][selectj - 1].isWhite() && piezasTablero[selecti + ajuste * 2][selectj - 2] == null) {
                                posibles.add(new Casilla(new Rectangle(), selecti + ajuste * 2, selectj - 2, selecti + ajuste , selectj - 1));
                                peligros.add(new Casilla(new Rectangle(), selecti + ajuste , selectj - 1));
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
        actualizar();
    }

    public void actualizar(){
        if(contB + contBD == 12) {
            ((Game) Gdx.app.getApplicationListener()).setScreen(new WinScreen("JUGADOR 2"));
            dispose();
        }
        if(contN + contND == 12){
            ((Game) Gdx.app.getApplicationListener()).setScreen(new WinScreen("JUGADOR 1"));
            dispose();
        }
    }

    public void pintar(){
        handleCamera();

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.render();


        batch.begin();


        if(selected)
            batch.draw(ResourceManager.selection,piezasTablero[selecti][selectj].getX(),piezasTablero[selecti][selectj].getY());

        for(Casilla casilla: posibles){
            batch.draw(ResourceManager.posibles, casillasTablero[casilla.getI()][casilla.getJ()].getRect().getX() ,casillasTablero[casilla.getI()][casilla.getJ()].getRect().getY());
        }

        for(Casilla casilla: peligros){
            batch.draw(ResourceManager.danger, casillasTablero[casilla.getI()][casilla.getJ()].getRect().getX() ,casillasTablero[casilla.getI()][casilla.getJ()].getRect().getY());
        }

        batch.end();


        batch2.begin();

        for(int i = 0 ; i < piezasTablero.length; i++)
            for(int j = 0 ; j < piezasTablero[0].length; j++)
                if(piezasTablero[i][j] != null)
                    piezasTablero[i][j].draw(batch2);


        for(int i = 0; i < contB; i++)
            batch2.draw(ResourceManager.blancasTexture, Gdx.graphics.getWidth() - ResourceManager.damabTexture.getWidth() -10 - (60*i),Piece.getScreenCoordinates(casillasTablero[0][0].getRect()).y + ResourceManager.blancasTexture.getHeight() + 10,Gdx.graphics.getWidth()/8,Gdx.graphics.getWidth()/8);

        for(int i = 0; i < contBD; i++)
            batch2.draw(ResourceManager.damabTexture, Gdx.graphics.getWidth() - ResourceManager.damabTexture.getWidth() -10 - (60*i),Piece.getScreenCoordinates(casillasTablero[0][0].getRect()).y + ResourceManager.damabTexture.getHeight()*2 +10,Gdx.graphics.getWidth()/8,Gdx.graphics.getWidth()/8);

        for(int i = 0; i < contN; i++)
            batch2.draw(ResourceManager.negrasTexture, 0 + 10 + (60*i), Piece.getScreenCoordinates(casillasTablero[7][7].getRect()).y - ResourceManager.damabTexture.getHeight() - 10,Gdx.graphics.getWidth()/8,Gdx.graphics.getWidth()/8);

        for(int i = 0; i < contND; i++)
            batch2.draw(ResourceManager.damanTexture, 0 + 10 + (60*i),Piece.getScreenCoordinates(casillasTablero[7][7].getRect()).y - ResourceManager.damabTexture.getHeight()*2 - 10,Gdx.graphics.getWidth()/8,Gdx.graphics.getWidth()/8);

        hud.pintar(batch2);

        batch2.end();

    }

    private void resetSelection(){
       selected = false;
       posibles.clear();
       peligros.clear();
    }

    private void handleCamera() {
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

        Vector2 pulsacion = new Vector2(getMousePosInGameWorld().x, getMousePosInGameWorld().y);

        for(int i = 0 ; i < piezasTablero.length; i++)
            for(int j = 0 ; j < piezasTablero[0].length; j++)
                if(piezasTablero[i][j] != null)
                    if(piezasTablero[i][j].getRect().contains(pulsacion)) {

                        if (!selected || piezasTablero[selecti][selectj].mismoColor(piezasTablero[i][j])) {
                            selecti = i;
                            selectj = j;
                            selected = false;
                        }

                        if(contTurno % 2 == 0 && piezasTablero[selecti][selectj].isWhite()) {
                            selected = true;
                            calcularPosibles();
                        }
                        else if(contTurno % 2 != 0 && piezasTablero[selecti][selectj].isBlack()) {
                            selected = true;
                            calcularPosibles();
                        }
                    }

        for(Casilla casilla: posibles){
            Rectangle rect = new Rectangle(casillasTablero[casilla.getI()][casilla.getJ()].getRect().getX() ,casillasTablero[casilla.getI()][casilla.getJ()].getRect().getY(),64,64);
            if(rect.contains(pulsacion)){


                piezasTablero[casilla.getI()][casilla.getJ()]=piezasTablero[selecti][selectj];
                piezasTablero[selecti][selectj]=null;
                piezasTablero[casilla.getI()][casilla.getJ()].setI(casilla.getI());
                piezasTablero[casilla.getI()][casilla.getJ()].setJ(casilla.getJ());
                piezasTablero[casilla.getI()][casilla.getJ()].setRect(casillasTablero[casilla.getI()][casilla.getJ()].getRect());


                if(casilla.hayAmenaza()){
                    if(piezasTablero[casilla.getAmenazai()][casilla.getAmenazaj()].isWhite())
                        if(piezasTablero[casilla.getAmenazai()][casilla.getAmenazaj()].isDama())
                            contBD++;
                        else
                            contB++;
                    else
                        if(piezasTablero[casilla.getAmenazai()][casilla.getAmenazaj()].isDama())
                            contND++;
                        else
                            contN++;

                    piezasTablero[casilla.getAmenazai()][casilla.getAmenazaj()]=null;


                }else
                    hud.pasarTurno(++contTurno);


                if(piezasTablero[casilla.getI()][casilla.getJ()].isWhite() && piezasTablero[casilla.getI()][casilla.getJ()].getI() == 0)
                    piezasTablero[casilla.getI()][casilla.getJ()].setDama();

                if(piezasTablero[casilla.getI()][casilla.getJ()].isBlack() && piezasTablero[casilla.getI()][casilla.getJ()].getI() == 7)
                    piezasTablero[casilla.getI()][casilla.getJ()].setDama();


                resetSelection();

            }
        }
        System.out.println(selecti +" - "+ selectj);

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
