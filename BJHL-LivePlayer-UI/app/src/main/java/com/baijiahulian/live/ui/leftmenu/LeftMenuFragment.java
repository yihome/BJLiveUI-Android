package com.baijiahulian.live.ui.leftmenu;

import android.view.View;
import android.widget.ImageView;

import com.baijiahulian.live.ui.BaseFragment;
import com.baijiahulian.live.ui.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Shubo on 2017/2/15.
 */

public class LeftMenuFragment extends BaseFragment implements LeftMenuContract.View {

    LeftMenuContract.Presenter presenter;

    @BindView(R.id.fragment_left_menu_clear_screen)
    ImageView fragmentLeftMenuClearScreen;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_leftmenu;
    }

    @Override
    public void notifyClearScreenChanged(boolean isCleared) {
        if (isCleared) fragmentLeftMenuClearScreen.setImageResource(R.drawable.live_ic_clear_on);
        else fragmentLeftMenuClearScreen.setImageResource(R.drawable.live_ic_clear);
    }

    @Override
    public void setPresenter(LeftMenuContract.Presenter presenter) {
        super.setBasePresenter(presenter);
        this.presenter = presenter;
    }

    @OnClick({R.id.fragment_left_menu_clear_screen, R.id.fragment_left_menu_send_message})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fragment_left_menu_clear_screen:
                presenter.clearScreen();
                break;
            case R.id.fragment_left_menu_send_message:
                presenter.showMessageInput();
                break;
        }
    }
}
