package family.li.aiyun.bean;

import java.io.Serializable;

/**
 * Created by keyC on 2019/6/19.
 * 城市信息
 */

public class City implements Serializable {

    private String city;
    private String city_code;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCity_code() {
        return city_code;
    }

    public void setCity_code(String city_code) {
        this.city_code = city_code;
    }
}
