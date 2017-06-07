package com.baijiahulian.live.ui.topbar;

import android.os.Bundle;
import android.view.View;

import com.baijiahulian.live.ui.R;
import com.baijiahulian.live.ui.base.BaseFragment;

/**
 * Created by Shubo on 2017/2/13.
 */

public class TopBarFragment extends BaseFragment implements TopBarContract.View {

    private TopBarContract.Presenter presenter;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_topbar;
    }

    @Override
    public void init(Bundle savedInstanceState) {

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
    public void showHideShare(boolean show) {
        if (show) $.id(R.id.fragment_top_bar_share).visible();
        else $.id(R.id.fragment_top_bar_share).gone();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        $ = null;
    }
}
