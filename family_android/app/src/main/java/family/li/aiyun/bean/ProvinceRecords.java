package family.li.aiyun.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by keyC on 2019/6/19.
 * 省份records
 */

public class ProvinceRecords implements Serializable {

    private List<Province> records;

    public List<Province> getRecords() {
        return records;
    }

    public void setRecords(List<Province> records) {
        this.records = records;
    }
}
