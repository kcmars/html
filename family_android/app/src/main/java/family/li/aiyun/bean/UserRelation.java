package family.li.aiyun.bean;

import java.io.Serializable;

/**
 * Created by keyC on 2019/6/19.
 *
 */

public class UserRelation implements Serializable {

    /**
     * user_id: 用户user_id
     * father_id: 父亲id
     * mother_id: 母亲id
     * root_id: 祖先id
     * bro_relate:
     * couple_id: 配偶id
     * father_name: 父亲姓名
     * mother_name: 母亲姓名
     * not_li: 0 是李家人  1 不是李家人（配偶）
     * sex: 1 男 2 女
     */
    private int user_id;
    private int father_id;
    private int root_id;
    private String bro_relate;
    private int couple_id;
    private String father_name;
    private String mother_name;
    private int not_li;
    private int sex;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getFather_id() {
        return father_id;
    }

    public void setFather_id(int father_id) {
        this.father_id = father_id;
    }

    public int getRoot_id() {
        return root_id;
    }

    public void setRoot_id(int root_id) {
        this.root_id = root_id;
    }

    public String getBro_relate() {
        return bro_relate;
    }

    public void setBro_relate(String bro_relate) {
        this.bro_relate = bro_relate;
    }

    public int getCouple_id() {
        return couple_id;
    }

    public void setCouple_id(int couple_id) {
        this.couple_id = couple_id;
    }

    public String getFather_name() {
        return father_name;
    }

    public void setFather_name(String father_name) {
        this.father_name = father_name;
    }

    public String getMother_name() {
        return mother_name;
    }

    public void setMother_name(String mother_name) {
        this.mother_name = mother_name;
    }

    public int getNot_li() {
        return not_li;
    }

    public void setNot_li(int not_li) {
        this.not_li = not_li;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }
}


