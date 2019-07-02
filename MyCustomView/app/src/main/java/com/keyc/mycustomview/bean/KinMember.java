package com.keyc.mycustomview.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by keyC on 2019/6/6.
 * 家族成员
 */

public class KinMember implements Serializable {

    private int id;//人员ID
    private String name;//姓名
    private String call;//称呼
    private String tel;//电话
    private String sex;//性别
    private String portrait;//头像

    private int fatherId;//父亲ID
    private int motherId;//母亲ID
    private int spouseId;//配偶ID

    private int mothersId;//养母ID
    private int fathersId;//养父ID

    private KinMember spouse;//配偶
    private KinMember fosterFather;//养父
    private KinMember fosterMother;//养母
    private KinMember father;//父亲
    private KinMember mother;//母亲
    private List<KinMember> brothers;//兄弟姐妹
    private List<KinMember> children;//儿女
    private boolean isSelect = false;//是否选中

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCall() {
        return call;
    }

    public void setCall(String call) {
        this.call = call;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public int getFatherId() {
        return fatherId;
    }

    public void setFatherId(int fatherId) {
        this.fatherId = fatherId;
    }

    public int getMotherId() {
        return motherId;
    }

    public void setMotherId(int motherId) {
        this.motherId = motherId;
    }

    public int getSpouseId() {
        return spouseId;
    }

    public void setSpouseId(int spouseId) {
        this.spouseId = spouseId;
    }

    public int getMothersId() {
        return mothersId;
    }

    public void setMothersId(int mothersId) {
        this.mothersId = mothersId;
    }

    public int getFathersId() {
        return fathersId;
    }

    public void setFathersId(int fathersId) {
        this.fathersId = fathersId;
    }

    public KinMember getSpouse() {
        return spouse;
    }

    public void setSpouse(KinMember spouse) {
        this.spouse = spouse;
    }

    public KinMember getFosterFather() {
        return fosterFather;
    }

    public void setFosterFather(KinMember fosterFather) {
        this.fosterFather = fosterFather;
    }

    public KinMember getFosterMother() {
        return fosterMother;
    }

    public void setFosterMother(KinMember fosterMother) {
        this.fosterMother = fosterMother;
    }

    public KinMember getFather() {
        return father;
    }

    public void setFather(KinMember father) {
        this.father = father;
    }

    public KinMember getMother() {
        return mother;
    }

    public void setMother(KinMember mother) {
        this.mother = mother;
    }

    public List<KinMember> getBrothers() {
        return brothers;
    }

    public void setBrothers(List<KinMember> brothers) {
        this.brothers = brothers;
    }

    public List<KinMember> getChildren() {
        return children;
    }

    public void setChildren(List<KinMember> children) {
        this.children = children;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
