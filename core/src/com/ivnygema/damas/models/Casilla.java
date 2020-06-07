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
}
