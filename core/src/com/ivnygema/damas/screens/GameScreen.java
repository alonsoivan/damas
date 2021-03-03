package com.ivnygema.damas.screens;


import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.ivnygema.damas.Aplication;
import com.ivnygema.damas.managers.HUD;
import com.ivnygema.damas.managers.ResourceManager;
import com.ivnygema.damas.models.Casilla;
import com.ivnygema.damas.models.Piece;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisTable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import static com.ivnygema.damas.managers.ResourceManager.*;
import static com.ivnygema.damas.util.Constantes.TILE_WIDTH;

public class GameScreen implements Screen, InputProcessor {

    // TiledMap
    private Batch batch;
    public static OrthographicCamera camera;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    private World world;

    private RayHandler rayHandlerPLS;


    //HUD
    private HUD hud;
    private SpriteBatch batch2 = new SpriteBatch();

    //
    private Piece [][] piezasTablero = new Piece[8][8];
    public static Casilla[][] casillasTablero = new Casilla[8][8];
    private Array<Casilla> posibles;
    private Array<Casilla> peligros;
    private Array<Casilla> movibles;
    private Boolean selected;

    private Array<int[]> hints = new Array<>();


    private int contB = 0;
    private int contBD = 0;
    private int contN = 0;
    private int contND = 0;

    private int contTurno = 0;
    protected int selecti = 0, selectj = 0;

    // Stage

    private Stage stage;
    private ImageButton imageButton;

    private boolean cpuGame;
    private Game game;
    public GameScreen(boolean cpuGame, Game game){
        this.cpuGame = cpuGame;
        this.game = game;
    }

