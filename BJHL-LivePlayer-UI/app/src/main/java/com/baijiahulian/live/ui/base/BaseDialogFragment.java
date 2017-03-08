package com.baijiahulian.live.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.baijiahulian.live.ui.R;
import com.baijiahulian.live.ui.utils.Query;

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
