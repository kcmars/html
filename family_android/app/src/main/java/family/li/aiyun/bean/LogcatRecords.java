package family.li.aiyun.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by keyC on 2019/6/18.
 * 日志返回列表
 */

public class LogcatRecords implements Serializable {

    private List<LogcatInfo> records;

    public List<LogcatInfo> getRecords() {
        return records;
    }

    public void setRecords(List<LogcatInfo> records) {
        this.records = records;
    }
}
