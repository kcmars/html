package family.li.aiyun.base;

/**
 * Created by keyC on 2019/6/12.
 */
public interface BaseLoadListener<T> {
    /**
     * 加载数据状态=1成功
     *
     * @param data
     */
    void loadStatusSuccess(T data, String string);
    /**
     * 加载数据状态=0成功
     *
     */
    void loadStatusFail(String msg);

    /**
     * 加载失败
     *
     * @param message
     */
    void loadFailure(String message);

    /**
     * 开始加载
     */
    void loadStart();

    /**
     * 加载结束
     */
    void loadComplete();
}
