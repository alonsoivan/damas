package com.ivnygema.damas.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.ivnygema.damas.Aplication;
import com.ivnygema.damas.models.Piece;

import static com.ivnygema.damas.screens.GameScreen.casillasTablero;

public class HUD {

    private BitmapFont jugador2;
    private BitmapFont jugador1;

    private BitmapFont jugador2Turn;
    private BitmapFont jugador1Turn;

    GlyphLayout layout;

    private FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/LemonMilk.otf"));
    private FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

    private String player2;
    public HUD(boolean cpuGame){
        parameter.size = (int)(Gdx.graphics.getWidth()*0.07);
        parameter.borderWidth = (int)(Gdx.graphics.getWidth()*0.01);
        parameter.color = Color.WHITE;

        jugador2 = generator.generateFont(parameter);
        jugador2.setUseIntegerPositions(false);

        jugador1 = generator.generateFont(parameter);
        jugador1.setUseIntegerPositions(false);


        parameter.size = (int)(Gdx.graphics.getWidth()*0.07);
        parameter.borderWidth = (int)(Gdx.graphics.getWidth()*0.011);
        parameter.borderColor = Color.YELLOW;
        parameter.color = Color.WHITE;

        jugador2Turn = generator.generateFont(parameter);
        jugador2Turn.setUseIntegerPositions(false);

        jugador1Turn = generator.generateFont(parameter);
        jugador1Turn.setUseIntegerPositions(false);

        layout = new GlyphLayout();
        layout.setText(jugador1, "Jugador 1");

        if(cpuGame)
            player2 = "CPU";
        else
            player2 = "Jugador 2";
    }


    public void pintar1(SpriteBatch batch) {
        float bgHeight = Piece.getScreenCoordinates(casillasTablero[7][7].getRect()).y - Aplication.adService.getBannerHeight() - (Gdx.graphics.getWidth() / 8) * 1.05f;

        // fondo nombres
        //batch.draw(namebg, 0, Gdx.graphics.getHeight() - bgHeight, bgWidth, bgHeight);
        //batch.draw(namebg, Gdx.graphics.getWidth() - bgWidth, Aplication.adService.getBannerHeight(), bgWidth, bgHeight);
    }


        int contTurno;
    float bgWidth = Gdx.graphics.getWidth()*0.8f;
    public void pintar2(SpriteBatch batch){
        float bgHeight = Piece.getScreenCoordinates(casillasTablero[7][7].getRect()).y - Aplication.adService.getBannerHeight() - (Gdx.graphics.getWidth()/8)*1.05f;


        //batch.draw(namebg, 0, Gdx.graphics.getHeight() - bgHeight,bgWidth,bgHeight);
        //batch.draw(namebg,Gdx.graphics.getWidth() - bgWidth, Aplication.adService.getBannerHeight(),bgWidth,bgHeight);


        if(contTurno %2 == 0) {
            //layout.setText(jugador1Turn, "Jugador 1");
            //jugador1Turn.draw(batch, "Jugador 1", Gdx.graphics.getWidth() / 2 - layout.width / 2, Aplication.adService.getBannerHeight()  + bgHeight/2 + layout.height/2);


            layout.setText(jugador1,"Jugador 1");
            //batch.draw(turnSelect, Gdx.graphics.getWidth()/2 - layout.width/1.25f , Aplication.adService.getBannerHeight() + layout.height , layout.width*1.65f, layout.height*2.95f);
            //jugador1.draw(batch, "Jugador 1", Gdx.graphics.getWidth()/2 - layout.width/2 , Aplication.adService.getBannerHeight() + bgHeight/2 + layout.height/2);


            layout.setText(jugador2, player2);
            //jugador2.draw(batch, player2, Gdx.graphics.getWidth() / 2 - layout.width / 2, Gdx.graphics.getHeight() - bgHeight/2 + layout.height/2);

        }else {
            //layout.setText(jugador2Turn, player2);
            //jugador2Turn.draw(batch, player2,Gdx.graphics.getWidth() / 2 - layout.width / 2, Gdx.graphics.getHeight() - bgHeight/2 + layout.height/2);


            layout.setText(jugador2, player2);
            //batch.draw(turnSelect, Gdx.graphics.getWidth()/2 - layout.width/1.25f , Gdx.graphics.getHeight() - bgHeight/1.25f , layout.width*1.65f, layout.height*2.95f);
            //jugador2.draw(batch, player2, Gdx.graphics.getWidth() / 2 - layout.width / 2, Gdx.graphics.getHeight() - bgHeight/2 + layout.height/2);

            layout.setText(jugador1,"Jugador 1");
            //jugador1.draw(batch, "Jugador 1", Gdx.graphics.getWidth()/2 - layout.width/2 , Aplication.adService.getBannerHeight() + bgHeight/2 + layout.height/2);
        }

    }

    public void pasarTurno(int contTurno){
        this.contTurno = contTurno;
        /*
        if(contTurno %2 == 0) {
            jugador1.setColor(Color.RED);
            jugador2.setColor(Color.WHITE);
        }else{
            jugador1.setColor(Color.WHITE);
            jugador2.setColor(Color.BLUE);
        }

         */
    }

}
