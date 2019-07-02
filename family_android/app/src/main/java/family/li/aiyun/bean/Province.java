package family.li.aiyun.bean;

import java.io.Serializable;

/**
 * Created by keyC on 2019/6/19.
 * 省份信息
 */

public class Province implements Serializable {

    private String province;
    private String province_code;

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getProvince_code() {
        return province_code;
    }

    public void setProvince_code(String province_code) {
        this.province_code = province_code;
    }
}
