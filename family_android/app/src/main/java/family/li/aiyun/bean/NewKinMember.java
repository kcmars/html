package family.li.aiyun.bean;

import java.io.Serializable;

/**
 * Created by keyC on 2019/6/24.
 * 新的族谱树形图中成员信息
 */

public class NewKinMember implements Serializable {
    /**
     * id:
     * user_profile: {}
     * user_relation: {}
     */

    private int id;
    private UserProfile user_profile;
    private UserRelation user_relation;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
