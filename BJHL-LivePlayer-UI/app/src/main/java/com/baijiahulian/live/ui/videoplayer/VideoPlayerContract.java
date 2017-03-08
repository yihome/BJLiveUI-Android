package com.baijiahulian.live.ui.videoplayer;

import com.baijiahulian.live.ui.base.BasePresenter;
import com.baijiahulian.live.ui.base.BaseView;
import com.baijiahulian.livecore.wrapper.LPPlayer;

/**
 * Created by Shubo on 2017/3/4.
 */

interface VideoPlayerContract {

    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter {
        LPPlayer getPlayer();

        void switchWithMaximum();
    }
}
