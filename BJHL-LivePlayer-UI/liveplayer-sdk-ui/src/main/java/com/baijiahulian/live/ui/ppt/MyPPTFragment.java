package com.baijiahulian.live.ui.ppt;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.baijiahulian.livecore.ppt.LPPPTFragment;
import com.baijiahulian.livecore.ppt.whiteboard.LPWhiteBoardView;

/**
 * Created by Shubo on 2017/2/18.
 */

public class MyPPTFragment extends LPPPTFragment implements PPTContract.View {

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        showPPTPageView();
    }

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
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    presenter.clearScreen();
                }
            }
        });

//        super.setOnDoubleTapListener(new LPWhiteBoardView.OnDoubleTapListener() {
//            @Override
//            public void onDoubleTap(LPWhiteBoardView whiteBoardView) {
//                presenter.switchWithMaximum();
//            }
//        });
        mPageTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.showQuickSwitchPPTView(currentPageIndex, maxIndex);
            }
        });
    }

    public LPWhiteBoardView getLPWhiteBoardView(){
        return mWhiteBoardView;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.destroy();
    }
}
