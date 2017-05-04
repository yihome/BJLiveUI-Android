package com.baijiahulian.live.ui.righttopmenu;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baijiahulian.live.ui.R;
import com.baijiahulian.live.ui.base.BaseFragment;

/**
 * Created by wangkangfei on 17/5/3.
 */

public class RightTopMenuFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_right_top_menu, container, false);
    }
}
