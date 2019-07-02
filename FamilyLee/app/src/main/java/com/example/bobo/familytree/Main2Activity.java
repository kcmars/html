package com.example.bobo.familytree;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        FamilyTreeView familyTreeView = (FamilyTreeView) findViewById(R.id.familyTree);
        NodeModel root = new NodeModel("A");

        NodeModel nodeB = new NodeModel("B");
        NodeModel nodeBB = new NodeModel("BB");

        NodeModel nodeE = new NodeModel("E");
        NodeModel nodeF = new NodeModel("F");
        NodeModel nodeG = new NodeModel("G");
        ArrayList<NodeModel> bChild = new ArrayList<NodeModel>();
        bChild.add(nodeE);
        bChild.add(nodeF);
        bChild.add(nodeG);
        nodeB.setChildren(bChild);
        NodeModel nodeC = new NodeModel("C");

        NodeModel nodeH = new NodeModel("H");
        NodeModel nodeI = new NodeModel("I");
        NodeModel nodeJ = new NodeModel("J");
        ArrayList<NodeModel> cChild = new ArrayList<NodeModel>();
        cChild.add(nodeH);
        cChild.add(nodeI);
        cChild.add(nodeJ);
        nodeC.setChildren(cChild);
        NodeModel nodeD = new NodeModel("D");
        NodeModel nodeK = new NodeModel("K");
        NodeModel nodeL = new NodeModel("L");
        NodeModel nodeM = new NodeModel("M");
        NodeModel nodeN = new NodeModel("N");
        ArrayList<NodeModel> dChild = new ArrayList<NodeModel>();
        dChild.add(nodeK);
        dChild.add(nodeL);
        dChild.add(nodeM);
        dChild.add(nodeN);
        nodeD.setChildren(dChild);

        ArrayList<NodeModel> rootChilden = new ArrayList<NodeModel>();
        rootChilden.add(nodeBB);
        rootChilden.add(nodeB);
        rootChilden.add(nodeC);
        rootChilden.add(nodeD);
        root.setChildren(rootChilden);

        FamilyUtils utils = new FamilyUtils();
        FamilyTree rootTree = utils.buchheim(root);
        Log.e("TAG", "onCreate: " + root );
        Log.e("TAG", "onCreate: " + rootTree );
        familyTreeView.setFamilyTree(rootTree);
    }

}
