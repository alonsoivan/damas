package com.ivnygema.damas.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

class Snake{
    Texture t;
    int[] pos;

    Snake(Texture t, int[] pos){
        this.t = t;
        this.pos = pos;
    }
}

public class SnakeScreen implements Screen {
    private SpriteBatch batch = new SpriteBatch();
    Snake[] snake;
    int[] fruta;
    String dir = "W";
    Texture f;
    Texture s;
    @Override
    public void show() {
        s = new Texture("piezas/p1/negra.png");
        f = new Texture("piezas/p1/blanca.png");

        fruta = new int[]{0,0};

        snake = new Snake[]{new Snake(s,new int[]{2,2}),new Snake(s,new int[]{2,3}),new Snake(s,new int[]{2,4}),
                new Snake(s,new int[]{2,5}),new Snake(s,new int[]{2,6})};


        TimerTask task = new TimerTask() {
            @Override
            public void run()
            {
               avanzar(dir);
            }
        };
        // Empezamos dentro de 10ms y luego lanzamos la tarea cada 1000ms
        Timer timer = new Timer();
        timer.schedule(task, 10, 200);
    }

    int width = Gdx.graphics.getWidth()/20;
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        for(Snake snake: snake){
            batch.draw(snake.t,snake.pos[0]*width,snake.pos[1]*width,width,width);
        }

        batch.draw(f,fruta[0]*width,fruta[1]*width,width,width);

        batch.end();

        update();
    }
    public void update(){
      if(Gdx.input.isKeyJustPressed(Input.Keys.W))
          dir = "W";
        if(Gdx.input.isKeyJustPressed(Input.Keys.A))
            dir = "A";
        if(Gdx.input.isKeyJustPressed(Input.Keys.S))
            dir = "S";
        if(Gdx.input.isKeyJustPressed(Input.Keys.D))
            dir = "D";
    }

    public void avanzar(String dir){

        int[][] aux = new int[snake.length][2];
        for(int i = 0 ; i < snake.length; i++){
            aux[i][0] = snake[i].pos[0];
            aux[i][1] = snake[i].pos[1];
        }

        switch (dir){
            case "W":
                if(aux[0][1] +1 > Gdx.graphics.getHeight() / width)
                    snake[0].pos = new int[]{aux[0][0] ,0};
                else
                    snake[0].pos = new int[]{aux[0][0] ,aux[0][1] +1};
                break;
            case "S":
                if(aux[0][1] - 1 < 0)
                    snake[0].pos = new int[]{aux[0][0] ,Gdx.graphics.getHeight() / width};
                else
                    snake[0].pos = new int[]{aux[0][0] ,aux[0][1] -1};
                break;
            case "A":
                if(aux[0][0] - 1 < 0)
                    snake[0].pos = new int[]{Gdx.graphics.getWidth() / width -1,aux[0][1] };
                else
                    snake[0].pos = new int[]{aux[0][0] - 1,aux[0][1] };
                break;
            case "D":
                if (aux[0][0] + 1 > Gdx.graphics.getWidth()/width -1)
                    snake[0].pos = new int[]{0,aux[0][1] };
                else
                    snake[0].pos = new int[]{aux[0][0] + 1,aux[0][1] };
                break;
        }

        System.out.println(snake[0].pos[0]+" "+snake[0].pos[1]);
        System.out.println(fruta[0]+" "+fruta[1]);
        if(snake[0].pos[0] == fruta[0] && fruta[1] == snake[0].pos[1]) {
            snake = Arrays.copyOf(snake,snake.length+1);
            snake[snake.length-1] = new Snake(s,new int[]{snake[snake.length-2].pos[0],snake[snake.length-2].pos[1]});
            fruta = new int[]{MathUtils.random(0,Gdx.graphics.getWidth()/width-1),MathUtils.random(0,Gdx.graphics.getHeight()/width-1)};
        }

        for(int i = 1 ; i<snake.length;i++){
            snake[i].pos = aux[i-1];
        }

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
