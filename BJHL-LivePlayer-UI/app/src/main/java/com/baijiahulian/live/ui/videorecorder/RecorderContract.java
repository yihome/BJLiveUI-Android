package com.baijiahulian.live.ui.videorecorder;

import com.baijiahulian.live.ui.base.BaseSwitchPresenter;
import com.baijiahulian.live.ui.base.BaseSwitchView;
import com.baijiahulian.livecore.wrapper.LPRecorder;

/**
 * Created by Shubo on 2017/2/18.
 */

interface RecorderContract {

    interface View extends BaseSwitchView<Presenter>{

    }

    interface Presenter extends BaseSwitchPresenter{
        LPRecorder getRecorder();
    }
}
