package com.keyc.mycustomview.bean;


import java.io.Serializable;
import java.util.List;

/**
 * 家族人员
 */

public class FamilyMember implements Serializable {

    private Person self; //自己信息
    private Person spouse;//配偶
    private List<FamilyMember> children;//儿女

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

    public List<FamilyMember> getChildren() {
        return children;
    }

    public void setChildren(List<FamilyMember> children) {
        this.children = children;
    }
}
