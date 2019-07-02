package com.keyc.mycustomview.bean;

import java.io.Serializable;

/**
 * Created by keyC on 2019/6/17.
 * 人员信息
 */

public class FamilyBean implements Serializable {

    private int id;
    private String name;
    private String call;
    private String tel;
    private int fatherId;//父亲ID
    private int motherId;//母亲ID
    private int spouseId;//配偶ID

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
}
