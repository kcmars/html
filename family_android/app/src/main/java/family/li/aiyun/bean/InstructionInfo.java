package family.li.aiyun.bean;

import java.io.Serializable;

/**
 * Created by keyC on 2019/6/21.
 * app 功能介绍信息
 */

public class InstructionInfo implements Serializable {

    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
