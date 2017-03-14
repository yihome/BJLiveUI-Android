package com.baijiahulian.live.ui.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.baijiahulian.live.ui.R;
import com.baijiahulian.live.ui.base.BasePresenter;
import com.baijiahulian.live.ui.base.BaseView;
import com.baijiahulian.live.ui.chat.ChatFragment;
import com.baijiahulian.live.ui.chat.ChatPresenter;
import com.baijiahulian.live.ui.leftmenu.LeftMenuFragment;
import com.baijiahulian.live.ui.leftmenu.LeftMenuPresenter;
import com.baijiahulian.live.ui.loading.LoadingFragment;
import com.baijiahulian.live.ui.loading.LoadingPresenter;
import com.baijiahulian.live.ui.ppt.MyPPTFragment;
import com.baijiahulian.live.ui.ppt.PPTPresenter;
import com.baijiahulian.live.ui.rightbotmenu.RightBottomMenuFragment;
import com.baijiahulian.live.ui.rightbotmenu.RightBottomMenuPresenter;
import com.baijiahulian.live.ui.rightmenu.RightMenuFragment;
import com.baijiahulian.live.ui.rightmenu.RightMenuPresenter;
import com.baijiahulian.live.ui.topbar.TopBarFragment;
import com.baijiahulian.live.ui.topbar.TopBarPresenter;
import com.baijiahulian.live.ui.videoplayer.VideoPlayerFragment;
import com.baijiahulian.live.ui.videoplayer.VideoPlayerPresenter;
import com.baijiahulian.live.ui.videorecorder.VideoRecorderFragment;
import com.baijiahulian.live.ui.videorecorder.VideoRecorderPresenter;
import com.baijiahulian.livecore.context.LiveRoom;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.baijiahulian.live.ui.utils.Precondition.checkNotNull;

public class LiveRoomActivity extends LiveRoomBaseActivity implements LiveRoomRouterListener {

    @BindView(R.id.activity_live_room_background_container)
    FrameLayout flBackground;
    @BindView(R.id.activity_live_room_foreground_left_container)
    FrameLayout flForegroundLeft;
    @BindView(R.id.activity_live_room_foreground_right_container)
    FrameLayout flForegroundRight;
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
    private TopBarFragment topBarFragment;

    private MyPPTFragment lppptFragment;
    private VideoRecorderFragment recorderFragment;
    private ChatFragment chatFragment;
    private RightBottomMenuFragment rightBottomMenuFragment;
    private LeftMenuFragment leftMenuFragment;
    private RightMenuFragment rightMenuFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_room);
        ButterKnife.bind(this);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            onConfigurationChanged(getResources().getConfiguration());
        }

        String code = getIntent().getStringExtra("code");
        String name = getIntent().getStringExtra("name");

