package family.li.aiyun.bean;

import java.io.Serializable;

/**
 * Created by keyC on 2019/6/18.
 * 用户登录token
 */

public class UserToken implements Serializable {

    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
