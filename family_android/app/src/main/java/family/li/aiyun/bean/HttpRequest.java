package family.li.aiyun.bean;

import java.io.Serializable;

/**
 * Created by keyC on 2019/6/12.
 * http 请求结果解析 bean 类
 */

public class HttpRequest<T> implements Serializable {

    /**
     * code: 请求是否成功 1 成功， 0 失败
     * data： 请求结果内容
     * msg： 请求结果提示
     * err_code： 1101
     */

    private int code;
    private T data;
    private String msg;
    private int err_code;


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getErr_code() {
        return err_code;
    }

    public void setErr_code(int err_code) {
        this.err_code = err_code;
    }

    @Override
    public String toString() {
        return "HttpRequest{" +
                "code=" + code +
                ", data=" + data +
                ", msg='" + msg + '\'' +
                ", err_code=" + err_code +
                '}';
    }
}
