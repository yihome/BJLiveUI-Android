package com.baijiahulian.live.ui.recorderdialog;

import com.baijiahulian.live.ui.base.BasePresenter;
import com.baijiahulian.live.ui.base.BaseView;

/**
 * Created by wangkangfei on 17/4/27.
 */

public interface RecorderDialogContract {

    interface View extends BaseView<Presenter> {
        void showFullscreen();

        void showSwitchCamera();

        void showSwitchPrettyFilterOn();

        void showSwitchPrettyFilterOff();

        void showTurnOffCamera();
    }

    interface Presenter extends BasePresenter {
        void switchFullscreen();

        void switchCamera();

        void switchPrettyFilter();

        void turnOffCamera();
    }
}
