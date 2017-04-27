package com.baijiahulian.live.ui.pptdialog;

import com.baijiahulian.live.ui.base.BasePresenter;
import com.baijiahulian.live.ui.base.BaseView;
import com.baijiahulian.live.ui.recorderdialog.RecorderDialogContract;

/**
 * Created by wangkangfei on 17/4/27.
 */

public interface PPTDialogContract {

    interface View extends BaseView<Presenter> {
        void showFullScreen();

        void showManagePPT();

        void hideManagePPT();
    }

    interface Presenter extends BasePresenter {
        void switchFullscreen();

        void managePPT();
    }
}
