package family.li.aiyun.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by keyC on 2019/6/21.
 * 宗族人员的列表 records
 */

public class KinMemberRecords implements Serializable {

    private List<FamilyMember> data;

    public List<FamilyMember> getData() {
        return data;
    }

    public void setData(List<FamilyMember> data) {
        this.data = data;
    }
}
