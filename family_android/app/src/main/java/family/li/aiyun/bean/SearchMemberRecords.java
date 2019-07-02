package family.li.aiyun.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by keyC on 2019/6/19.
 * 搜索人员信息 records
 */

public class SearchMemberRecords implements Serializable {

    private List<KinSearchMember> records;

    public List<KinSearchMember> getRecords() {
        return records;
    }

    public void setRecords(List<KinSearchMember> records) {
        this.records = records;
    }
}
