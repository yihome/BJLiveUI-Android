package com.baijiahulian.live.ui.topbar;

import android.os.Bundle;
import android.view.View;

import com.baijiahulian.live.ui.R;
import com.baijiahulian.live.ui.base.BaseFragment;
import com.baijiahulian.live.ui.utils.Query;

/**
 * Created by Shubo on 2017/2/13.
 */

public class TopBarFragment extends BaseFragment implements TopBarContract.View {

    private Query $;

    private TopBarContract.Presenter presenter;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_topbar;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        $ = Query.with(view);

        $.id(R.id.fragment_top_bar_user_count).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.navigateToUserList();
            }
        });
        $.id(R.id.fragment_top_bar_back).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        $.id(R.id.fragment_top_bar_share).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.navigateToShare();
            }
        });
    }

    @Override
    public void setPresenter(TopBarContract.Presenter presenter) {
        super.setBasePresenter(presenter);
        this.presenter = presenter;
    }

    @Override
    public void showOnlineUserCount(int count) {
        $.id(R.id.fragment_top_bar_user_count).text(getString(R.string.on_line_user_count, count));
    }

    @Override
    public void showRoomTitle(String roomTitle) {
        $.id(R.id.fragment_top_bar_title).text(roomTitle);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        $ = null;
    }
}
