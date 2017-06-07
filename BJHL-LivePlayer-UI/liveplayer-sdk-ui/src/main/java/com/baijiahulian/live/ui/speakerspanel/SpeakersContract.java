package com.baijiahulian.live.ui.speakerspanel;

import com.baijiahulian.live.ui.base.BasePresenter;
import com.baijiahulian.live.ui.base.BaseView;

/**
 * Created by Shubo on 2017/6/5.
 */

interface SpeakersContract {

    interface View extends BaseView<Presenter> {
        void notifyItemChanged(int position);

        void notifyItemInserted(int position);

        void notifyItemDeleted(int position);

        void notifyItemMoved(int fromPosition, int toPosition);
    }

    interface Presenter extends BasePresenter {
        Object getItem(int position);

        int getCount();

        void agreeSpeakApply(int position);

        void disagreeSpeakApply(int position);

//        void play
    }
}
