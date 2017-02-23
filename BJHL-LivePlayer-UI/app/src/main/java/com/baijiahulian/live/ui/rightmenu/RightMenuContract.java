package com.baijiahulian.live.ui.rightmenu;

import com.baijiahulian.live.ui.base.BasePresenter;
import com.baijiahulian.live.ui.base.BaseView;

/**
 * Created by Shubo on 2017/2/15.
 */

interface RightMenuContract {

    interface View extends BaseView<Presenter>{
        void showSpeakApplyImage(String imgUrl);

        void showSpeakApplyCount(int count);

        void showDrawingStatus(boolean isEnable);

        void showSpeakApplyCountDown(int countDownTime);

        void showSpeakApplyAgreed();

        void showSpeakApplyDisagreed();

        void showTeacherRightMenu();

        void showStudentRightMenu();
    }

    interface Presenter extends BasePresenter{
        void visitSpeakers();

        void drawing();

        void managePPT();

        void speakApply();

    }
}
