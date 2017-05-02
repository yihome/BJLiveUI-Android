package com.baijiahulian.live.ui.recorderdialog;

import android.os.Bundle;
import android.support.v4.view.GravityCompat;
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
 * 点击自己的图像采集窗口，可以是老师或者学生
 */

public class RecorderDialogFragment extends BaseDialogFragment implements RecorderDialogContract.View {
    private QueryPlus $;
    private RecorderDialogContract.Presenter presenter;

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_recorder;
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
        $.id(R.id.dialog_recorder_fullscreen).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.switchFullscreen();
                dismiss();
            }
        });
        $.id(R.id.dialog_recorder_switch_camera).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.switchCamera();
                dismiss();
            }
        });
        $.id(R.id.dialog_recorder_pretty_filter_switch).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.switchPrettyFilter();
                dismiss();
            }
        });
        $.id(R.id.dialog_recorder_turnoff_camera).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.turnOffCamera();
                dismiss();
            }
        });

        $.id(R.id.dialog_recorder_cancel).clicked(new View.OnClickListener() {
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
    public void setPresenter(RecorderDialogContract.Presenter presenter) {
        super.setBasePresenter(presenter);
        this.presenter = presenter;
    }

    @Override
    public void showFullscreen() {

    }

    @Override
    public void showSwitchCamera() {

    }

    @Override
    public void showSwitchPrettyFilterOn() {
        ((TextView) $.id(R.id.dialog_recorder_pretty_filter_switch).view()).setText(R.string.live_recorder_pretty_filter_on);
    }

    @Override
    public void showSwitchPrettyFilterOff() {
        ((TextView) $.id(R.id.dialog_recorder_pretty_filter_switch).view()).setText(R.string.live_recorder_pretty_filter_off);
    }

    @Override
    public void showTurnOffCamera() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        $ = null;
        presenter = null;
    }
}
