package family.li.aiyun.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by keyC on 2019/6/12.
 * 成员信息
 */

public class KinMember implements Serializable {

    /**
     * "user_id": 56,
     * "name": 1,
     * "head_img": 0,
     * "father_id": 0,
     * "mother_id": 0,
     * "couple_id": 0,
     * "sex": "",
     * "is_ex":
     * "root_id":
     * not_li: 0 是李家人  1 不是李家人（配偶）
     */

    private int user_id;
    private String name;
    private String head_img;
    private int father_id;
    private int mother_id;
    private int couple_id;
    private int sex;
    private int is_ex;
    private int root_id;
    private int not_li;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHead_img() {
        return head_img;
    }

    public void setHead_img(String head_img) {
        this.head_img = head_img;
    }

    public int getFather_id() {
        return father_id;
    }

    public void setFather_id(int father_id) {
        this.father_id = father_id;
    }

    public int getMother_id() {
        return mother_id;
    }

    public void setMother_id(int mother_id) {
        this.mother_id = mother_id;
    }

    public int getCouple_id() {
        return couple_id;
    }

    public void setCouple_id(int couple_id) {
        this.couple_id = couple_id;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getIs_ex() {
        return is_ex;
    }

    public void setIs_ex(int is_ex) {
        this.is_ex = is_ex;
    }

    public int getRoot_id() {
        return root_id;
    }

    public void setRoot_id(int root_id) {
        this.root_id = root_id;
    }

    public int getNot_li() {
        return not_li;
    }

    public void setNot_li(int not_li) {
        this.not_li = not_li;
    }
}
