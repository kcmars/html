package com.example.bobo.familytree;

import java.util.ArrayList;

public class NodeModel {
    private String name;
    private ArrayList<NodeModel>children = new ArrayList<>();

    public NodeModel(String name) {
        this.name = name;
    }

    public NodeModel(String name, ArrayList<NodeModel> children) {
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

    public ArrayList<NodeModel> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<NodeModel> children) {
        this.children = children;
    }

}
