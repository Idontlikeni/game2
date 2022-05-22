package com.mygdx.game;

import com.badlogic.gdx.math.Rectangle;

public class ColorButton {
    public float x, y, width, height;
    public Rectangle rectangle;
    public String color;

    public ColorButton(float x, float y, float width, float height, String color) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = color;
        rectangle = new Rectangle(x, y, width, height);
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public String getColor(){
        return color;
    }

    public void setColor(String color){
        this.color = color;
    }
}
