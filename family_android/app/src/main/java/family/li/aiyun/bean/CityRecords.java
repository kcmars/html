package family.li.aiyun.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by keyC on 2019/6/19.
 * 城市records
 */

public class CityRecords implements Serializable {

    private List<City> records;

    public List<City> getRecords() {
        return records;
    }

    public void setRecords(List<City> records) {
        this.records = records;
    }
}
