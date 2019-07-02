package com.keyc.mycustomview.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by keyC on 2019/6/11.
 * 存储家族成员的坐标
 */

public class KinPoint implements Serializable {

    private Point point;
    private List<KinPoint> child;

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public List<KinPoint> getChild() {
        return child;
    }

    public void setChild(List<KinPoint> child) {
        this.child = child;
    }
}
