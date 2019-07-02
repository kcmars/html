package com.example.bobo.familytree;

import java.util.ArrayList;

public class FamilyTree {
    private float x;
    private float y;
    private NodeModel tree;
    private ArrayList<FamilyTree> children;
    private FamilyTree parent;
    private FamilyTree thread;
    private FamilyTree mostSibling;
    private float mode;
    private FamilyTree ancestor;
    private float number;
    private float change;
    private float shift;

    public FamilyTree(NodeModel tree, FamilyTree parent, float number, float depth) {
        this.x = -1.f;
        this.y = depth;
        this.tree = tree;
        this.children = new ArrayList<>();
        for (int i = 0; i < tree.getChildren().size(); i++) {
            NodeModel childrenNode = tree.getChildren().get(i);
            FamilyTree childrenTree = new FamilyTree(childrenNode, this, i + 1.0f, depth + 1.f);
            this.children.add(childrenTree);
        }
        this.parent = parent;
        this.number = number;
        this.ancestor = this;
        this.thread = null;
        this.mode = 0.0f;
        this.change = 0.0f;
        this.shift = 0.0f;
        this.mostSibling = null;
    }

    public FamilyTree left() {
        if (this.children.size() > 0) {
            if (this.thread != null) {
                return this.thread;
            } else {
                return this.children.get(0);
            }
        } else {
            return null;
        }
    }

    public FamilyTree right() {
        if (this.children.size() > 0) {
            if (this.thread != null) {
                return this.thread;
            } else {
                return this.children.get(this.children.size() - 1);
            }
        } else {
            return null;
        }
    }

    public FamilyTree getLeftBrother() {
        FamilyTree temp = null;
        if (this.parent != null){
            for (FamilyTree childTree :this.parent.getChildren()) {
                if (childTree == this){
                    return temp;
                }else {
                    temp = childTree;
                }
            }
        }
        return temp;
    }

    @Override
    public String toString() {
        return String.format("%s: x=%f y=%f mod=%f", this.tree.toString(), this.x, this.y, this.mode);
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

    public NodeModel getTree() {
        return tree;
    }

    public void setTree(NodeModel tree) {
        this.tree = tree;
    }

    public ArrayList<FamilyTree> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<FamilyTree> children) {
        this.children = children;
    }

    public FamilyTree getParent() {
        return parent;
    }

    public void setParent(FamilyTree parent) {
        this.parent = parent;
    }

    public FamilyTree getThread() {
        return thread;
    }

    public void setThread(FamilyTree thread) {
        this.thread = thread;
    }

    public FamilyTree getMostSibling() {
        if (this.mostSibling == null && this.parent != null && this != this.parent.children.get(0)) {
            this.mostSibling = this.parent.children.get(0);
        }
        return this.mostSibling;
    }

    public void setMostSibling(FamilyTree mostSibling) {
        this.mostSibling = mostSibling;
    }

    public float getMode() {
        return mode;
    }

    public void setMode(float mode) {
        this.mode = mode;
    }

    public FamilyTree getAncestor() {
        return ancestor;
    }

    public void setAncestor(FamilyTree ancestor) {
        this.ancestor = ancestor;
    }

    public float getNumber() {
        return number;
    }

    public void setNumber(float number) {
        this.number = number;
    }

    public float getChange() {
        return change;
    }

    public void setChange(float change) {
        this.change = change;
    }

    public float getShift() {
        return shift;
    }

    public void setShift(float shift) {
        this.shift = shift;
    }
}
