package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;

public class Asteroid {
    public float x, y, velocity, radius;
    public String color;
    public String chcolor;
    public boolean dead;
    public Circle circle;
    public boolean dynamic;
    public int change;
    public int chng;

    Asteroid(float x, float y, float radius, String color, float velocity,boolean dynamic) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.velocity = velocity;
        this.radius = radius;
        circle = new Circle(x, y, 20);
        dead = false;
        this.dynamic = dynamic;
        change = MathUtils.random(150, 250);
        chng = 0;
        String[] arr = new String[2];
        switch (color) {
            case "green":
                arr[0] = "blue";
                arr[1] = "red";
                break;
            case "blue":
                arr[0] = "green";
                arr[1] = "red";
                break;
            case "red":
                arr[0] = "green";
                arr[1] = "blue";
                break;
        }
        chcolor = arr[MathUtils.random(0, 1)];
    }


    public void move() {
        if (dynamic) chng++;
        if (chng >= change) {
            dynamic = false;
            color = chcolor;
        }
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

    public void die() {
        dead = true;
    }
}
