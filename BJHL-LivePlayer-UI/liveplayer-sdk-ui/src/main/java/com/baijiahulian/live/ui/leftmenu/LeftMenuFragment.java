package com.baijiahulian.live.ui.leftmenu;

import android.os.Bundle;
import android.view.View;

import com.baijiahulian.live.ui.R;
import com.baijiahulian.live.ui.base.BaseFragment;

/**
 * Created by Shubo on 2017/2/15.
 */

public class LeftMenuFragment extends BaseFragment implements LeftMenuContract.View {

    LeftMenuContract.Presenter presenter;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_leftmenu;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        $.id(R.id.fragment_left_menu_clear_screen).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.clearScreen();
            }
        });
        $.id(R.id.fragment_left_menu_send_message).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.showMessageInput();
            }
        });
    }

    @Override
    public void notifyClearScreenChanged(boolean isCleared) {
        if (isCleared) $.id(R.id.fragment_left_menu_clear_screen).image(R.drawable.live_ic_clear_on);
        else $.id(R.id.fragment_left_menu_clear_screen).image(R.drawable.live_ic_clear);
    }

    @Override
    public void setPresenter(LeftMenuContract.Presenter presenter) {
        super.setBasePresenter(presenter);
        this.presenter = presenter;
    }
}