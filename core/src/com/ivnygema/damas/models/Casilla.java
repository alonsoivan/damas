package com.ivnygema.damas.models;


import com.badlogic.gdx.math.Rectangle;

public class Casilla {
    private Rectangle rect;
    private int i;
    private int j;

    public Casilla(Rectangle rect, int i, int j) {
        this.rect = rect;
        this.i = i;
        this.j = j;
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
}
