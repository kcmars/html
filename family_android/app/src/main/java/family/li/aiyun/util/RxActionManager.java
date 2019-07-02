package family.li.aiyun.util;

import io.reactivex.disposables.Disposable;

/**
 * 作者：created by keyC
 * 日期：2018/7/19
 */
public interface RxActionManager<T> {

    void add(T tag, Disposable d);
    void remove(T tag);
    void cancel(T tag);
    void cancelAll();
}
