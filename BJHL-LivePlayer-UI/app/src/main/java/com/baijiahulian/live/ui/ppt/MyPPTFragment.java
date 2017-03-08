package com.baijiahulian.live.ui.ppt;

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
