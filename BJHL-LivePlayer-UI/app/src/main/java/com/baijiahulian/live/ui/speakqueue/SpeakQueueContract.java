package com.baijiahulian.live.ui.speakqueue;

import com.baijiahulian.live.ui.base.BasePresenter;
import com.baijiahulian.live.ui.base.BaseView;

/**
 * Created by Shubo on 2017/4/11.
 */

public interface SpeakQueueContract {

    interface View extends BaseView<Presenter> {
        void notifyItemChanged(int position);

        void notifyItemInserted(int position);

        void notifyItemDeleted(int position);
    }

    interface Presenter extends BasePresenter {
        void agreeSpeakApply(int position);

        void disagreeSpeakApply(int position);

        void closeSpeaking(int position);

        void openCamera(int position);

        void closeCamera(int position);
    }
}
