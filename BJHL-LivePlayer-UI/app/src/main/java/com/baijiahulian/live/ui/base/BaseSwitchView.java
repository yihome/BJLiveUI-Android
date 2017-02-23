package com.baijiahulian.live.ui.base;

/**
 * Created by Shubo on 2017/2/18.
 */

public interface BaseSwitchView<T extends BaseSwitchPresenter> extends BaseView<T> {

    boolean isMaximised();

    void setIsDisplayMaximised(boolean maximised);
}
