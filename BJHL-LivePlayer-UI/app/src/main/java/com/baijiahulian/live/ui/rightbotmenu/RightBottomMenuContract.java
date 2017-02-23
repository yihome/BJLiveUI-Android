package com.baijiahulian.live.ui.rightbotmenu;

import com.baijiahulian.live.ui.base.BasePresenter;
import com.baijiahulian.live.ui.base.BaseView;

/**
 * Created by Shubo on 2017/2/15.
 */

interface RightBottomMenuContract {
    interface View extends BaseView<Presenter> {
        void showVideoStatus(boolean isOn);

        void showAudioStatus(boolean isOn);

        void showAVButton();

        void hideAVButton();

        void clearScreen();

        void unClearScreen();
    }

    interface Presenter extends BasePresenter {
        void changeZoom();

        void changeAudio();

        void changeVideo();

        void more(int anchorX, int anchorY);
    }
}
