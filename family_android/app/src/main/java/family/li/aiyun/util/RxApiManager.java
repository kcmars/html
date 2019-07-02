package family.li.aiyun.util;

import android.util.ArrayMap;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * 作者：created by keyC
 * 日期：2018/7/19
 */
public class RxApiManager implements RxActionManager<Object> {
    private static RxApiManager sInstance = null;

    private ArrayMap<Object, Disposable> maps;
    private CompositeDisposable disposables ;
    public static RxApiManager get() {

        if (sInstance == null) {
            synchronized (RxApiManager.class) {
                if (sInstance == null) {
                    sInstance = new RxApiManager();
                }
            }
        }
        return sInstance;
    }


    private RxApiManager() {
        maps = new ArrayMap<>();
        disposables= new CompositeDisposable();
    }

    @Override
    public void add(Object tag, Disposable subscription) {
        maps.put(tag, subscription);
        disposables.add(subscription);
    }



    public void remove(Object tag) {
        if (!maps.isEmpty()) {
            Disposable disposable=maps.get(tag);
            maps.remove(tag);
            if (disposables != null && disposable != null) {
                disposables.remove(disposable);
            }
        }
    }



    @Override
    public void cancel(Object tag) {
        if (maps.isEmpty()) {
            return;
        }
        if (maps.get(tag) == null) {
            return;
        }
        if (!maps.get(tag).isDisposed()) {
            disposables.remove(maps.get(tag));
            maps.remove(tag);
        }
    }
    @Override
     public void cancelAll() {
        if (maps.isEmpty()) {
            return;
        }
          disposables.clear();
          maps.clear();

    }
}
