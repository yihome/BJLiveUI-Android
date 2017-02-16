package com.baijiahulian.live.ui.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.baijiahulian.live.ui.R;
import com.baijiahulian.live.ui.leftmenu.LeftMenuFragment;
import com.baijiahulian.live.ui.leftmenu.LeftMenuPresenter;
import com.baijiahulian.live.ui.loading.LoadingFragment;
import com.baijiahulian.live.ui.loading.LoadingPresenter;
import com.baijiahulian.live.ui.topbar.TopBarFragment;
import com.baijiahulian.live.ui.topbar.TopBarPresenter;
import com.baijiahulian.livecore.LiveSDK;
import com.baijiahulian.livecore.context.LPConstants;
import com.baijiahulian.livecore.context.LiveRoom;
import com.baijiahulian.livecore.ppt.LPPPTFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.baijiahulian.live.ui.utils.Precondition.checkNotNull;

public class LiveRoomActivity extends LiveRoomBaseActivity implements LiveRoomRouterListener {

    @BindView(R.id.activity_live_room_ppt_container)
    FrameLayout flPPT;
    @BindView(R.id.activity_live_room_recorder_container)
    FrameLayout flRecorder;
    @BindView(R.id.activity_live_room_player_container)
    FrameLayout flPlayer;
    @BindView(R.id.activity_live_room_chat)
    FrameLayout flChat;
    @BindView(R.id.activity_live_room_top)
    FrameLayout flTop;
    @BindView(R.id.activity_live_room_bottom_left)
    FrameLayout flLeft;
    @BindView(R.id.activity_live_room_right)
    FrameLayout flRight;
    @BindView(R.id.activity_live_room_bottom_right)
    FrameLayout flBottomRight;
    @BindView(R.id.activity_live_room_loading)
    FrameLayout flLoading;

    private LiveRoom liveRoom;

    private LoadingFragment loadingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_room);
        ButterKnife.bind(this);

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            onConfigurationChanged(getResources().getConfiguration());
        }

        String code = getIntent().getStringExtra("code");
        String name = getIntent().getStringExtra("name");

        LiveSDK.init("", LPConstants.LPDeployType.Test);

        code = "kjs0en";
        name = "Shubo";

        loadingFragment = new LoadingFragment();
        LoadingPresenter loadingPresenter = new LoadingPresenter(loadingFragment, code, name);
        loadingPresenter.setRouter(this);
        loadingFragment.setPresenter(loadingPresenter);
        addFragment(R.id.activity_live_room_loading, loadingFragment);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        onPPTContainerConfigurationChanged(newConfig);
        onRecorderContainerConfigurationChanged(newConfig);
        onPlayerContainerConfigurationChanged(newConfig);
    }

    private void onPlayerContainerConfigurationChanged(Configuration newConfig) {
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) flPlayer.getLayoutParams();
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            lp.removeRule(RelativeLayout.BELOW);
            lp.addRule(RelativeLayout.BELOW, R.id.activity_live_room_top);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            lp.removeRule(RelativeLayout.BELOW);
            lp.addRule(RelativeLayout.BELOW, R.id.activity_live_room_ppt_container);
        }
        flPlayer.setLayoutParams(lp);
    }

    private void onRecorderContainerConfigurationChanged(Configuration newConfig) {
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) flRecorder.getLayoutParams();
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            lp.removeRule(RelativeLayout.BELOW);
            lp.addRule(RelativeLayout.BELOW, R.id.activity_live_room_top);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            lp.removeRule(RelativeLayout.BELOW);
            lp.addRule(RelativeLayout.BELOW, R.id.activity_live_room_ppt_container);
        }
        flRecorder.setLayoutParams(lp);
    }

    private void onPPTContainerConfigurationChanged(Configuration newConfig) {
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) flPPT.getLayoutParams();
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            lp.removeRule(RelativeLayout.ABOVE);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            lp.addRule(RelativeLayout.ABOVE, R.id.activity_live_room_center_anchor);
        }
        flPPT.setLayoutParams(lp);
    }

    @Override
    public LiveRoom getLiveRoom() {
        checkNotNull(liveRoom);
        return liveRoom;
    }

    @Override
    public void setLiveRoom(LiveRoom liveRoom) {
        this.liveRoom = liveRoom;
    }

    @Override
    public void navigateToMain() {
        LPPPTFragment lppptFragment = new LPPPTFragment();
        lppptFragment.setLiveRoom(getLiveRoom());
        addFragment(R.id.activity_live_room_ppt_container, lppptFragment);

        TopBarFragment topBarFragment = new TopBarFragment();
        TopBarPresenter topBarPresenter = new TopBarPresenter(topBarFragment);
        topBarPresenter.setRouter(this);
        topBarFragment.setPresenter(topBarPresenter);
        addFragment(R.id.activity_live_room_top, topBarFragment);

        LeftMenuFragment leftMenuFragment = new LeftMenuFragment();
        LeftMenuPresenter leftMenuPresenter = new LeftMenuPresenter(leftMenuFragment);
        leftMenuPresenter.setRouter(this);
        leftMenuFragment.setPresenter(leftMenuPresenter);
        addFragment(R.id.activity_live_room_bottom_left, leftMenuFragment);

        removeFragment(loadingFragment);
        flLoading.setVisibility(View.GONE);
    }

    @Override
    public void clearScreen() {

    }

    @Override
    public void unClearScreen() {

    }

    @Override
    public void navigateToMessageInput() {

    }
}
