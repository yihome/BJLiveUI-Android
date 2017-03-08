package com.baijiahulian.live.ui.videorecorder;

import com.baijiahulian.live.ui.base.BasePresenter;
import com.baijiahulian.live.ui.base.BaseView;
import com.baijiahulian.livecore.wrapper.LPRecorder;

/**
 * Created by Shubo on 2017/2/18.
 */

interface VideoRecorderContract {

    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter {
        LPRecorder getRecorder();

        void switchWithMaximum();
    }
}
