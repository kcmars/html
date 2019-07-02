package com.keyc.mycustomview.utils;

import com.keyc.mycustomview.bean.FamilyTree;
import com.keyc.mycustomview.bean.NodeModel2;

public class FamilyUtils {

    public FamilyTree buchheim(NodeModel2 node) {
        FamilyTree rootTree = new FamilyTree(node, null, 1.f, 0.f);

        FamilyTree dt = this.firstWalk(rootTree, 1.0f);

        Float min = this.secondWalk(dt, 0.f, 0.f, null);
        if (min != null && min < 0) {
            this.thirdWalk(dt, -min);
        }
        return dt;
    }

    private FamilyTree firstWalk(FamilyTree rootTree, float distance) {
        if (rootTree.getChildren().size() == 0) {
            if (rootTree.getMostSibling() != null) {
                rootTree.setX(rootTree.getLeftBrother().getX() + distance);
            } else {
                rootTree.setX(0.f);
            }
        } else {
            FamilyTree ancestor = rootTree.getChildren().get(0);
            for (FamilyTree childrenTree : rootTree.getChildren()) {
                this.firstWalk(childrenTree, 1.f);
                ancestor = this.apportion(childrenTree, ancestor, distance);
            }
            this.executeShifts(rootTree);
            float midPoint = (rootTree.getChildren().get(0).getX() + rootTree.getChildren().get(rootTree.getChildren().size() - 1).getX()) / 2;
            FamilyTree brotherTree = rootTree.getLeftBrother();
            if (brotherTree != null) {
                rootTree.setX(brotherTree.getX() + distance);
                rootTree.setMode(rootTree.getX() - midPoint);
            } else {
                rootTree.setX(midPoint);
            }
        }
        return rootTree;
    }

    private Float secondWalk(FamilyTree rootTree, float mode, float depth, Float min) {
        rootTree.setX(rootTree.getX() + mode);
        rootTree.setY(depth);
        if (min == null || rootTree.getX() < min) {
            min = rootTree.getX();
        }
        for (FamilyTree childrenTree : rootTree.getChildren()) {
            min = this.secondWalk(childrenTree, mode + rootTree.getMode(), depth + 1, min);
        }
        return min;
    }

    private void thirdWalk(FamilyTree rootTree, float offset) {
        rootTree.setX(rootTree.getX() + offset);
        for (FamilyTree childrenTree : rootTree.getChildren()) {
            this.thirdWalk(childrenTree, offset);
        }
    }

    private FamilyTree apportion(FamilyTree rootTree, FamilyTree ancestor, float distance) {
        FamilyTree brotherTree = rootTree.getLeftBrother();
        if (brotherTree != null) {
            FamilyTree vir = rootTree;
            FamilyTree vil = brotherTree;
            FamilyTree vor = rootTree;
            FamilyTree vol = rootTree.getMostSibling();
            float sir = rootTree.getMode();
            float sil = vil.getMode();
            float sor = rootTree.getMode();
            float sol = vol.getMode();
            while (vil.right() != null && vir.left() != null) {
                vil = vil.right();
                vir = vir.left();
                vol = (vol != null) ? vol.left() : null;
                vor = vor.right();
                vor.setAncestor(rootTree);
                float shift = (vil.getX() + sil) - (vir.getX() + sir) + distance;
                if (shift > 0) {
                    this.moveSubtree(this.ancestor(vil, rootTree, ancestor), rootTree, shift);
                    sir = sir + shift;
                    sor = sor + shift;
                }
                sil += vil.getMode();
                sir += vir.getMode();
                sol += (vol != null) ? vol.getMode() : 0;
                sor += vor.getMode();

            }
            if (vil.right() != null && vor.right() == null) {
                vor.setThread(vil.right());
                vor.setMode((sil - sor) + vor.getMode());
            } else {
                if (vir.left() != null && (vol != null && vol.left() == null)) {
                    vol.setThread(vir.left());
                    vol.setMode((sir - sol) + vol.getMode());
                }
                ancestor = rootTree;
            }
        }
        return ancestor;
    }

    private void executeShifts(FamilyTree rootTree) {
        float shift = 0.f, change = 0.f;
        for (int i = rootTree.getChildren().size() - 1; i >= 0; i--) {
            FamilyTree childTree = rootTree.getChildren().get(i);
            childTree.setX(shift + childTree.getX());
            childTree.setMode(shift + childTree.getMode());
            change += childTree.getChange();
            shift += childTree.getShift() + change;
        }
    }

    private void moveSubtree(FamilyTree wl, FamilyTree wr, float shift) {
        float subDistance = wr.getNumber() - wl.getNumber();
        wr.setChange(wr.getChange() - (shift / subDistance));
        wr.setShift(wr.getShift() + shift);
        wr.setX(wr.getX() + shift);
        wr.setMode(wr.getMode() + shift);
        wl.setChange(wl.getChange() + (shift / subDistance));
    }

    private FamilyTree ancestor(FamilyTree vil, FamilyTree vir, FamilyTree defaultAncestor) {
        for (FamilyTree childTree : vir.getChildren()) {
            if (childTree.getAncestor().getX() == vil.getAncestor().getX()
                    && childTree.getAncestor().getY() == vil.getAncestor().getY()) {
                return vil.getAncestor();
            }
        }
        return defaultAncestor;
    }

}
