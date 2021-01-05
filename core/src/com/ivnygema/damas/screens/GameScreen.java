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

import static com.ivnygema.damas.managers.ResourceManager.*;
import static com.ivnygema.damas.util.Constantes.TILE_WIDTH;

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
    boolean selected;

    boolean comer;

    Array<int[]> hints = new Array<>();


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
        //camera.setToOrtho(false, TILES_IN_CAMERA_WIDTH * TILE_WIDTH, TILES_IN_CAMERA_HEIGHT * TILE_WIDTH);

        float width = TILE_WIDTH * 8;
        float height = width * Gdx.graphics.getHeight() / Gdx.graphics.getWidth();
        camera.setToOrtho(false, width   , height);
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
        camera.position.y = casillasTablero[3][1].rect.y;
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

    private void calcularPosibles(boolean comer ) {
        this.comer = comer;
        posibles.clear();
        peligros.clear();

        if(piezasTablero[selecti][selectj].isDama())
<<<<<<< HEAD
            movimientoDama();
        else
            movimientoNormal();
=======
            movimientoDama(comer);
        else
            movimientoNormal(comer);

>>>>>>> 528c675a19bfb20be7cd9028e52be827e5859f7d
    }

    public void movimientoDama(boolean comer) {
        int auxi = selecti;
        int auxj = selectj;
        boolean encontrado = false;

        Array<int[]> amenazas = new Array<>();

        // Diagonal arriba izq
        while(--auxi >= 0 && --auxj >= 0 && !encontrado){
            if(piezasTablero[auxi][auxj] == null) {
<<<<<<< HEAD
                if (!existePosible(auxi, auxj)) {
                    if(hints.isEmpty() || ( !hints.isEmpty() && !amenazas.isEmpty()))
                        posibles.add(new Casilla(new Rectangle(), auxi, auxj, amenazas));
                }
            }else if (piezasTablero[selecti][selectj].mismoColor(piezasTablero[auxi][auxj]))
=======
                if (!comer)
                    posibles.add(new Casilla(new Rectangle(), auxi, auxj));
            }else
                if (piezasTablero[selecti][selectj].mismoColor(piezasTablero[auxi][auxj]))
>>>>>>> 528c675a19bfb20be7cd9028e52be827e5859f7d
                    encontrado = true;
            else{
                    if(auxi -1 >= 0 && auxj -1 >= 0)
                        if(piezasTablero[auxi -1][auxj -1] == null){
                            peligros.add(new Casilla(new Rectangle(), auxi, auxj));
                            amenazas.add(new int[]{auxi,auxj});
                            posibles.add(new Casilla(new Rectangle(), auxi -1, auxj -1, amenazas));
                        }else
                            encontrado = true;

            }
        }

        amenazas.clear();
        auxi = selecti;
        auxj = selectj;
        encontrado = false;
        // Diagonal arriba der
        while(--auxi >= 0 && ++auxj <= 7 && !encontrado){
            if(piezasTablero[auxi][auxj] == null) {
<<<<<<< HEAD
                if (!existePosible(auxi, auxj)) {
                    if(hints.isEmpty() || ( !hints.isEmpty() && !amenazas.isEmpty()))
                        posibles.add(new Casilla(new Rectangle(), auxi, auxj,amenazas));
                }
            }else if (piezasTablero[selecti][selectj].mismoColor(piezasTablero[auxi][auxj]))
                    encontrado = true;
=======
                if (!comer)
                    posibles.add(new Casilla(new Rectangle(), auxi, auxj));
            }else if (piezasTablero[selecti][selectj].mismoColor(piezasTablero[auxi][auxj]))
                encontrado = true;
