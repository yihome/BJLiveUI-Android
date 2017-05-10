package com.baijiahulian.live.ui.error;

import android.os.Bundle;
import android.view.View;

import com.baijiahulian.live.ui.R;
import com.baijiahulian.live.ui.activity.LiveRoomRouterListener;
import com.baijiahulian.live.ui.base.BaseFragment;

/**
 * Created by Shubo on 2017/5/10.
 */

public class ErrorFragment extends BaseFragment{

    private LiveRoomRouterListener routerListener;

    public static ErrorFragment newInstance(String title, String content) {

        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("content", content);

        ErrorFragment fragment = new ErrorFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_error;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        $.id(R.id.fragment_error_back).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        $.id(R.id.fragment_error_title).text(getArguments().getString("title"));
        $.id(R.id.fragment_error_reason).text(getArguments().getString("content"));
        $.id(R.id.fragment_error_retry).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(routerListener != null){
                    routerListener.doReconnectServer();
                }
            }
        });
    }

    public void setRouterListener(LiveRoomRouterListener routerListener) {
        this.routerListener = routerListener;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        routerListener = null;
    }
}
