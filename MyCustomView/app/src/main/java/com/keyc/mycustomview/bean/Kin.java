package com.keyc.mycustomview.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by keyC on 2019/5/30.
 * 家族成员
 */

public class Kin implements Serializable {

    private Person self;   //自己
    private Person spouse;   //配偶
    private ArrayList<Kin> children;   //孩子

    public Person getSelf() {
        return self;
    }

    public void setSelf(Person self) {
        this.self = self;
    }

    public Person getSpouse() {
        return spouse;
    }

    public void setSpouse(Person spouse) {
        this.spouse = spouse;
    }

    public ArrayList<Kin> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<Kin> children) {
        this.children = children;
    }
}
