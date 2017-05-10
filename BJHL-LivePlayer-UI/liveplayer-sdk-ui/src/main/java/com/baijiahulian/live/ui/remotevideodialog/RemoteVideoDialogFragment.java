package com.baijiahulian.live.ui.remotevideodialog;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.baijiahulian.live.ui.R;
import com.baijiahulian.live.ui.base.BaseDialogFragment;
import com.baijiahulian.live.ui.utils.DisplayUtils;
import com.baijiahulian.live.ui.utils.QueryPlus;

/**
 * Created by wangkangfei on 17/4/27.
 */

public class RemoteVideoDialogFragment extends BaseDialogFragment implements RemoteVideoDialogContract.View {
    private QueryPlus $;
    private RemoteVideoDialogContract.Presenter presenter;

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_remote_player;
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
        $.id(R.id.dialog_remote_fullscreen).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.switchFullscreen();
                dismiss();
            }
        });
        $.id(R.id.dialog_remote_turnoff).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.closeVideo();
                dismiss();
            }
        });
        $.id(R.id.dialog_remote_close_speaking).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.closeSpeaking();
                dismiss();
            }
        });
        $.id(R.id.dialog_remote_cancel).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
    public void setPresenter(RemoteVideoDialogContract.Presenter presenter) {
        super.setBasePresenter(presenter);
        this.presenter = presenter;
    }

    @Override
    public void showCurrentRemoteUserName(String name) {
        ((TextView) $.id(R.id.dialog_remote_user_name).view()).setText(name);
    }

    @Override
    public void showCloseSpeaking() {
        $.id(R.id.dialog_remote_close_speaking).visible();
    }

    @Override
    public void hideCloseSpeaking() {
        $.id(R.id.dialog_remote_close_speaking).gone();
    }
}
