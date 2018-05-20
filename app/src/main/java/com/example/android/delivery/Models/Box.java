package com.example.android.delivery.Models;

/**
 * Created by ASUS on 20/05/2018.
 */

public class Box {
    private String x;
    private String y;
    private String id;

    public Box(String x, String y, String id) {
        this.x = x;
        this.y = y;
        this.id = id;
    }

    public Box() {
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Box{" +
                "x='" + x + '\'' +
                ", y='" + y + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}