//        LiveSDK.init(LPConstants.LPDeployType.Test);

        code = "epznlk";
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
        onBackgroundContainerConfigurationChanged(newConfig);
        onForegroundLeftContainerConfigurationChanged(newConfig);
        onForegroundRightContainerConfigurationChanged(newConfig);
    }

    private void onForegroundRightContainerConfigurationChanged(Configuration newConfig) {
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) flForegroundRight.getLayoutParams();
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            lp.removeRule(RelativeLayout.BELOW);
            lp.addRule(RelativeLayout.BELOW, R.id.activity_live_room_top);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            lp.removeRule(RelativeLayout.BELOW);
            lp.addRule(RelativeLayout.BELOW, R.id.activity_live_room_background_container);
        }
        flForegroundRight.setLayoutParams(lp);
    }

    private void onForegroundLeftContainerConfigurationChanged(Configuration newConfig) {
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) flForegroundLeft.getLayoutParams();
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            lp.removeRule(RelativeLayout.BELOW);
            lp.addRule(RelativeLayout.BELOW, R.id.activity_live_room_top);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            lp.removeRule(RelativeLayout.BELOW);
            lp.addRule(RelativeLayout.BELOW, R.id.activity_live_room_background_container);
        }
        flForegroundLeft.setLayoutParams(lp);
    }

    private void onBackgroundContainerConfigurationChanged(Configuration newConfig) {
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) flBackground.getLayoutParams();
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            lp.removeRule(RelativeLayout.ABOVE);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            lp.addRule(RelativeLayout.ABOVE, R.id.activity_live_room_center_anchor);
        }
        flBackground.setLayoutParams(lp);
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

        lppptFragment = new MyPPTFragment();
        lppptFragment.setLiveRoom(getLiveRoom());
        bindVP(lppptFragment, new PPTPresenter(lppptFragment));
        addFragment(R.id.activity_live_room_background_container, lppptFragment);

        topBarFragment = new TopBarFragment();
        bindVP(topBarFragment, new TopBarPresenter(topBarFragment));
        addFragment(R.id.activity_live_room_top, topBarFragment);

        leftMenuFragment = new LeftMenuFragment();
        bindVP(leftMenuFragment, new LeftMenuPresenter(leftMenuFragment));
        addFragment(R.id.activity_live_room_bottom_left, leftMenuFragment);

        rightMenuFragment = new RightMenuFragment();
        bindVP(rightMenuFragment, new RightMenuPresenter(rightMenuFragment));
        addFragment(R.id.activity_live_room_right, rightMenuFragment);

        rightBottomMenuFragment = new RightBottomMenuFragment();
        bindVP(rightBottomMenuFragment, new RightBottomMenuPresenter(rightBottomMenuFragment));
        addFragment(R.id.activity_live_room_bottom_right, rightBottomMenuFragment);

        recorderFragment = new VideoRecorderFragment();
        bindVP(recorderFragment, new VideoRecorderPresenter(recorderFragment));
        addFragment(R.id.activity_live_room_foreground_left_container, recorderFragment);

        chatFragment = new ChatFragment();
        bindVP(chatFragment, new ChatPresenter(chatFragment));
        addFragment(R.id.activity_live_room_chat, chatFragment);

        playerFragment = new VideoPlayerFragment();
        playerPresenter = new VideoPlayerPresenter(playerFragment);
        bindVP(playerFragment, playerPresenter);
        addFragment(R.id.activity_live_room_foreground_right_container, playerFragment);

        // might delay 500ms to process
        removeFragment(loadingFragment);
        flLoading.setVisibility(View.GONE);
    }

    private VideoPlayerFragment playerFragment;
    private VideoPlayerPresenter playerPresenter;

    @Override
    public void clearScreen() {
        chatFragment.clearScreen();
        rightBottomMenuFragment.clearScreen();
        hideFragment(topBarFragment);
        hideFragment(rightMenuFragment);
    }

    @Override
    public void unClearScreen() {
        chatFragment.unClearScreen();
        rightBottomMenuFragment.unClearScreen();
        showFragment(topBarFragment);
        showFragment(rightMenuFragment);
    }

    @Override
    public void navigateToMessageInput() {

    }

    @Override
    public void navigateToPPTDrawing() {
        checkNotNull(lppptFragment);
        lppptFragment.changePPTCanvasMode();
    }

    @Override
    public void navigateToSpeakers() {

    }

    @Override
    public void navigateToPPTWareHouse() {

    }

    @Override
    public void disableSpeakerMode() {

    }

    @Override
    public void enableSpeakerMode() {

    }

    @Override
    public void maximiseRecorderView() {
        View max = flBackground.getChildAt(0);
        if (recorderFragment.getView() == max) return;
        boolean isPPT = max == lppptFragment.getView();
        if (isPPT)
            lppptFragment.onPause();
//        getLiveRoom().getRecorder().detachVideo();
        switchView(recorderFragment.getView(), max);
        if (isPPT)
            lppptFragment.onResume();
//        getLiveRoom().getRecorder().attachVideo();
        liveRoom.getRecorder().invalidVideo();
    }

    @Override
    public void maximisePlayerView() {
        View max = flBackground.getChildAt(0);
        if(playerFragment.getView() == max) return;
        boolean isPPT = max == lppptFragment.getView();
        if (isPPT)
            lppptFragment.onPause();
//        else
//            getLiveRoom().getRecorder().detachVideo();
        switchView(playerFragment.getView(), max);
        if (isPPT)
            lppptFragment.onResume();
//        if(max == recorderFragment.getView())
//            getLiveRoom().getRecorder().attachVideo();
            liveRoom.getRecorder().invalidVideo();
    }

    @Override
    public void maximisePPTView() {
        View max = flBackground.getChildAt(0);
        if(lppptFragment.getView() == max) return;
        lppptFragment.onPause();
//        if(max == recorderFragment.getView())
//            getLiveRoom().getRecorder().detachVideo();
        switchView(lppptFragment.getView(), max);
        lppptFragment.onResume();
//        if(max == recorderFragment.getView())
//            getLiveRoom().getRecorder().attachVideo();
            liveRoom.getRecorder().invalidVideo();
    }

    private void switchView(View view1, View view2) {
        FrameLayout fl1 = (FrameLayout) view1.getParent();
        FrameLayout fl2 = (FrameLayout) view2.getParent();
        fl1.removeView(view1);
        fl2.removeView(view2);
        fl1.addView(view2);
        fl2.addView(view1);
    }

    private <V extends BaseView, P extends BasePresenter> void bindVP(V view, P presenter) {
        presenter.setRouter(this);
        view.setPresenter(presenter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getLiveRoom().quitRoom();
    }
}
