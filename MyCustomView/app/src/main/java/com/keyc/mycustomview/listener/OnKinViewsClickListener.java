package com.keyc.mycustomview.listener;

import com.keyc.mycustomview.bean.Person;

/**
 * Created by keyC on 2019/6/4.
 * 视图点击事件
 */

public interface OnKinViewsClickListener {
    /**
     * 视图点击事件，传递当前视图显示参数的内容
     */
    void onViewsClick(Person person);
}
