package family.li.aiyun.base;

/**
 * Created by keyC on 2019/6/12.
 * http 请求结果回调
 */

public interface HttpRequestCallback<T> {
    /**
     * 请求成功 status = 1
     * @param data 请求结果内容
     * @param msg 请求结果提示
     */
    void requestSuccess(T data, String msg);

    /**
     * 请求失败 status = 0
     * @param msg 请求结果提示
     */
    void requestFail(String msg);

    /**
     * 请求错误
     * @param msg 请求结果提示
     */
    void requestError(String msg);

    /**
     * 请求加载
     */
    void requestStart();

    /**
     * 请求结束
     */
    void requestComplete();
}
