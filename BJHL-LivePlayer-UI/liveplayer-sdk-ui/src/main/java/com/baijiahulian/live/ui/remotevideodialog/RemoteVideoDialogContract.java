package com.baijiahulian.live.ui.remotevideodialog;

import com.baijiahulian.live.ui.base.BasePresenter;
import com.baijiahulian.live.ui.base.BaseView;

/**
 * Created by wangkangfei on 17/4/27.
 */

public interface RemoteVideoDialogContract {

    interface View extends BaseView<Presenter> {
        void showCurrentRemoteUserName(String name);

        void showCloseSpeaking();

        void hideCloseSpeaking();
    }

    interface Presenter extends BasePresenter {
        void switchFullscreen();

        void closeVideo();

        void closeSpeaking();
    }
}
