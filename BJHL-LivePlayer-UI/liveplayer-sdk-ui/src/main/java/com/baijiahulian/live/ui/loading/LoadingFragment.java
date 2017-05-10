package com.baijiahulian.live.ui.loading;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ProgressBar;

import com.baijiahulian.live.ui.R;
import com.baijiahulian.live.ui.base.BaseFragment;
import com.baijiahulian.livecore.LiveSDK;
import com.baijiahulian.livecore.context.LPError;
import com.baijiahulian.livecore.context.LiveRoom;

import static com.baijiahulian.live.ui.utils.Precondition.checkNotNull;

/**
 * Created by Shubo on 2017/2/14.
 */

public class LoadingFragment extends BaseFragment implements LoadingContract.View {

    private LoadingContract.Presenter presenter;
    private ProgressBar progressBar;

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
        progressBar = (ProgressBar) $.id(R.id.fragment_loading_pb).view();
        if (!presenter.isReconnect()) {
            LiveRoom room = LiveSDK.enterRoom(getActivity(), presenter.getCode(), presenter.getName(), presenter.getLaunchListener());
            presenter.setLiveRoom(room);
        }
    }

    private ObjectAnimator animator;

    @Override
    public void showLoadingSteps(int currentStep, int totalSteps) {
        int start = progressBar.getProgress();
        int end = currentStep * 100 / totalSteps;
        if (animator != null && animator.isRunning()) {
            animator.cancel();
        }
        animator = ObjectAnimator.ofInt(progressBar, "progress", start, end);
        animator.setDuration(400);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.start();
    }

    @Override
    public void showLaunchError(LPError lpError) {
        showToast(lpError.getMessage());
    }
}
