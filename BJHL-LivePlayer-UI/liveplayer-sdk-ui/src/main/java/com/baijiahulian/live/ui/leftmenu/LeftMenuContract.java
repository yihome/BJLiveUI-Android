package com.baijiahulian.live.ui.leftmenu;

import com.baijiahulian.live.ui.base.BasePresenter;
import com.baijiahulian.live.ui.base.BaseView;

/**
 * Created by Shubo on 2017/2/15.
 */

interface LeftMenuContract {
    interface View extends BaseView<Presenter> {
        void notifyClearScreenChanged(boolean isCleared);
    }

    interface Presenter extends BasePresenter {
        void clearScreen();

        void showMessageInput();
    }
}
