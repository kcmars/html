package family.li.aiyun.bean;

import java.io.Serializable;

/**
 * Created by keyC on 2019/6/15.
 * 日志信息
 */

public class LogcatInfo implements Serializable {

    private int id;
    private int user_id;
    private int opt;         //新增:1;删除:2;更新:3
    private String content;
    private String add_time;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getOpt() {
        return opt;
    }

    public void setOpt(int opt) {
        this.opt = opt;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
