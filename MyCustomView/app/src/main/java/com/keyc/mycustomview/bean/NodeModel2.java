package com.keyc.mycustomview.bean;

import java.util.ArrayList;

public class NodeModel2 {
    private String name;
    private ArrayList<NodeModel2> children = new ArrayList<>();

    public NodeModel2(String name) {
        this.name = name;
    }

    public NodeModel2(String name, ArrayList<NodeModel2> children) {
        this.name = name;
        this.children = children;
    }

    @Override
    public String toString() {
        return this.name + this.children;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<NodeModel2> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<NodeModel2> children) {
        this.children = children;
    }

}
