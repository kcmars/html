package family.li.aiyun.bean;

import java.io.Serializable;

/**
 * Created by keyC on 2019/6/21.
 * 分享类容
 */

public class ShareInfo implements Serializable {

    /**
     * type: "qq, 微信, 朋友圈"
     * clip_text : "剪切板的内容"
     * description : "分享的简要信息"
     * img_url : "图标地址"
     * title: "分享的标题"
     * url : "内容地址"
     */

    private int type;
    private String clip_text;
    private String description;
    private String img_url;
    private String title;
    private String url;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getClip_text() {
        return clip_text;
    }

    public void setClip_text(String clip_text) {
        this.clip_text = clip_text;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "ShareInfo{" +
                "clip_text='" + clip_text + '\'' +
                ", description='" + description + '\'' +
                ", img_url='" + img_url + '\'' +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
