package family.li.aiyun.bean;

import java.io.Serializable;

/**
 * Created by keyC on 2019/6/24.
 * 判断被邀请人的手机号是否注册的信息
 */

public class RegisterPhoneInfo implements Serializable {

    /**
     * need_register： 邀请是否成功 0 不需要注册 1 需要注册
     * url: 需要注册的地址
     * message: "邀请成功"
     * title: "邀请成功"
     * description: "邀请你加入他的李氏族亲"
     * img_url: "logo"
     */

    private int need_register;
    private String url;
    private String message;
    private String title;
    private String description;
    private String img_url;

    public int getNeed_register() {
        return need_register;
    }

    public void setNeed_register(int need_register) {
        this.need_register = need_register;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }
}
