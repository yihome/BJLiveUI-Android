package com.baijiahulian.live.ui.loading;

import android.os.Bundle;

import com.baijiahulian.live.ui.base.BaseFragment;
import com.baijiahulian.live.ui.R;
import com.baijiahulian.livecore.LiveSDK;
import com.baijiahulian.livecore.context.LPError;

import static com.baijiahulian.live.ui.utils.Precondition.checkNotNull;

/**
 * Created by Shubo on 2017/2/14.
 */

public class LoadingFragment extends BaseFragment implements LoadingContract.View {

    private LoadingContract.Presenter presenter;

    @Override
    public void setPresenter(LoadingContract.Presenter presenter) {
        super.setBasePresenter(presenter);
        this.presenter = presenter;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_loading;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        checkNotNull(presenter);
        LiveSDK.enterRoom(getActivity(), presenter.getCode(), presenter.getName(), presenter.getLaunchListener());
    }

    @Override
    public void showLoadingSteps(int currentStep, int totalSteps) {

    }

    @Override
    public void showLaunchError(LPError lpError) {
        showToast(lpError.getMessage());
    }
}
