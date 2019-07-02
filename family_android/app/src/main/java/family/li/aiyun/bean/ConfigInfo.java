package family.li.aiyun.bean;

import java.io.Serializable;

/**
 * Created by keyC on 2019/6/25.
 * 配置信息
 */

public class ConfigInfo implements Serializable {

    /**
     * id：
     * name:
     */

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
