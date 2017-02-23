package com.baijiahulian.live.ui.ppt;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;

import com.baijiahulian.livecore.ppt.LPPPTFragment;

/**
 * Created by Shubo on 2017/2/18.
 */

public class MyPPTFragment extends LPPPTFragment implements PPTContract.View {

    private boolean isMaximised = true;
    private PPTContract.Presenter presenter;

    @Override
    public boolean isMaximised() {
        return isMaximised;
    }

    @Override
    public void setIsDisplayMaximised(boolean maximised) {
        this.isMaximised = maximised;
    }

    @Override
    public void setPresenter(PPTContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
