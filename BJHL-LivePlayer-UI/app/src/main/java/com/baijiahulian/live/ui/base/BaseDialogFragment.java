package com.baijiahulian.live.ui.base;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.baijiahulian.live.ui.R;
import com.baijiahulian.live.ui.utils.Query;
import com.baijiahulian.livecore.utils.DisplayUtils;

import static com.baijiahulian.live.ui.utils.Precondition.checkNotNull;

/**
 * Created by Shubo on 2017/2/15.
 */

public abstract class BaseDialogFragment extends DialogFragment {

    private BasePresenter basePresenter;
    protected View contentView;
    private boolean isEditing;
    private Query $;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View baseView = inflater.inflate(R.layout.dialog_base, container);
        contentView = inflater.inflate(getLayoutId(), null);
        $ = Query.with(baseView);
        ((FrameLayout) $.id(R.id.dialog_base_content).view()).addView(contentView);
        init(savedInstanceState, getArguments());
        return baseView;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), getTheme());
        checkNotNull(dialog.getWindow());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            dialog.getWindow().getDecorView().setSystemUiVisibility(getActivity().getWindow().getDecorView().getSystemUiVisibility());
        }
        // 处理dialog沉浸式状态栏
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                //Clear the not focusable flag from the window
                getDialog().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
                //Update the WindowManager with the new attributes (no nicer way I know of to do this)..
                WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
                wm.updateViewLayout(getDialog().getWindow().getDecorView(), getDialog().getWindow().getAttributes());
            }
        });
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        checkNotNull(window);
        window.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getContext(), R.color.live_white)));
        WindowManager.LayoutParams windowParams = window.getAttributes();
        windowParams.dimAmount = 0.50f;
        windowParams.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        windowParams.windowAnimations = R.style.LiveBaseDialogAnim;
        setWindowParams(windowParams);
        window.setAttributes(windowParams);
    }

    protected void setWindowParams(WindowManager.LayoutParams windowParams) {
        int longEdge = Math.max(DisplayUtils.getScreenHeightPixels(getContext()), DisplayUtils.getScreenWidthPixels(getContext()));
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            //竖屏
            windowParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            windowParams.height = longEdge / 2;
        } else {
            //横屏
            windowParams.width = longEdge / 2;
            windowParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        }
        windowParams.gravity = Gravity.BOTTOM | GravityCompat.END;
    }

    protected abstract int getLayoutId();

    protected abstract void init(Bundle savedInstanceState, Bundle arguments);

    public void setBasePresenter(BasePresenter presenter) {
        basePresenter = presenter;
    }

    public BaseDialogFragment title(String title) {
        $.id(R.id.dialog_base_title).text(title);
        return this;
    }

    public BaseDialogFragment editable(boolean editable) {
        $.id(R.id.dialog_base_edit).visibility(editable ? View.VISIBLE : View.GONE);
        $.id(R.id.dialog_base_edit).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEditing) {
                    $.id(R.id.dialog_base_edit).text(getString(R.string.live_cancel));
                    enableEdit();
                } else {
                    $.id(R.id.dialog_base_edit).text(getString(R.string.live_edit));
                    disableEdit();
                }
                isEditing = !isEditing;
            }
        });
        return this;
    }

    protected void hideTitleBar() {
        $.id(R.id.dialog_base_title_container).gone();
    }

    protected void showTitleBar() {
        $.id(R.id.dialog_base_title_container).visible();
    }

    protected boolean isEditing() {
        return isEditing;
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        return super.onCreateAnimation(transit, enter, nextAnim);
    }

    protected void disableEdit() {

    }

    protected void enableEdit() {

    }

    protected void showToast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        basePresenter.subscribe();
    }

    @Override
    public void onPause() {
        super.onPause();
        basePresenter.unSubscribe();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        basePresenter.destroy();
        basePresenter = null;
        $ = null;
    }
}
