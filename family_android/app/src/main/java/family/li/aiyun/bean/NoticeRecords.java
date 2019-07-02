package family.li.aiyun.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by keyC on 2019/6/18.
 * 消息，公告，返回列表
 */

public class NoticeRecords implements Serializable {

    private List<NoticeInfo> records;

    public List<NoticeInfo> getRecords() {
        return records;
    }

    public void setRecords(List<NoticeInfo> records) {
        this.records = records;
    }
}
