package com.baijiahulian.live.ui.ppt;

import android.content.res.Configuration;
import android.os.Bundle;
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
    public void onStart() {
        super.onStart();
        super.setOnSingleTapListener(new LPWhiteBoardView.OnSingleTapListener() {
            @Override
            public void onSingleTap(LPWhiteBoardView whiteBoardView) {
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    presenter.clearScreen();
                }
            }
        });

        if(presenter.getBackgroundContainer().getChildAt(0) == getView()){
            mWhiteBoardView.setZOrderMediaOverlay(false);
        }else{
            mWhiteBoardView.setZOrderMediaOverlay(true);
            presenter.notifyPPTResumeInSpeakers();
        }

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
    public void setMaxPage(int maxIndex) {
        super.setMaxPage(maxIndex);
        presenter.updateQuickSwitchPPTView(maxIndex);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.destroy();
    }
}
