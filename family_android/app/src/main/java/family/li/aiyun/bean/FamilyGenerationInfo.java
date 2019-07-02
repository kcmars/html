package family.li.aiyun.bean;

import java.io.Serializable;

/**
 * Created by keyC on 2019/6/22.
 * 房代信息
 */

public class FamilyGenerationInfo implements Serializable {

    private int id;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
