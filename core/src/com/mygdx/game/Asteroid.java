package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;

public class Asteroid {
    public float x, y, velocity, radius;
    public String color;
    public boolean dead;
    public Circle circle;
    Asteroid(float x, float y, float radius, String color, float velocity){
        this.x = x;
        this.y = y;
        this.color = color;
        this.velocity = velocity;
        this.radius = radius;
        circle = new Circle(x, y, 20);
        dead = false;
    }


    public void move(){
        y -= velocity * Gdx.graphics.getDeltaTime();
    }

    public boolean isDead() {
        return dead;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void die(){
        dead = true;
    }
}
