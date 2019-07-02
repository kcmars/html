package family.li.aiyun.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by keyC on 2019/6/25.
 * 排行数据
 */

public class ConfigRank implements Serializable {

    /**
     * male: [] 男排行
     * female: [] 女排行
     */

    private List<ConfigInfo> male;
    private List<ConfigInfo> female;

    public List<ConfigInfo> getMale() {
        return male;
    }

    public void setMale(List<ConfigInfo> male) {
        this.male = male;
    }

    public List<ConfigInfo> getFemale() {
        return female;
    }

    public void setFemale(List<ConfigInfo> female) {
        this.female = female;
    }
}
