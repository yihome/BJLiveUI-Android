package com.baijiahulian.live.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import butterknife.ButterKnife;

/**
 * Created by Shubo on 2017/2/13.
 */

public abstract class BaseFragment extends Fragment {

    protected View view;
    private BasePresenter basePresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(getLayoutId(), container, false);
        ButterKnife.bind(this, view);
        init(savedInstanceState);
        return view;
    }

    public abstract int getLayoutId();

    public void init(Bundle savedInstanceState){

    };

    public void setBasePresenter(BasePresenter presenter) {
        basePresenter = presenter;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    protected void showToast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
//        hideShowLifeCycle(hidden);
    }

    protected void hideShowLifeCycle(boolean hidden) {
        if (hidden) onPause();
        else onResume();
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
    }
}
