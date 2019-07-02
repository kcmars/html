package com.keyc.mycustomview.bean;

import java.io.Serializable;

/**
 * Created by keyC on 2019/6/11.
 * 坐标(x,y)
 */

public class Point implements Serializable {

    private float x;
    private float y;

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
}
