package com.ivnygema.damas.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.ivnygema.damas.screens.GameScreen;

import static com.ivnygema.damas.managers.ResourceManager.*;

public class Piece extends Sprite {

    private boolean dama;
    private Rectangle rect;
    private String color;
    private int i = 0;
    private int j = 0;

    public Piece(Vector2 pos, String color, int i, int j){
        super(negrasTexture);
        super.setPosition(pos.x,pos.y);
        rect = new Rectangle(pos.x,pos.y, 64,64);
        this.color = color;

        if(color.equals("b")){
            super.setTexture(blancasTexture);
        }

        this.i = i;
        this.j = j;

    }

    public void draw(SpriteBatch batch) {
        //super.draw(batch);


        batch.draw(this.getTexture(),getScreenCoordinates(rect).x,getScreenCoordinates(rect).y,Gdx.graphics.getWidth()/8,Gdx.graphics.getWidth()/8);
    }

    public static Vector3 getScreenCoordinates(Rectangle rect){
        Vector3 vec=new Vector3(rect.x,rect.y,0);
        return GameScreen.camera.project(vec);
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

    public void setDama() {
        this.dama = true;
        if(color.equals("n"))
            super.setTexture(damanTexture);
        else
            super.setTexture(damabTexture);
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

    public boolean mismoColor(Piece piece){
        return color.equals(piece.color);
    }
}
