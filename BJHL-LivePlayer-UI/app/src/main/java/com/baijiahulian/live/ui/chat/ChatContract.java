package com.baijiahulian.live.ui.chat;

import com.baijiahulian.live.ui.base.BasePresenter;
import com.baijiahulian.live.ui.base.BaseView;
import com.baijiahulian.livecore.models.imodels.IMessageModel;

/**
 * Created by Shubo on 2017/2/15.
 */

interface ChatContract {

    interface View extends BaseView<Presenter>{
        void notifyDataChanged();

        void clearScreen();

        void unClearScreen();
    }

    interface Presenter extends BasePresenter{
        int getCount();

        IMessageModel getMessage(int position);
    }
}
