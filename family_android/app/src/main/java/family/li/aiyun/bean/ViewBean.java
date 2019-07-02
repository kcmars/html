package family.li.aiyun.bean;

import android.view.View;

import java.io.Serializable;
import java.util.List;

/**
 * Created by keyC on 2019/6/17.
 * 统计所有的view
 */

public class ViewBean implements Serializable {

    private View mineView;
    private View spouseView;
    private List<ViewBean> childrenView;

    public View getMineView() {
        return mineView;
    }

    public void setMineView(View mineView) {
        this.mineView = mineView;
    }

    public View getSpouseView() {
        return spouseView;
    }

    public void setSpouseView(View spouseView) {
        this.spouseView = spouseView;
    }

    public List<ViewBean> getChildrenView() {
        return childrenView;
    }

    public void setChildrenView(List<ViewBean> childrenView) {
        this.childrenView = childrenView;
    }
}
