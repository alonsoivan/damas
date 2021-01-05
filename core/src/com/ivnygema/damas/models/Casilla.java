package com.ivnygema.damas.models;


import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class Casilla {
    public Rectangle rect;
    public int i;
    public int j;
    private boolean amenaza;
    private Array<int[]> amenazas = new Array<>();

    public Casilla(Rectangle rect, int i, int j) {
        this.rect = rect;
        this.i = i;
        this.j = j;
    }

    public Casilla(Rectangle rect, int i, int j, Array<int[]> amenazas){
        this.rect = rect;
        this.i = i;
        this.j = j;
        if(!amenazas.isEmpty())
            amenaza = true;

        for (int[] amenaza : amenazas)
            this.amenazas.add(amenaza);
    }

    public Rectangle getRect() {
        return rect;
    }

    public void setRect(Rectangle rect) {
        this.rect = rect;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public int getJ() {
        return j;
    }

    public void setJ(int j) {
        this.j = j;
    }

    public boolean hayAmenaza() {
        return amenaza;
    }

    public void setAmenaza(boolean amenaza) {
        this.amenaza = amenaza;
    }

    public Array<int[]> getAmenazas(){
        return amenazas;
    }
}
