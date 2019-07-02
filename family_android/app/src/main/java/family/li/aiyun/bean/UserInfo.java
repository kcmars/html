package family.li.aiyun.bean;

import java.io.Serializable;

/**
 * Created by keyC on 2019/6/19.
 * 用户信息详情
 */

public class UserInfo implements Serializable {

    /**
     * id: id
     * area_code: 国籍code
     * phone: 电话
     * phone_display: 电话带星星
     * last_login_ip: 上次登录ip地址
     * last_login_time: 上次登录时间
     * is_admin: 是否管理员 0 否 1 是
     * hide_info: 是否隐藏个人资料 0 否 1 是
     * user_profile: {}
     * user_relation: {}
     */

    private int id;
    private int area_code;
    private String phone;
    private String phone_display;
    private int is_admin;
    private int hide_info;
    private UserProfile user_profile;
    private UserRelation user_relation;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getArea_code() {
        return area_code;
    }

    public void setArea_code(int area_code) {
        this.area_code = area_code;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone_display() {
        return phone_display;
    }

    public void setPhone_display(String phone_display) {
        this.phone_display = phone_display;
    }

    public int getIs_admin() {
        return is_admin;
    }

    public void setIs_admin(int is_admin) {
        this.is_admin = is_admin;
    }

    public int getHide_info() {
        return hide_info;
    }

    public void setHide_info(int hide_info) {
        this.hide_info = hide_info;
    }

    public UserProfile getUser_profile() {
        return user_profile;
    }

    public void setUser_profile(UserProfile user_profile) {
        this.user_profile = user_profile;
    }

    public UserRelation getUser_relation() {
        return user_relation;
    }

    public void setUser_relation(UserRelation user_relation) {
        this.user_relation = user_relation;
    }
}
