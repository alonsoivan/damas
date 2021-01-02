package com.ivnygema.damas.models;


import com.badlogic.gdx.math.Rectangle;

public class Casilla {
    public Rectangle rect;
    public int i;
    public int j;
    private int amenazai;
    private int amenazaj;
    private boolean amenaza;

    public Casilla(Rectangle rect, int i, int j) {
        this.rect = rect;
        this.i = i;
        this.j = j;
    }

    public Casilla(Rectangle rect, int i, int j, int amenazai, int amenazaj) {
        this.rect = rect;
        this.i = i;
        this.j = j;
        this.amenaza = true;
        this.amenazai = amenazai;
        this.amenazaj = amenazaj;
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

    public int getAmenazai() {
        return amenazai;
    }

    public void setAmenazai(int amenazai) {
        this.amenazai = amenazai;
    }

    public int getAmenazaj() {
        return amenazaj;
    }

    public void setAmenazaj(int amenazaj) {
        this.amenazaj = amenazaj;
    }

    public boolean hayAmenaza() {
        return amenaza;
    }

    public void setAmenaza(boolean amenaza) {
        this.amenaza = amenaza;
    }
}
