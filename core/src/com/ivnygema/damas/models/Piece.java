package com.ivnygema.damas.models;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
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

    @Override
    public void draw(Batch batch) {
        super.draw(batch);
    }
}
