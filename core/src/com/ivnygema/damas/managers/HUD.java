package com.ivnygema.damas.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.ivnygema.damas.models.Piece;

import static com.ivnygema.damas.screens.GameScreen.casillasTablero;

public class HUD {

    private BitmapFont jugador2;
    private BitmapFont jugador1;

    GlyphLayout layout;

    private FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/LemonMilk.otf"));
    private FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

    public HUD(){
        parameter.size = 80;
        parameter.color = Color.WHITE;

        jugador2 = generator.generateFont(parameter);
        jugador2.setUseIntegerPositions(false);

        jugador1 = generator.generateFont(parameter);
        jugador1.setUseIntegerPositions(false);

        layout = new GlyphLayout();
        layout.setText(jugador2, "Jugador 2");

        jugador1.setColor(Color.RED);
    }

    public void pintar(SpriteBatch batch){

        jugador2.draw(batch, "Jugador 2", 20 , (Gdx.graphics.getHeight() + Piece.getScreenCoordinates(casillasTablero[0][0].getRect()).y + ResourceManager.damabTexture.getHeight()) / 2);

        jugador1.draw(batch, "Jugador 1", Gdx.graphics.getWidth() - layout.width -20  , Piece.getScreenCoordinates(casillasTablero[7][7].getRect()).y / 2);

    }

    public void pasarTurno(int contTurno){
        if(contTurno %2 == 0) {
            jugador1.setColor(Color.RED);
            jugador2.setColor(Color.WHITE);
        }else{
            jugador1.setColor(Color.WHITE);
            jugador2.setColor(Color.BLUE);
        }
    }

}
