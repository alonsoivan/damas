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
    private int i, j;
    private Rectangle nuevaPos;

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

    double vel;
    public void draw(SpriteBatch batch, float dt) {
        //super.draw(batch);

        if (nuevaPos!=null) {

            System.out.println("distancia a recorrer: "+abs(nuevaPos.x - rect.x));
            System.out.println("distancia 1 cuadrado: "+(64) );
            // Movemos mas rapido a la dama cuando tiene que recorrer mayores distancias.
            if(isDama() && (abs(nuevaPos.x - rect.x)) >= (64*1.5f)) {
                vel = 850 * dt;
                System.out.println("Velocidad absurda");
            }else {
                vel = 500 * dt;
                System.out.println("Velocidad normal");
            }

            // Ajustamos la velocidad para que llegue suave
            if(abs(nuevaPos.x - rect.x) < vel)
                vel = 1;

            // Si está lo suficientemente cerca, <1 pixel, hacemos tp
            if(abs(nuevaPos.x - rect.x) < 1){
                rect.x = nuevaPos.x;
                rect.y = nuevaPos.y;
            }

            if (nuevaPos.x > rect.x)
                rect.x+=vel;
            if (nuevaPos.x < rect.x)
                rect.x-=vel;
            if (nuevaPos.y > rect.y)
                rect.y+=vel;
            if (nuevaPos.y < rect.y)
                rect.y-=vel;

            if(nuevaPos.x == rect.x && nuevaPos.y == rect.y){
                moveSound.play(0.7f);
                nuevaPos = null;
            }else {
                System.out.println("NuevaPosX: "+nuevaPos.x+ " NuevaPosY: "+nuevaPos.y);
                System.out.println("PosX: "+rect.x+" PosY: "+rect.y);
            }
        }
        super.setPosition(rect.x,rect.y);

        batch.draw(this.getTexture(),getScreenCoordinates(rect).x,getScreenCoordinates(rect).y,Gdx.graphics.getWidth()/8,Gdx.graphics.getWidth()/8);
    }

    public static Vector3 getScreenCoordinates(Rectangle rect){
        Vector3 vec=new Vector3(rect.x,rect.y,0);
        return GameScreen.camera.project(vec);
    }

    public static Vector3 getScreenCoordinates(Vector2 rect){
        Vector3 vec=new Vector3(rect.x,rect.y,0);
        return GameScreen.camera.project(vec);
    }

    public float abs(float num){
        if(num<0)
            return num + num*-2;
        else
            return num;
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
        if(!dama) {
            this.dama = true;

            damaSound.play(0.7f);

            if (color.equals("n"))
                super.setTexture(damanTexture);
            else
                super.setTexture(damabTexture);
        }
    }

    public void setRect(Rectangle rect) {
        //this.rect = rect;
        nuevaPos = rect;
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

    public String getPieceColor(){
        return color;
    }

    public boolean mismoColor(Piece piece){
        return color.equals(piece.color);
    }
}
