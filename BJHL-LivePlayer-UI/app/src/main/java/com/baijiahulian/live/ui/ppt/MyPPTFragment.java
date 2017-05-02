package com.baijiahulian.live.ui.ppt;

import android.view.GestureDetector;
import android.view.MotionEvent;

import com.baijiahulian.livecore.ppt.LPPPTFragment;
import com.baijiahulian.livecore.ppt.whiteboard.LPWhiteBoardView;

/**
 * Created by Shubo on 2017/2/18.
 */

public class MyPPTFragment extends LPPPTFragment implements PPTContract.View {

    private PPTContract.Presenter presenter;

    @Override
    public void setPresenter(PPTContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onResume() {
        super.onResume();
        super.setOnSingleTapListener(new LPWhiteBoardView.OnSingleTapListener() {
            @Override
            public void onSingleTap(LPWhiteBoardView whiteBoardView) {
                presenter.popUpPPTDialog();
            }
        });
        super.setOnDoubleTapListener(new LPWhiteBoardView.OnDoubleTapListener() {
            @Override
            public void onDoubleTap(LPWhiteBoardView whiteBoardView) {
                presenter.switchWithMaximum();
            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.destroy();
    }
}