>>>>>>> 528c675a19bfb20be7cd9028e52be827e5859f7d
            else{
                if(auxi -1 >= 0 && auxj +1 <= 7)
                    if(piezasTablero[auxi -1][auxj +1] == null){
                        peligros.add(new Casilla(new Rectangle(), auxi, auxj));
                        amenazas.add(new int[]{auxi,auxj});
                        posibles.add(new Casilla(new Rectangle(), auxi -1, auxj +1,amenazas));
                    }else
                        encontrado = true;
            }
        }

        amenazas.clear();
        auxi = selecti;
        auxj = selectj;
        encontrado = false;
        // Diagonal abajo izq
        while(++auxi <= 7 && --auxj >= 0 && !encontrado){
            if(piezasTablero[auxi][auxj] == null) {
<<<<<<< HEAD
                if (!existePosible(auxi, auxj)) {
                    if(hints.isEmpty() || ( !hints.isEmpty() && !amenazas.isEmpty()))
                        posibles.add(new Casilla(new Rectangle(), auxi, auxj,amenazas));
                }
            }else if (piezasTablero[selecti][selectj].mismoColor(piezasTablero[auxi][auxj]))
                    encontrado = true;
=======
                if (!comer)
                    posibles.add(new Casilla(new Rectangle(), auxi, auxj));
            }else if (piezasTablero[selecti][selectj].mismoColor(piezasTablero[auxi][auxj]))
                encontrado = true;
>>>>>>> 528c675a19bfb20be7cd9028e52be827e5859f7d
            else{
                if(auxi +1 <=7 && auxj -1 >=0)
                    if(piezasTablero[auxi +1][auxj -1] == null){
                        peligros.add(new Casilla(new Rectangle(), auxi, auxj));
                        amenazas.add(new int[]{auxi,auxj});
                        posibles.add(new Casilla(new Rectangle(), auxi +1, auxj -1, amenazas));
                    }else
                        encontrado = true;
            }
        }

        amenazas.clear();
        auxi = selecti;
        auxj = selectj;
        encontrado = false;
        // Diagonal abajo der
