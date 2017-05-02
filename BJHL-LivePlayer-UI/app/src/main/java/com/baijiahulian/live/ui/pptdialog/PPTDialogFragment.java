package com.baijiahulian.live.ui.pptdialog;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.baijiahulian.live.ui.R;
import com.baijiahulian.live.ui.base.BaseDialogFragment;
import com.baijiahulian.live.ui.utils.DisplayUtils;
import com.baijiahulian.live.ui.utils.QueryPlus;

/**
 * Created by wangkangfei on 17/4/27.
 */

public class PPTDialogFragment extends BaseDialogFragment implements PPTDialogContract.View {
    private QueryPlus $;
    private PPTDialogContract.Presenter presenter;

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_ppt;
    }

    @Override
    public void onStart() {
        contentBackgroundColor(android.R.color.transparent);
        super.onStart();
    }

    @Override
    protected void init(Bundle savedInstanceState, Bundle arguments) {
        hideTitleBar();
        $ = QueryPlus.with(contentView);
        $.id(R.id.dialog_ppt_fullscreen).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.switchFullscreen();
                dismiss();
            }
        });
        $.id(R.id.dialog_ppt_manage).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.managePPT();
                dismiss();
            }
        });
    }

    @Override
    protected void setWindowParams(WindowManager.LayoutParams windowParams) {
        int screenWidth = DisplayUtils.getScreenWidthPixels(getContext());
        int screenHeight = DisplayUtils.getScreenHeightPixels(getContext());
        windowParams.width = Math.min(screenHeight, screenWidth);
        windowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        windowParams.gravity = Gravity.BOTTOM | Gravity.CENTER;
        windowParams.x = 0;
        windowParams.y = 0;

        windowParams.windowAnimations = R.style.LiveBaseSendMsgDialogAnim;
    }

    @Override
    public void setPresenter(PPTDialogContract.Presenter presenter) {
        super.setBasePresenter(presenter);
        this.presenter = presenter;
    }

    @Override
    public void showFullScreen() {

    }

    @Override
    public void showManagePPT() {
        $.id(R.id.dialog_ppt_manage).visible();
    }

    @Override
    public void hideManagePPT() {
        $.id(R.id.dialog_ppt_manage).gone();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        $ = null;
        presenter = null;
    }
}
