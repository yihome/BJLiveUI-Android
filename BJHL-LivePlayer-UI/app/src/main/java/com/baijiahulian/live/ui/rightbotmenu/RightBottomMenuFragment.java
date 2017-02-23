package com.baijiahulian.live.ui.rightbotmenu;

import com.baijiahulian.live.ui.base.BaseFragment;
import com.baijiahulian.live.ui.R;

/**
 * Created by Shubo on 2017/2/16.
 */

public class RightBottomMenuFragment extends BaseFragment implements RightBottomMenuContract.View {

    private RightBottomMenuContract.Presenter presenter;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_right_bottom_menu;
    }

    @Override
    public void showVideoStatus(boolean isOn) {

    }

    @Override
    public void showAudioStatus(boolean isOn) {

    }

    @Override
    public void showAVButton() {

    }

    @Override
    public void hideAVButton() {

    }

    @Override
    public void clearScreen() {

    }

    @Override
    public void unClearScreen() {

    }

    @Override
    public void setPresenter(RightBottomMenuContract.Presenter presenter) {
        super.setBasePresenter(presenter);
        this.presenter = presenter;
    }
}