    @Override
    public void show() {
        // Stage
        stage = new Stage();

        if(!VisUI.isLoaded())
            VisUI.load();


        ImageButton.ImageButtonStyle imageButtonStyle =  new ImageButton.ImageButtonStyle();
        imageButtonStyle.imageUp = new TextureRegionDrawable(new TextureRegion(quit));

        imageButton = new ImageButton(imageButtonStyle);
        imageButton.setSize(Gdx.graphics.getWidth()/10,Gdx.graphics.getWidth()/10);
        imageButton.setPosition(Gdx.graphics.getWidth() -Gdx.graphics.getWidth()/10*1.3f,Gdx.graphics.getHeight()-Gdx.graphics.getWidth()/10*1.3f);

        final VisTable visTable = new VisTable(true);
        imageButton.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                quitSound.play(0.3f);
                Dialog dialog = new Dialog("", visTable.getSkin()) {
                    public void result(Object obj) {
                        if((boolean)obj == true){
                            resetSound.play(0.3f);
                            game.setScreen(new MainScreen(game));
                        }
                    }
                };

                TextButton.TextButtonStyle textButtonStyle = visTable.getSkin().get(TextButton.TextButtonStyle.class);
                textButtonStyle.font = titleFont;

                Label.LabelStyle labelStyle = visTable.getSkin().get(Label.LabelStyle.class);
                labelStyle.font = titleFont;

                dialog.pad(50);
                dialog.setMovable(false);
                dialog.text("EXIT?").pad(40);
                dialog.button(" Yes ",true);
                dialog.button(" No ",false);
                dialog.show(stage);
            }
        });

        stage.addActor(imageButton);


        hud = new HUD(cpuGame);

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


        world = new World(new Vector2(0, -10), true);

        //
        posibles = new Array<>();
        peligros = new Array<>();
        movibles = new Array<>();
        selected = false;

        generarBlancas();
        generarNegras();
        obtenerCasillas();

        float porcentajeBannerEnPantalla = Aplication.adService.getBannerHeight()/2 * 100 / Gdx.graphics.getHeight();
        camera.position.y = casillasTablero[3][1].rect.y - camera.viewportHeight*((porcentajeBannerEnPantalla+0.5f)/100);


        rayHandlerPLS = new RayHandler(world);
        rayHandlerPLS.setShadows(false);


        pls = new PointLight[5][2];
        for(int i = 0; i< pls.length ; i++) {
            pls[i][0] = new PointLight(rayHandlerPLS, 200, new Color(1, 0.929f, 0.419f, 1), 85, casillasTablero[3][1].rect.x, casillasTablero[3][1].rect.y);
            pls[i][1] = new PointLight(rayHandlerPLS, 200, new Color(1, 0.929f, 0.419f, 1), 85, casillasTablero[3][1].rect.x, casillasTablero[3][1].rect.y);
            pls[i][0].setActive(false);
            pls[i][1].setActive(false);
        }


        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(stage);
        inputMultiplexer.addProcessor(this);

        Gdx.input.setInputProcessor(inputMultiplexer);
        playerCanMove();
    }
    public PointLight[][] pls;


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

        Array<int[]> amenazas = new Array<>();

        // Diagonal arriba izq
        while(--auxi >= 0 && --auxj >= 0 && !encontrado){
            if(piezasTablero[auxi][auxj] == null) {
                if (!existePosible(auxi, auxj)) {
                    if(hints.isEmpty() || ( !hints.isEmpty() && !amenazas.isEmpty()))
                        posibles.add(new Casilla(new Rectangle(), auxi, auxj, amenazas));
                }
            }else if (piezasTablero[selecti][selectj].mismoColor(piezasTablero[auxi][auxj]))
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
                if (!existePosible(auxi, auxj)) {
                    if(hints.isEmpty() || ( !hints.isEmpty() && !amenazas.isEmpty()))
                        posibles.add(new Casilla(new Rectangle(), auxi, auxj,amenazas));
                }
            }else if (piezasTablero[selecti][selectj].mismoColor(piezasTablero[auxi][auxj]))
                    encontrado = true;
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
                if (!existePosible(auxi, auxj)) {
                    if(hints.isEmpty() || ( !hints.isEmpty() && !amenazas.isEmpty()))
                        posibles.add(new Casilla(new Rectangle(), auxi, auxj,amenazas));
                }
            }else if (piezasTablero[selecti][selectj].mismoColor(piezasTablero[auxi][auxj]))
                    encontrado = true;
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
        while(++auxi <= 7 && ++auxj <= 7 && !encontrado) {
            if (piezasTablero[auxi][auxj] == null) {
                if (!existePosible(auxi, auxj)) {
                    if(hints.isEmpty() || ( !hints.isEmpty() && !amenazas.isEmpty()))
                        posibles.add(new Casilla(new Rectangle(), auxi, auxj, amenazas));
                }
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
                    if(hints.isEmpty() || ( !hints.isEmpty() && !amenazas.isEmpty()))
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
                    if(hints.isEmpty() || ( !hints.isEmpty() && !amenazas.isEmpty()))
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
                        if(hints.isEmpty() || ( !hints.isEmpty() && !amenazas.isEmpty()))
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
                        if(hints.isEmpty() || ( !hints.isEmpty() && !amenazas.isEmpty()))
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
        // Step the physics world.
        world.step(1/60f,6,2);

        pintar(delta);

        actualizar();
    }

    public void actualizar(){
        endGame();

        if(cpuGame && isCpuTurn){
            isCpuTurn = false;
            cpuTurn();
        }
    }

    Timer.Task task = new Timer.Task() {
        @Override
        public void run() {
            movibles.clear();
            if(!hints.isEmpty()){
                selecti = hints.get(0)[0];
                selectj = hints.get(0)[1];
                selected = true;
                calcularPosibles();

                Casilla casilla = posibles.get(MathUtils.random(posibles.size-1));

                moverDirectamente(new int[]{casilla.j,casilla.j},posibles.get(MathUtils.random(posibles.size-1)));

            }else{
                getAllPosiblesMoves();

                Set<int[]> set = moves.keySet();
                Iterator<int[]> iterator = set.iterator();
                int rndPiece = MathUtils.random(set.size()-1);

                for(int i = 0; i < rndPiece-1; i++){
                    iterator.next();
                }

                int[] sel = iterator.next();

                selecti = sel[0];
                selectj = sel[1];
                selected = true;

                int rndPos = MathUtils.random(moves.get(sel).size-1);

                System.out.println("PIEZA A MOVER "+sel[0]+ " " +sel[1]);
                System.out.println("CASILLA A MOVERLA "+moves.get(sel).get(rndPos)[0]+ " " +moves.get(sel).get(rndPos)[1]);

                moverDirectamente(moves.get(sel).get(MathUtils.random(moves.get(sel).size-1)), null);
            }
        }
    };

    boolean isCpuTurn = false;
    public void cpuTurn(){
        Timer.schedule(task,0.75f);
    }

    public void endGame(){
        if(contB + contBD == 12) {
            ((Game) Gdx.app.getApplicationListener()).setScreen(new WinScreen("n",game));
            dispose();
        }
        if(contN + contND == 12){
            ((Game) Gdx.app.getApplicationListener()).setScreen(new WinScreen("b",game));
            dispose();
        }
    }

    public void resetPls(){
        for(int i = 0; i< pls.length ; i++) {
            pls[i][0].setActive(false);
            pls[i][1].setActive(false);
        }
    }


    public void pintar(float dt){
        handleCamera();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.render();

        batch.begin();


        for(Casilla casilla: posibles){
            batch.draw(ResourceManager.posibles, casillasTablero[casilla.getI()][casilla.getJ()].getRect().getX() ,casillasTablero[casilla.getI()][casilla.getJ()].getRect().getY());
        }

        for(Casilla casilla: peligros){
            batch.draw(ResourceManager.danger, casillasTablero[casilla.getI()][casilla.getJ()].getRect().getX() ,casillasTablero[casilla.getI()][casilla.getJ()].getRect().getY());
        }

        for(Casilla casilla: movibles){
            batch.draw(ResourceManager.movibles, casillasTablero[casilla.getI()][casilla.getJ()].getRect().getX() ,casillasTablero[casilla.getI()][casilla.getJ()].getRect().getY());
        }

        if(selected)
            batch.draw(ResourceManager.selection,casillasTablero[selecti][selectj].rect.getX(),casillasTablero[selecti][selectj].rect.getY());


        /*
        for(int i = 0; i<hints.size; i++){
            //batch.draw(ResourceManager.hint, casillasTablero[hints.get(i)[0]][hints.get(i)[1]].getRect().getX() ,casillasTablero[hints.get(i)[0]][hints.get(i)[1]].getRect().getY());

            pls[i][0].setPosition(casillasTablero[hints.get(i)[0]][hints.get(i)[1]].getRect().getX()+casillasTablero[hints.get(i)[0]][hints.get(i)[1]].getRect().getHeight()/2 ,casillasTablero[hints.get(i)[0]][hints.get(i)[1]].getRect().getY()+casillasTablero[hints.get(i)[0]][hints.get(i)[1]].getRect().getHeight()/2);
            pls[i][1].setPosition(casillasTablero[hints.get(i)[0]][hints.get(i)[1]].getRect().getX()+casillasTablero[hints.get(i)[0]][hints.get(i)[1]].getRect().getHeight()/2 ,casillasTablero[hints.get(i)[0]][hints.get(i)[1]].getRect().getY()+casillasTablero[hints.get(i)[0]][hints.get(i)[1]].getRect().getHeight()/2);

            pls[i][0].setActive(true);
            pls[i][1].setActive(true);

        }
        */


        batch.end();

        // RAYHANDLER HINTS
        rayHandlerPLS.setCombinedMatrix(camera);
        rayHandlerPLS.updateAndRender();


        batch2.begin();

        for(int i = 0 ; i < piezasTablero.length; i++)
            for(int j = 0 ; j < piezasTablero[0].length; j++)
                if(piezasTablero[i][j] != null)
                    piezasTablero[i][j].draw(batch2,dt);

        hud.pintar1(batch2);

        for(int i = 0; i < contBD; i++)
            batch2.draw(ResourceManager.damabTexture, Gdx.graphics.getWidth() - Gdx.graphics.getWidth()/8 - (ajusteComidas*i),Piece.getScreenCoordinates(casillasTablero[0][0].getRect()).y + Gdx.graphics.getWidth()/8,Gdx.graphics.getWidth()/8,Gdx.graphics.getWidth()/8);

        for(int i = 0; i < contB; i++)
            batch2.draw(blancasTexture,  (ajusteComidas*i),Piece.getScreenCoordinates(casillasTablero[0][0].getRect()).y + Gdx.graphics.getWidth()/8,Gdx.graphics.getWidth()/8,Gdx.graphics.getWidth()/8);

        for(int i = 0; i < contN; i++)
            batch2.draw(ResourceManager.negrasTexture, (ajusteComidas*i), Piece.getScreenCoordinates(casillasTablero[7][7].getRect()).y - Gdx.graphics.getWidth()/8 ,Gdx.graphics.getWidth()/8,Gdx.graphics.getWidth()/8);

        for(int i = 0; i < contND; i++)
            batch2.draw(ResourceManager.damanTexture,Gdx.graphics.getWidth() - Gdx.graphics.getWidth()/8 - (ajusteComidas*i),Piece.getScreenCoordinates(casillasTablero[7][7].getRect()).y - Gdx.graphics.getWidth()/8 ,Gdx.graphics.getWidth()/8,Gdx.graphics.getWidth()/8);

        batch2.end();


        batch2.begin();

        hud.pintar2(batch2);

        batch2.end();

        stage.act(dt);
        stage.draw();
    }
    float ajusteComidas = Gdx.graphics.getWidth()*0.06f;

    private void resetSelection(){
       selected = false;
       posibles.clear();
       peligros.clear();
    }

    private void handleCamera() {

        float porcentajeBannerEnPantalla = Aplication.adService.getBannerHeight()/2 * 100 / Gdx.graphics.getHeight();
        camera.position.y = casillasTablero[3][1].rect.y - camera.viewportHeight*((porcentajeBannerEnPantalla+0.5f)/100);

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
        world.dispose();
        rayHandlerPLS.dispose();
        VisUI.dispose(true);
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

        if(cpuGame && contTurno%2 != 0)
            return false;

        Vector2 pulsacion = new Vector2(getMousePosInGameWorld().x, getMousePosInGameWorld().y);

        // seleccionar pieza
        for(int i = 0 ; i < piezasTablero.length; i++)
            for(int j = 0 ; j < piezasTablero[0].length; j++)
                if(piezasTablero[i][j] != null)
                    if(piezasTablero[i][j].getRect().contains(pulsacion)) {

                        if(contTurno % 2 == 0 && piezasTablero[i][j].isWhite() || contTurno % 2 != 0 && piezasTablero[i][j].isBlack()) {

                            if(hints.isEmpty() || isHinted(i,j)){
                                selecti = i;
                                selectj = j;

                                selectionSound.play(0.7f);
                                selected = true;
                                calcularPosibles();
                            }
                        }
                    }

        mover(pulsacion,false);

        return false;
    }

    public void moverDirectamente(int[] ij, Casilla casilla){
        boolean pasarTurno = true;

        int i = ij[0];
        int j = ij[1];

        if(casilla == null)
            casilla = new Casilla(new Rectangle(),i,j);

        hints.clear();
        resetPls();

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

            if(piezasTablero[casilla.getI()][casilla.getJ()].isWhite() && piezasTablero[casilla.getI()][casilla.getJ()].getI() == 0 ||
                    (piezasTablero[casilla.getI()][casilla.getJ()].isBlack() && piezasTablero[casilla.getI()][casilla.getJ()].getI() == 7))
                piezasTablero[casilla.getI()][casilla.getJ()].setDama();

            calcularPosibles();
            if(peligros.size>0){
                playerCanMove();
                resetSelection();

                pasarTurno = false;
            }

        }

        if(piezasTablero[casilla.getI()][casilla.getJ()].isWhite() && piezasTablero[casilla.getI()][casilla.getJ()].getI() == 0 ||
                (piezasTablero[casilla.getI()][casilla.getJ()].isBlack() && piezasTablero[casilla.getI()][casilla.getJ()].getI() == 7))
            piezasTablero[casilla.getI()][casilla.getJ()].setDama();

        if(pasarTurno) {
            hud.pasarTurno(++contTurno);

            playerCanMove();

            resetSelection();
        }

        if(contTurno%2 != 0)
            isCpuTurn = true;
    }

    public void mover(Vector2 pulsacion, boolean firstOne){
        boolean pasarTurno = true;
        for(final Casilla casilla: posibles){
            Rectangle rect = new Rectangle(casillasTablero[casilla.getI()][casilla.getJ()].getRect().getX() ,casillasTablero[casilla.getI()][casilla.getJ()].getRect().getY(),64,64);
            if(rect.contains(pulsacion) || firstOne){

                hints.clear();
                resetPls();

                System.out.println(selecti + " " + selectj);

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

                    if(piezasTablero[casilla.getI()][casilla.getJ()].isWhite() && piezasTablero[casilla.getI()][casilla.getJ()].getI() == 0 ||
                            (piezasTablero[casilla.getI()][casilla.getJ()].isBlack() && piezasTablero[casilla.getI()][casilla.getJ()].getI() == 7))
                        piezasTablero[casilla.getI()][casilla.getJ()].setDama();


                    calcularPosibles();
                    if(peligros.size>0){
                        playerCanMove();
                        resetSelection();

                        pasarTurno = false;

                        //
                        movibles.clear();
                        selecti = casilla.getI();
                        selectj = casilla.getJ();
                        System.out.println(selecti + " " + selectj);
                        calcularPosibles();
                        selected = true;
                    }

                }

                if(piezasTablero[casilla.getI()][casilla.getJ()].isWhite() && piezasTablero[casilla.getI()][casilla.getJ()].getI() == 0 ||
                        (piezasTablero[casilla.getI()][casilla.getJ()].isBlack() && piezasTablero[casilla.getI()][casilla.getJ()].getI() == 7))
                    piezasTablero[casilla.getI()][casilla.getJ()].setDama();


                if(pasarTurno) {
                    hud.pasarTurno(++contTurno);

                    movibles.clear();
                    playerCanMove();

                    resetSelection();
                }

                if(contTurno%2 != 0)
                    isCpuTurn = true;

            }
        }

    }

    HashMap<int[],Array<int[]>> moves = new HashMap<>();
    public void getAllPosiblesMoves(){
        int i = 0, j = 0;

        moves.clear();

        while ( i < piezasTablero.length ){
            j = 0;
            while (j < piezasTablero[i].length ){
                if(piezasTablero[i][j] != null){
                    if (piezasTablero[i][j].getPieceColor().equals("n")) {
                        selecti = i;
                        selectj = j;

                        calcularPosibles();

                        if(!posibles.isEmpty()){
                            Array<int[]> auxPosibles = new Array<>();
                            for (Casilla casilla : posibles)
                                auxPosibles.add(new int[]{casilla.i,casilla.j});

                            moves.put(new int[]{i,j},auxPosibles);
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
                            movibles.add(new Casilla(new Rectangle(),i,j));
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
            ((Game) Gdx.app.getApplicationListener()).setScreen(new WinScreen(contTurno % 2 == 0 ? "n" : "b",game));
            dispose();
        }

        if (!hints.isEmpty()){
            movibles.clear();
            for(int[] hint : hints)
                movibles.add(new Casilla(new Rectangle(),hint[0],hint[1]));
        }
    }

    public static Vector3 getMousePosInGameWorld() {
        return camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
    }

    public static Vector3 getCoorsInGameWorld(float x, float y) {
        return camera.unproject(new Vector3(x,y, 0));
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
