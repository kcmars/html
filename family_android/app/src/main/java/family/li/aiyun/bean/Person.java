package family.li.aiyun.bean;

import java.io.Serializable;

/**
 * Created by keyC on 2019/5/30.
 * 人员信息
 */

public class Person implements Serializable {

    /**
     * id： user_id
     * name:  姓名
     * sex:  性别 1 男 2 女
     * head:  头像
     * tag:  标记（自己，妻子，儿子等）
     * not_li: 0 李家人 1 不是李家人
     */

    private int id;
    private String name;
    private int sex;
    private String head;
    private String tag;
    private int not_li;

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

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getNot_li() {
        return not_li;
    }

    public void setNot_li(int not_li) {
        this.not_li = not_li;
    }
}
