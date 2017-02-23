package com.baijiahulian.live.ui.topbar;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.baijiahulian.live.ui.base.BaseFragment;
import com.baijiahulian.live.ui.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Shubo on 2017/2/13.
 */

public class TopBarFragment extends BaseFragment implements TopBarContract.View {

    @BindView(R.id.fragment_top_bar_title)
    TextView fragmentTopBarTitle;
    @BindView(R.id.fragment_top_bar_user_count)
    TextView fragmentTopBarUserCount;

    private TopBarContract.Presenter presenter;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_topbar;
    }

    @Override
    public void init(Bundle savedInstanceState) {

    }

    @Override
    public void setPresenter(TopBarContract.Presenter presenter) {
        super.setBasePresenter(presenter);
        this.presenter = presenter;
    }

    @Override
    public void showOnlineUserCount(int count) {
        fragmentTopBarUserCount.setText(getString(R.string.on_line_user_count, count));
    }

    @Override
    public void showRoomTitle(String roomTitle) {
        fragmentTopBarTitle.setText(roomTitle);
    }

    @OnClick({R.id.fragment_top_bar_back, R.id.fragment_top_bar_user_count_container, R.id.fragment_top_bar_share})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fragment_top_bar_back:
                getActivity().finish();
                break;
            case R.id.fragment_top_bar_user_count_container:
                presenter.navigateToUserList();
                break;
            case R.id.fragment_top_bar_share:
                presenter.navigateToShare();
                break;
        }
    }


}
