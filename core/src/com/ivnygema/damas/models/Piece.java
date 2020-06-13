package com.ivnygema.damas.models;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import static com.ivnygema.damas.managers.ResourceManager.blancasTexture;
import static com.ivnygema.damas.managers.ResourceManager.negrasTexture;

public class Piece extends Sprite {

    private boolean dama;
    private Rectangle rect;
    private String color;
    private int i = 0;
    private int j = 0;

    public Piece(Vector2 pos, String color, int i, int j){
        super(negrasTexture);
        super.setPosition(pos.x,pos.y);
        rect = new Rectangle(pos.x,pos.y, getWidth(),getHeight());
        this.color = color;

        if(color.equals("b")){
            super.setTexture(blancasTexture);
        }

        this.i = i;
        this.j = j;

    }

    public void draw(SpriteBatch batch) {
        super.draw(batch);
    }

    public boolean isDama() {
        return dama;
    }

    public Rectangle getRect() {
        return rect;
    }

    public int getI() {
        return i;
    }

    public int getJ() {
        return j;
    }

    public void setDama(boolean dama) {
        this.dama = dama;
    }

    public void setRect(Rectangle rect) {
        this.rect = rect;
        super.setPosition(rect.x,rect.y);
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setI(int i) {
        this.i = i;
    }

    public void setJ(int j) {
        this.j = j;
    }

    public boolean isBlack(){
        if (color.equals("b"))
            return false;
        return true;
    }

    public boolean isWhite(){
        if (color.equals("b"))
            return true;
        return false;
    }
}
