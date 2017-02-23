package com.baijiahulian.live.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.baijiahulian.common.utils.DisplayUtils;
import com.baijiahulian.live.ui.R;
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
import com.baijiahulian.live.ui.videorecorder.RecorderFragment;
import com.baijiahulian.live.ui.videorecorder.RecorderPresenter;
import com.baijiahulian.livecore.LiveSDK;
import com.baijiahulian.livecore.context.LPConstants;
import com.baijiahulian.livecore.context.LiveRoom;

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

    private MyPPTFragment lppptFragment;

    private RecorderFragment recorderFragment;

    private boolean isSwitchAnimationRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_room);
        ButterKnife.bind(this);

        onInitConfiguration(getResources().getConfiguration());

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

    private void onInitConfiguration(Configuration configuration) {
        onConfigurationMaximise(configuration, flPPT);
        onConfigurationNormal(configuration, flRecorder);
        onConfigurationNormal(configuration, flPlayer);
    }

    private void onConfigurationNormal(Configuration configuration, FrameLayout layout) {
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) layout.getLayoutParams();
        if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            lp.setMargins(lp.leftMargin, DisplayUtils.dip2px(this, 52), lp.rightMargin, lp.bottomMargin);
        } else if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            int screenWidth = DisplayUtils.getScreenWidthPixels(this);
            int screenHeight = DisplayUtils.getScreenHeightPixels(this);
            int pptHeight = (int) (screenWidth * (screenWidth / (float) screenHeight));
            lp.setMargins(lp.leftMargin, pptHeight + DisplayUtils.dip2px(this, 10), lp.rightMargin, lp.bottomMargin);
        }
        layout.setLayoutParams(lp);
    }

    private void onConfigurationMaximise(Configuration configuration, FrameLayout layout) {
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) layout.getLayoutParams();
        lp.setMargins(0, 0, 0, 0);
        if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            lp.width = RelativeLayout.LayoutParams.MATCH_PARENT;
            lp.height = RelativeLayout.LayoutParams.MATCH_PARENT;
        } else if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            lp.width = RelativeLayout.LayoutParams.MATCH_PARENT;
            int screenWidth = DisplayUtils.getScreenWidthPixels(this);
            int screenHeight = DisplayUtils.getScreenHeightPixels(this);
            lp.height = (int) (screenWidth * (screenWidth / (float) screenHeight));
        }
        layout.setLayoutParams(lp);
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
//            lp.removeRule(RelativeLayout.BELOW);
//            lp.addRule(RelativeLayout.BELOW, R.id.activity_live_room_top);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
//            lp.removeRule(RelativeLayout.BELOW);
//            lp.addRule(RelativeLayout.BELOW, R.id.activity_live_room_ppt_container);
        }
        flPlayer.setLayoutParams(lp);
    }

    private void onRecorderContainerConfigurationChanged(Configuration newConfig) {
        if (recorderFragment == null) return;
        if (recorderFragment.isMaximised()) {
            onConfigurationMaximise(newConfig, flRecorder);
        } else {
            onConfigurationNormal(newConfig, flRecorder);
        }
    }

    private void onPPTContainerConfigurationChanged(Configuration newConfig) {
        if (lppptFragment == null) return;
        if (lppptFragment.isMaximised()) {
            onConfigurationMaximise(newConfig, flPPT);
        } else {
            onConfigurationNormal(newConfig, flPPT);
        }
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
        final PPTPresenter pptPresenter = new PPTPresenter(lppptFragment);
        pptPresenter.setRouter(this);
        lppptFragment.setPresenter(pptPresenter);
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

        RightMenuFragment rightMenuFragment = new RightMenuFragment();
        RightMenuPresenter rightMenuPresenter = new RightMenuPresenter(rightMenuFragment);
        rightMenuPresenter.setRouter(this);
        rightMenuFragment.setPresenter(rightMenuPresenter);
        addFragment(R.id.activity_live_room_right, rightMenuFragment);

        RightBottomMenuFragment rightBottomMenuFragment = new RightBottomMenuFragment();
        RightBottomMenuPresenter rightBottomMenuPresenter = new RightBottomMenuPresenter(rightBottomMenuFragment);
        rightBottomMenuPresenter.setRouter(this);
        rightBottomMenuFragment.setPresenter(rightBottomMenuPresenter);
        addFragment(R.id.activity_live_room_bottom_right, rightBottomMenuFragment);

        recorderFragment = new RecorderFragment();
        RecorderPresenter recorderPresenter = new RecorderPresenter(recorderFragment);
        recorderPresenter.setRouter(this);
        recorderFragment.setPresenter(recorderPresenter);
        addFragment(R.id.activity_live_room_recorder_container, recorderFragment);

        // might delay 500ms to process
        removeFragment(loadingFragment);
        flLoading.setVisibility(View.GONE);

//        Observable.timer(5, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new LPErrorPrintSubscriber<Long>() {
//                    @Override
//                    public void call(Long aLong) {
//                        removeFragment(recorderFragment);
//                        addFragment(R.id.activity_live_room_player_container, recorderFragment);
//                        flPlayer.setVisibility(View.VISIBLE);
//                    }
//                });
    }

    private void switchContainer(final ViewGroup toZoomOut, final ViewGroup toZoomIn) {
        if (isSwitchAnimationRunning) {
            return;
        }
        isSwitchAnimationRunning = true;
        Rect zoomOutRect = new Rect();
        toZoomOut.getGlobalVisibleRect(zoomOutRect);

        Rect zoomInRect = new Rect();
        toZoomIn.getGlobalVisibleRect(zoomInRect);

        RelativeLayout.LayoutParams lp = ((RelativeLayout.LayoutParams) toZoomOut.getLayoutParams());
        lp.removeRule(RelativeLayout.BELOW);
        lp.setMargins(lp.leftMargin, zoomOutRect.top - zoomInRect.top, lp.rightMargin, lp.bottomMargin);

        RelativeLayout.LayoutParams lp1 = ((RelativeLayout.LayoutParams) toZoomIn.getLayoutParams());
        lp1.removeRule(RelativeLayout.ABOVE);

        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(toZoomOut, "scaleX", 1f, (zoomInRect.right - zoomInRect.left) / (float) (zoomOutRect.right - zoomOutRect.left));
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(toZoomOut, "scaleY", 1f, (zoomInRect.bottom - zoomInRect.top) / (float) (zoomOutRect.bottom - zoomOutRect.top));
        ObjectAnimator translationX = ObjectAnimator.ofFloat(toZoomOut, "translationX", (zoomInRect.right - zoomInRect.left) / 2 - (zoomOutRect.right - zoomOutRect.left) / 2 - ((RelativeLayout.LayoutParams) toZoomOut.getLayoutParams()).leftMargin);
        ObjectAnimator translationY = ObjectAnimator.ofFloat(toZoomOut, "translationY", (zoomInRect.bottom - zoomInRect.top) / 2 - (zoomOutRect.bottom - zoomOutRect.top) / 2 - ((RelativeLayout.LayoutParams) toZoomOut.getLayoutParams()).topMargin);

        ObjectAnimator scaleX1 = ObjectAnimator.ofFloat(toZoomIn, "scaleX", 1f, (zoomOutRect.right - zoomOutRect.left) / (float) (zoomInRect.right - zoomInRect.left));
        ObjectAnimator scaleY1 = ObjectAnimator.ofFloat(toZoomIn, "scaleY", 1f, (zoomOutRect.bottom - zoomOutRect.top) / (float) (zoomInRect.bottom - zoomInRect.top));
        ObjectAnimator translationX1 = ObjectAnimator.ofFloat(toZoomIn, "translationX", (zoomOutRect.right - zoomOutRect.left) / 2 - (zoomInRect.right - zoomInRect.left) / 2 + ((RelativeLayout.LayoutParams) toZoomOut.getLayoutParams()).leftMargin);
        ObjectAnimator translationY1 = ObjectAnimator.ofFloat(toZoomIn, "translationY", (zoomOutRect.bottom - zoomOutRect.top) / 2 - (zoomInRect.bottom - zoomInRect.top) / 2 + ((RelativeLayout.LayoutParams) toZoomOut.getLayoutParams()).topMargin);

        animatorSet.setDuration(2000);
        animatorSet.play(scaleX).with(scaleY).with(translationX).with(translationY)
                .with(scaleX1).with(scaleY1).with(translationX1).with(translationY1);
//        animatorSet.play(scaleX1).with(scaleY1).with(translationX1).with(translationY1);

        animatorSet.setInterpolator(new DecelerateInterpolator());

        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isSwitchAnimationRunning = false;
                flPPT.measure(View.MeasureSpec.AT_MOST, View.MeasureSpec.AT_MOST);
                flPPT.forceLayout();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animatorSet.start();


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
        if (recorderFragment.isMaximised()) return;
//        sendViewToBack(flRecorder);
        switchContainer(flRecorder, getCurrentMaximisedView());
        lppptFragment.setIsDisplayMaximised(false);
        recorderFragment.setIsDisplayMaximised(true);

//        flRecorder.bringToFront();
    }

    @Override
    public void maximisePlayerView() {
        switchContainer(flPlayer, getCurrentMaximisedView());

    }

    public static void sendViewToBack(final View child) {
        final ViewGroup parent = (ViewGroup) child.getParent();
        if (null != parent) {
            parent.removeView(child);
            parent.addView(child, 0);
        }
    }

    @Override
    public void maximisePPTView() {
        if (lppptFragment.isMaximised()) return;
//        sendViewToBack(flPPT);
        switchContainer(flPPT, getCurrentMaximisedView());
        lppptFragment.setIsDisplayMaximised(true);
        recorderFragment.setIsDisplayMaximised(false);
//        flPPT.bringToFront();

    }

    private FrameLayout getCurrentMaximisedView() {
        if (lppptFragment.isMaximised()) {
            return flPPT;
        }
        if (recorderFragment != null && recorderFragment.isMaximised()) {
            return flRecorder;
        }
//        flPlayer
//        if(recorderFragment!=null && recorderFragment.isMaximised()){
//            return flPlayer;
//        }
        return null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getLiveRoom().quitRoom();
    }
}
