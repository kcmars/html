package family.li.aiyun.bean;

import java.io.Serializable;

/**
 * Created by keyC on 2019/6/22.
 * 搜索列表展示的人员信息
 */

public class KinSearchMember implements Serializable {

    /**
     * user_id:
     * name：
     * head_img：
     * father_id：
     * father_name：
     * mother_id：
     * mother_name：
     * is_self: 1 自己
     */

    private int user_id;
    private String name;
    private String head_img;
    private int father_id;
    private String father_name;
    private int mother_id;
    private String mother_name;
    private int is_self;

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

    public String getFather_name() {
        return father_name;
    }

    public void setFather_name(String father_name) {
        this.father_name = father_name;
    }

    public int getMother_id() {
        return mother_id;
    }

    public void setMother_id(int mother_id) {
        this.mother_id = mother_id;
    }

    public String getMother_name() {
        return mother_name;
    }

    public void setMother_name(String mother_name) {
        this.mother_name = mother_name;
    }

    public int getIs_self() {
        return is_self;
    }

    public void setIs_self(int is_self) {
        this.is_self = is_self;
    }
}
