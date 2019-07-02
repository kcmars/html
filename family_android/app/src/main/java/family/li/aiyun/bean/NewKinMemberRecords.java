package family.li.aiyun.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by keyC on 2019/6/24.
 * 新族谱树图的数据源
 */

public class NewKinMemberRecords implements Serializable {

    /**
     * root_id： 225   //祖宗id
     * current_user_id： 225   //当前用户id
     * tree_data： []  //所有成员列表
     */

    private int root_id;
    private int current_user_id;
    private List<NewKinMember> tree_data;

    public int getRoot_id() {
        return root_id;
    }

    public void setRoot_id(int root_id) {
        this.root_id = root_id;
    }

    public int getCurrent_user_id() {
        return current_user_id;
    }

    public void setCurrent_user_id(int current_user_id) {
        this.current_user_id = current_user_id;
    }

    public List<NewKinMember> getTree_data() {
        return tree_data;
    }

    public void setTree_data(List<NewKinMember> tree_data) {
        this.tree_data = tree_data;
    }
}
