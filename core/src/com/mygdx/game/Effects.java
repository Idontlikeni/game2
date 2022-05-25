package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public class Effects{
    public float x, y, velocity, angle;
    public float[] color;
    public boolean dead;
    public Rectangle rect;
    public Texture texture;

    public Effects(float x, float y, float velocity, float angle) {
        this.x = x;
        this.y = y;
        this.velocity = velocity;
        this.angle = angle;
        String[] rand = new String[]{"glitch_1.png", "glitch_1.png"};
        texture = new Texture(Gdx.files.internal(rand[MathUtils.random(0, 6000)]));
    }

    /*public move(){

    }*/
}
