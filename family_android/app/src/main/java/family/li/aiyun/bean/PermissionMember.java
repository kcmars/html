package family.li.aiyun.bean;

import java.io.Serializable;

/**
 * Created by keyC on 2019/6/18.
 * 权限人员信息
 */

public class PermissionMember implements Serializable {

    private String name;
    private String head_img;

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
}
