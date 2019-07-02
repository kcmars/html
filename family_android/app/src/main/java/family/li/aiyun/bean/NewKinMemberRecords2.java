package family.li.aiyun.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by keyC on 2019/6/24.
 * 新关系链条的数据源
 */

public class NewKinMemberRecords2 implements Serializable {

    /**
     * root_id： 225   //祖宗id
     * tree_data： []  //所有成员列表
     */

    private int root_id;
    private List<KinMember> tree_data;

    public int getRoot_id() {
        return root_id;
    }

    public void setRoot_id(int root_id) {
        this.root_id = root_id;
    }

    public List<KinMember> getTree_data() {
        return tree_data;
    }

    public void setTree_data(List<KinMember> tree_data) {
        this.tree_data = tree_data;
    }
}