<<<<<<< HEAD
        while(++auxi <= 7 && ++auxj <= 7 && !encontrado) {
            if (piezasTablero[auxi][auxj] == null) {
                if (!existePosible(auxi, auxj)) {
                    if(hints.isEmpty() || ( !hints.isEmpty() && !amenazas.isEmpty()))
                        posibles.add(new Casilla(new Rectangle(), auxi, auxj, amenazas));
                }
=======
        while(++auxi <= 7 && ++auxj <= 7 && !encontrado){
            if(piezasTablero[auxi][auxj] == null) {
                if (!comer)
                    posibles.add(new Casilla(new Rectangle(), auxi, auxj));
>>>>>>> 528c675a19bfb20be7cd9028e52be827e5859f7d
            }else if (piezasTablero[selecti][selectj].mismoColor(piezasTablero[auxi][auxj]))
                encontrado = true;
            else{
                if(auxi +1 <=7 && auxj +1 <=7)
                    if(piezasTablero[auxi +1][auxj +1] == null){
                        peligros.add(new Casilla(new Rectangle(), auxi, auxj));
                        amenazas.add(new int[]{auxi,auxj});
                        posibles.add(new Casilla(new Rectangle(), auxi +1, auxj +1, amenazas));
                    }else
                        encontrado = true;
            }
        }
    }

<<<<<<< HEAD
    public boolean existePosible(int i, int j){
        boolean existe = false;
        int iterator = 0;
        while (iterator < posibles.size && !existe){
            if(posibles.get(iterator).i == i && posibles.get(iterator).j == j)
                existe = true;
            iterator++;
        }
        return existe;
    }

    public void movimientoNormal(){
=======
    public void movimientoNormal(boolean comer){
>>>>>>> 528c675a19bfb20be7cd9028e52be827e5859f7d
        int ajuste = 0;

        Array<int[]> amenazas = new Array<>();

        if(piezasTablero[selecti][selectj].isBlack())
            ajuste = 1;
        else
            ajuste = -1;

        if((piezasTablero[selecti][selectj].isWhite() && selecti == 0) || (piezasTablero[selecti][selectj].isBlack() && selecti == 7)){
            System.out.println("no puedes mover marinero");
        }else {
            if (selectj > 0 && selectj < 7) {
                if (piezasTablero[selecti + ajuste][selectj - 1] == null) {
<<<<<<< HEAD
                    if(hints.isEmpty() || ( !hints.isEmpty() && !amenazas.isEmpty()))
=======
                    if(!comer)
>>>>>>> 528c675a19bfb20be7cd9028e52be827e5859f7d
                        posibles.add(new Casilla(new Rectangle(), selecti + ajuste, selectj - 1));
                } else {
                    // blancas comer IZQUIERDA
                    if (selecti > 1 && selectj > 1 && piezasTablero[selecti][selectj].isWhite()) {
                        if (piezasTablero[selecti + ajuste][selectj - 1].isBlack() && piezasTablero[selecti + ajuste * 2][selectj - 2] == null) {
                            amenazas.add(new int[]{selecti + ajuste, selectj - 1});
                            posibles.add(new Casilla(new Rectangle(), selecti + ajuste * 2, selectj - 2, amenazas));
                            peligros.add(new Casilla(new Rectangle(), selecti + ajuste, selectj - 1));
                        }
                    }
                    amenazas.clear();
                    // NEGRAS comer IZQUIERDA
                    if (selecti < 6 && selectj > 1 && piezasTablero[selecti][selectj].isBlack()) {
                        if (piezasTablero[selecti + ajuste][selectj - 1].isWhite() && piezasTablero[selecti + ajuste * 2][selectj - 2] == null) {
                            amenazas.add(new int[]{ selecti + ajuste, selectj - 1});
                            posibles.add(new Casilla(new Rectangle(), selecti + ajuste * 2, selectj - 2,amenazas));
                            peligros.add(new Casilla(new Rectangle(), selecti + ajuste, selectj - 1));
                        }
                    }
                }
                amenazas.clear();
                if (piezasTablero[selecti + ajuste][selectj + 1] == null) {
<<<<<<< HEAD
                    if(hints.isEmpty() || ( !hints.isEmpty() && !amenazas.isEmpty()))
=======
                    if(!comer)
>>>>>>> 528c675a19bfb20be7cd9028e52be827e5859f7d
                        posibles.add(new Casilla(new Rectangle(), selecti + ajuste, selectj + 1));
                } else {
                    // blancas comer DERECHA
                    if (selecti > 1 && selectj < 6 && piezasTablero[selecti][selectj].isWhite()) {
                        if (piezasTablero[selecti + ajuste][selectj + 1].isBlack() && piezasTablero[selecti + ajuste * 2][selectj + 2] == null) {
                            amenazas.add(new int[]{selecti + ajuste , selectj + 1});
                            posibles.add(new Casilla(new Rectangle(), selecti + ajuste * 2, selectj + 2, amenazas));
                            peligros.add(new Casilla(new Rectangle(), selecti + ajuste , selectj + 1));
                        }
                    }
                    amenazas.clear();
                    // negras comer DERECHA
                    if (selecti < 6 && selectj < 6 && piezasTablero[selecti][selectj].isBlack()) {
                        if (piezasTablero[selecti + ajuste][selectj + 1].isWhite() && piezasTablero[selecti + ajuste * 2][selectj + 2] == null) {
                            amenazas.add(new int[]{selecti + ajuste, selectj + 1});
                            posibles.add(new Casilla(new Rectangle(), selecti + ajuste * 2, selectj + 2, amenazas));
                            peligros.add(new Casilla(new Rectangle(), selecti + ajuste, selectj + 1));
                        }
                    }
                }
            } else {
                if (selectj == 0) {
                    if (piezasTablero[selecti + ajuste][selectj + 1] == null) {
<<<<<<< HEAD
                        if(hints.isEmpty() || ( !hints.isEmpty() && !amenazas.isEmpty()))
=======
                        if(!comer)
>>>>>>> 528c675a19bfb20be7cd9028e52be827e5859f7d
                            posibles.add(new Casilla(new Rectangle(), selecti + ajuste, selectj + 1));
                    } else {
                        amenazas.clear();
                        // blancas comer DERECHA
                        if (selecti > 1 && piezasTablero[selecti][selectj].isWhite()) {
                            if (piezasTablero[selecti + ajuste][selectj + 1].isBlack() && piezasTablero[selecti + ajuste * 2][selectj + 2] == null) {
                                amenazas.add(new int[]{selecti + ajuste , selectj + 1});
                                posibles.add(new Casilla(new Rectangle(), selecti + ajuste * 2, selectj + 2, amenazas));
                                peligros.add(new Casilla(new Rectangle(), selecti + ajuste , selectj + 1));
                            }
                        }
                        amenazas.clear();
                        // NEGRAS comer DERECHA
                        if (selecti < 6 && piezasTablero[selecti][selectj].isBlack()) {
                            if (piezasTablero[selecti + ajuste][selectj + 1].isWhite() && piezasTablero[selecti + ajuste * 2][selectj + 2] == null) {
                                amenazas.add(new int[]{selecti + ajuste , selectj + 1});
                                posibles.add(new Casilla(new Rectangle(), selecti + ajuste * 2, selectj + 2, amenazas));
                                peligros.add(new Casilla(new Rectangle(), selecti + ajuste , selectj + 1));
                            }
                        }
                    }
                }
                if (selectj == 7) {
                    if (piezasTablero[selecti + ajuste][selectj - 1] == null) {
<<<<<<< HEAD
                        if(hints.isEmpty() || ( !hints.isEmpty() && !amenazas.isEmpty()))
=======
                        if(!comer)
>>>>>>> 528c675a19bfb20be7cd9028e52be827e5859f7d
                            posibles.add(new Casilla(new Rectangle(), selecti + ajuste, selectj - 1));
                    } else {
                        amenazas.clear();
                        // blancas comer IZQUIERDA
                        if (selecti > 1 && piezasTablero[selecti][selectj].isWhite()) {
                            if (piezasTablero[selecti + ajuste][selectj - 1].isBlack() && piezasTablero[selecti + ajuste * 2][selectj - 2] == null) {
                                amenazas.add(new int[]{selecti + ajuste , selectj - 1});
                                posibles.add(new Casilla(new Rectangle(), selecti + ajuste * 2, selectj - 2, amenazas));
                                peligros.add(new Casilla(new Rectangle(), selecti + ajuste , selectj - 1));
                            }
                        }
                        amenazas.clear();
                        // negras comer IZQUIERDA
                        if (selecti < 6 && piezasTablero[selecti][selectj].isBlack()) {
                            if (piezasTablero[selecti + ajuste][selectj - 1].isWhite() && piezasTablero[selecti + ajuste * 2][selectj - 2] == null) {
                                amenazas.add(new int[]{selecti + ajuste , selectj - 1});
                                posibles.add(new Casilla(new Rectangle(), selecti + ajuste * 2, selectj - 2, amenazas));
                                peligros.add(new Casilla(new Rectangle(), selecti + ajuste , selectj - 1));
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
            batch.draw(ResourceManager.selection,casillasTablero[selecti][selectj].rect.getX(),casillasTablero[selecti][selectj].rect.getY());

        for(Casilla casilla: posibles){
            batch.draw(ResourceManager.posibles, casillasTablero[casilla.getI()][casilla.getJ()].getRect().getX() ,casillasTablero[casilla.getI()][casilla.getJ()].getRect().getY());
        }

        for(Casilla casilla: peligros){
            batch.draw(ResourceManager.danger, casillasTablero[casilla.getI()][casilla.getJ()].getRect().getX() ,casillasTablero[casilla.getI()][casilla.getJ()].getRect().getY());
        }

        for(int i = 0; i<hints.size; i++){
            batch.draw(ResourceManager.hint, casillasTablero[hints.get(i)[0]][hints.get(i)[1]].getRect().getX() ,casillasTablero[hints.get(i)[0]][hints.get(i)[1]].getRect().getY());
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

    public boolean isHinted(int i, int j){
        for(int iterator = 0; iterator<hints.size;iterator++){
            if(hints.get(iterator)[0] == i && hints.get(iterator)[1] == j)
                return true;
        }
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        Vector2 pulsacion = new Vector2(getMousePosInGameWorld().x, getMousePosInGameWorld().y);

        boolean pasarTurno = true;

        // seleccionar pieza
        for(int i = 0 ; i < piezasTablero.length; i++)
            for(int j = 0 ; j < piezasTablero[0].length; j++)
                if(piezasTablero[i][j] != null)
                    if(piezasTablero[i][j].getRect().contains(pulsacion)) {

<<<<<<< HEAD
                        if(contTurno % 2 == 0 && piezasTablero[i][j].isWhite() || contTurno % 2 != 0 && piezasTablero[i][j].isBlack()) {

                            if(hints.isEmpty() || isHinted(i,j)){
                                selecti = i;
                                selectj = j;

                                selectionSound.play(0.7f);
                                selected = true;
                                calcularPosibles();
=======
                        if(!comer) {
                            if (!selected || piezasTablero[selecti][selectj].mismoColor(piezasTablero[i][j])) {
                                selecti = i;
                                selectj = j;
                                selected = false;
                            }


                            if (contTurno % 2 == 0 && piezasTablero[selecti][selectj].isWhite() || contTurno % 2 != 0 && piezasTablero[selecti][selectj].isBlack()) {
                                selectionSound.play(0.7f);
                                selected = true;
                                calcularPosibles(false);
>>>>>>> 528c675a19bfb20be7cd9028e52be827e5859f7d
                            }
                        }
                    }

        for(final Casilla casilla: posibles){
            Rectangle rect = new Rectangle(casillasTablero[casilla.getI()][casilla.getJ()].getRect().getX() ,casillasTablero[casilla.getI()][casilla.getJ()].getRect().getY(),64,64);
            if(rect.contains(pulsacion)){

                hints.clear();

                piezasTablero[casilla.getI()][casilla.getJ()]=piezasTablero[selecti][selectj];
                piezasTablero[selecti][selectj]=null;
                piezasTablero[casilla.getI()][casilla.getJ()].setI(casilla.getI());
                piezasTablero[casilla.getI()][casilla.getJ()].setJ(casilla.getJ());
                piezasTablero[casilla.getI()][casilla.getJ()].setRect(casillasTablero[casilla.getI()][casilla.getJ()].getRect());

                if(casilla.hayAmenaza()) {
                    for (int[] amenaza : casilla.getAmenazas()){
                        if (piezasTablero[amenaza[0]][amenaza[1]].isWhite())
                            if (piezasTablero[amenaza[0]][amenaza[1]].isDama())
                                contBD++;
                            else
                                contB++;
                        else if (piezasTablero[amenaza[0]][amenaza[1]].isDama())
                            contND++;
                        else
                            contN++;
                    }

                    // borrar pieza/s
                    for (int[] amenaza : casilla.getAmenazas())
                        piezasTablero[amenaza[0]][amenaza[1]] = null;


                    comerSound.play(0.5f);

                    selecti = casilla.getI();
                    selectj = casilla.getJ();
<<<<<<< HEAD

                    if(piezasTablero[casilla.getI()][casilla.getJ()].isWhite() && piezasTablero[casilla.getI()][casilla.getJ()].getI() == 0 ||
                            (piezasTablero[casilla.getI()][casilla.getJ()].isBlack() && piezasTablero[casilla.getI()][casilla.getJ()].getI() == 7))
                        piezasTablero[casilla.getI()][casilla.getJ()].setDama();

                    calcularPosibles();
                    if(peligros.size>0){
                        selected = true;
                        pasarTurno = false;
                    }

=======
                    calcularPosibles(true);
                    if(peligros.size>0){
                        selected = true;
                        pasarTurno = false;
                    }else
                        comer = false;
>>>>>>> 528c675a19bfb20be7cd9028e52be827e5859f7d
                }

                if(piezasTablero[casilla.getI()][casilla.getJ()].isWhite() && piezasTablero[casilla.getI()][casilla.getJ()].getI() == 0 ||
                        (piezasTablero[casilla.getI()][casilla.getJ()].isBlack() && piezasTablero[casilla.getI()][casilla.getJ()].getI() == 7))
                    piezasTablero[casilla.getI()][casilla.getJ()].setDama();

                if(pasarTurno) {
                    hud.pasarTurno(++contTurno);

                    playerCanMove();

                    resetSelection();
                }

            }
        }
        return false;
    }

    public void playerCanMove(){
        boolean hasMoves = false;
        int i = 0, j = 0;

        String color = "n";
        if(contTurno %2 == 0) {
            color = "b";
        }

        while ( i < piezasTablero.length){
            j = 0;
            while (j < piezasTablero[i].length){
                if(piezasTablero[i][j] != null){
                    if (piezasTablero[i][j].getPieceColor().equals(color)) {
                        selecti = i;
                        selectj = j;

                        calcularPosibles();

                        if(!posibles.isEmpty()){
                            hasMoves = true;
                        }

                        if (!peligros.isEmpty()){
                            hints.add(new int[]{selecti,selectj});
                        }

                    }
                }
                j++;
            }
            i++;
        }

        if (!hasMoves){
            ((Game) Gdx.app.getApplicationListener()).setScreen(new WinScreen("JUGADOR 1"));
            dispose();
        }
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
