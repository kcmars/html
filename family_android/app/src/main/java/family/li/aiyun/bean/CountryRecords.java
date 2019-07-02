package family.li.aiyun.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by keyC on 2019/6/18.
 * 国家地区列表
 */

public class CountryRecords implements Serializable {

    private List<CountryCode> records;

    public List<CountryCode> getRecords() {
        return records;
    }

    public void setRecords(List<CountryCode> records) {
        this.records = records;
    }
}
