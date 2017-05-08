package com.baijiahulian.live.ui.righttopmenu;


import com.baijiahulian.live.ui.R;
import com.baijiahulian.live.ui.base.BaseFragment;

/**
 * Created by wangkangfei on 17/5/3.
 */

public class RightTopMenuFragment extends BaseFragment implements RightTopMenuContract.View {

    @Override
    public int getLayoutId() {
        return R.layout.fragment_right_top_menu;
    }

    @Override
    public void setPresenter(RightTopMenuContract.Presenter presenter) {
        super.setBasePresenter(presenter);
    }
}
