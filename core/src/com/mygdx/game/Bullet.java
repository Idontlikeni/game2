package com.mygdx.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;

public class Bullet {
    public float x, y, velocity, angle;
    public String color;
    public boolean dead;
    public Circle circle;
    Bullet(float x, float y, float angle, String color, float velocity){
        this.x = x;
        this.y = y;
        this.color = color;
        this.velocity = velocity;
        this.angle = angle;
        circle = new Circle(x, y, 20);
        dead = false;
    }


    public boolean[] move(Array<Asteroid>arr){
        x += (float)Math.cos(angle) * velocity * Gdx.graphics.getDeltaTime();
        y += (float)Math.sin(angle) * velocity *  Gdx.graphics.getDeltaTime();
        for (Iterator<Asteroid> iter = arr.iterator(); iter.hasNext(); ) {
            Asteroid met = iter.next();
            if(Math.sqrt((x - met.x) * (x - met.x) + (y - met.y) * (y - met.y)) <= 20 + met.radius) {
                if(met.color.equals(color)){
                    iter.remove();
                    return new boolean[]{true, true};
                }
                dead = true;
                return new boolean[]{true, false};
            }
        }

        return new boolean[]{false, false};
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
