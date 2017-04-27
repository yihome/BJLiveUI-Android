package com.baijiahulian.live.ui.activity;

import android.content.Context;
import android.content.res.Configuration;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.baijiahulian.live.ui.R;
import com.baijiahulian.live.ui.announcement.AnnouncementFragment;
import com.baijiahulian.live.ui.announcement.AnnouncementPresenter;
import com.baijiahulian.live.ui.base.BasePresenter;
import com.baijiahulian.live.ui.base.BaseView;
import com.baijiahulian.live.ui.chat.ChatFragment;
import com.baijiahulian.live.ui.chat.ChatPresenter;
import com.baijiahulian.live.ui.chat.MessageSendPresenter;
import com.baijiahulian.live.ui.chat.MessageSentFragment;
import com.baijiahulian.live.ui.leftmenu.LeftMenuFragment;
import com.baijiahulian.live.ui.leftmenu.LeftMenuPresenter;
import com.baijiahulian.live.ui.loading.LoadingFragment;
import com.baijiahulian.live.ui.loading.LoadingPresenter;
import com.baijiahulian.live.ui.more.MoreMenuDialogFragment;
import com.baijiahulian.live.ui.more.MoreMenuPresenter;
import com.baijiahulian.live.ui.ppt.MyPPTFragment;
import com.baijiahulian.live.ui.ppt.PPTPresenter;
import com.baijiahulian.live.ui.recorderdialog.RecorderDialogFragment;
import com.baijiahulian.live.ui.recorderdialog.RecorderDialogPresenter;
import com.baijiahulian.live.ui.rightbotmenu.RightBottomMenuFragment;
import com.baijiahulian.live.ui.rightbotmenu.RightBottomMenuPresenter;
import com.baijiahulian.live.ui.rightmenu.RightMenuFragment;
import com.baijiahulian.live.ui.rightmenu.RightMenuPresenter;
import com.baijiahulian.live.ui.setting.SettingDialogFragment;
import com.baijiahulian.live.ui.setting.SettingPresenter;
import com.baijiahulian.live.ui.speakqueue.SpeakQueueDialogFragment;
import com.baijiahulian.live.ui.speakqueue.SpeakQueuePresenter;
import com.baijiahulian.live.ui.topbar.TopBarFragment;
import com.baijiahulian.live.ui.topbar.TopBarPresenter;
import com.baijiahulian.live.ui.users.OnlineUserDialogFragment;
import com.baijiahulian.live.ui.users.OnlineUserPresenter;
import com.baijiahulian.live.ui.videoplayer.VideoPlayerFragment;
import com.baijiahulian.live.ui.videoplayer.VideoPlayerPresenter;
import com.baijiahulian.live.ui.videorecorder.VideoRecorderFragment;
import com.baijiahulian.live.ui.videorecorder.VideoRecorderPresenter;
import com.baijiahulian.livecore.context.LPConstants;
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
    @BindView(R.id.activity_live_room_chat_drawer)
    DrawerLayout dlChat;

    private LiveRoom liveRoom;

    private LoadingFragment loadingFragment;
    private TopBarFragment topBarFragment;

    private MyPPTFragment lppptFragment;
    private VideoRecorderFragment recorderFragment;
    private ChatFragment chatFragment;
    private RightBottomMenuFragment rightBottomMenuFragment;
    private LeftMenuFragment leftMenuFragment;
    private RightMenuFragment rightMenuFragment;
    private WindowManager windowManager;

    private VideoPlayerFragment playerFragment;
    private VideoPlayerPresenter playerPresenter;

    private OrientationEventListener orientationEventListener; //处理屏幕旋转时本地录制视频的方向
    private int oldRotation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_room);
        ButterKnife.bind(this);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            onConfigurationChanged(getResources().getConfiguration());
        }

        String code = getIntent().getStringExtra("code");
        String name = getIntent().getStringExtra("name");

        loadingFragment = new LoadingFragment();
        LoadingPresenter loadingPresenter = new LoadingPresenter(loadingFragment, code, name);
        loadingPresenter.setRouter(this);
        loadingFragment.setPresenter(loadingPresenter);
        addFragment(R.id.activity_live_room_loading, loadingFragment);

        windowManager = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);
        oldRotation = windowManager.getDefaultDisplay().getRotation();

        orientationEventListener = new OrientationEventListener(this, SensorManager.SENSOR_DELAY_FASTEST) {
            @Override
            public void onOrientationChanged(int orientation) {
                int newRotation = windowManager.getDefaultDisplay().getRotation();
                if (newRotation != oldRotation) {
                    oldRotation = newRotation;
                    if (liveRoom.getRecorder().isVideoAttached())
                        liveRoom.getRecorder().invalidVideo();
                }
            }
        };
        dlChat.openDrawer(Gravity.START);
        checkScreenOrientationInit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        orientationEventListener.enable();
    }

    @Override
    protected void onPause() {
        super.onPause();
        orientationEventListener.disable();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            dlChat.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            dlChat.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
        }
        onBackgroundContainerConfigurationChanged(newConfig);
        onForegroundLeftContainerConfigurationChanged(newConfig);
        onForegroundRightContainerConfigurationChanged(newConfig);
    }

    private void onForegroundRightContainerConfigurationChanged(Configuration newConfig) {
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) flForegroundRight.getLayoutParams();
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            lp.addRule(RelativeLayout.BELOW, R.id.activity_live_room_top);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            lp.addRule(RelativeLayout.BELOW, R.id.activity_live_room_background_container);
        }
        flForegroundRight.setLayoutParams(lp);
    }

    private void onForegroundLeftContainerConfigurationChanged(Configuration newConfig) {
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) flForegroundLeft.getLayoutParams();
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            lp.addRule(RelativeLayout.BELOW, R.id.activity_live_room_top);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            lp.addRule(RelativeLayout.BELOW, R.id.activity_live_room_background_container);
        }
        flForegroundLeft.setLayoutParams(lp);
    }

    private void onBackgroundContainerConfigurationChanged(Configuration newConfig) {
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) flBackground.getLayoutParams();
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            lp.addRule(RelativeLayout.ABOVE, 0); // lp.removeRule()
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

        chatFragment = new ChatFragment();
        bindVP(chatFragment, new ChatPresenter(chatFragment));
        addFragment(R.id.activity_live_room_chat, chatFragment);

        // might delay 500ms to process
        removeFragment(loadingFragment);
        flLoading.setVisibility(View.GONE);

        if (getLiveRoom().getCurrentUser().getType() == LPConstants.LPUserType.Teacher) {
            liveRoom.requestClassStart();
        }
    }

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
        MessageSentFragment fragment = MessageSentFragment.newInstance();
        MessageSendPresenter presenter = new MessageSendPresenter(fragment);
        bindVP(fragment, presenter);
        showDialogFragment(fragment);
    }

    @Override
    public void navigateToPPTDrawing() {
        checkNotNull(lppptFragment);
        lppptFragment.changePPTCanvasMode();
        lppptFragment.setPPTShowWay(LPConstants.LPPPTShowWay.SHOW_COVERED);
    }

    @Override
    public LPConstants.LPPPTShowWay getPPTShowType() {
        return lppptFragment.getPPTShowWay();
    }

    @Override
    public void setPPTShowType(LPConstants.LPPPTShowWay type) {
        lppptFragment.setPPTShowWay(type);
    }

    @Override
    public void navigateToSpeakers() {
        SpeakQueueDialogFragment fragment = SpeakQueueDialogFragment.newInstance();
        SpeakQueuePresenter presenter = new SpeakQueuePresenter(fragment);
        bindVP(fragment, presenter);
        showDialogFragment(fragment);
    }

    @Override
    public void navigateToUserList() {
        OnlineUserDialogFragment userListFragment = OnlineUserDialogFragment.newInstance();
        OnlineUserPresenter userPresenter = new OnlineUserPresenter(userListFragment);
        bindVP(userListFragment, userPresenter);
        showDialogFragment(userListFragment);
    }

    @Override
    public void navigateToPPTWareHouse() {

    }

    @Override
    public void disableSpeakerMode() {
        rightBottomMenuFragment.disableSpeakerMode();
        if (recorderFragment != null) {
            detachVideo();
        }
        if (getLiveRoom().getRecorder().isPublishing())
            getLiveRoom().getRecorder().stopPublishing();
    }

    @Override
    public void enableSpeakerMode() {
        rightBottomMenuFragment.enableSpeakerMode();
    }

    @Override
    public void maximiseRecorderView() {
        View max = flBackground.getChildAt(0);
        if (recorderFragment.getView() == max) return;
        boolean isPPT = max == lppptFragment.getView();
        if (isPPT)
            lppptFragment.onPause();
        switchView(recorderFragment.getView(), max);
        if (isPPT)
            lppptFragment.onResume();
        liveRoom.getRecorder().invalidVideo();
    }

    @Override
    public void maximisePlayerView() {
        View max = flBackground.getChildAt(0);
        if (playerFragment.getView() == max) return;
        boolean isPPT = max == lppptFragment.getView();
        if (isPPT)
            lppptFragment.onPause();
        switchView(playerFragment.getView(), max);
        if (isPPT)
            lppptFragment.onResume();
        liveRoom.getRecorder().invalidVideo();
    }

    @Override
    public void maximisePPTView() {
        View max = flBackground.getChildAt(0);
        if (lppptFragment.getView() == max) return;
        lppptFragment.onPause();
        switchView(lppptFragment.getView(), max);
        lppptFragment.onResume();
        liveRoom.getRecorder().invalidVideo();
    }

    @Override
    public void showMorePanel(int anchorX, int anchorY) {
        MoreMenuDialogFragment fragment = MoreMenuDialogFragment.newInstance(anchorX, anchorY);
        MoreMenuPresenter presenter = new MoreMenuPresenter(fragment);
        bindVP(fragment, presenter);
        showDialogFragment(fragment);
    }

    @Override
    public void navigateToShare() {

    }

    @Override
    public void navigateToAnnouncement() {
        AnnouncementFragment fragment = AnnouncementFragment.newInstance();
        AnnouncementPresenter presenter = new AnnouncementPresenter(fragment);
        bindVP(fragment, presenter);
        showDialogFragment(fragment);
    }

    @Override
    public void navigateToCloudRecord() {

    }

    @Override
    public boolean getCloudRecordStatus() {
        return false;
    }

    @Override
    public void navigateToHelp() {

    }

    @Override
    public void navigateToSetting() {
        SettingDialogFragment settingFragment = SettingDialogFragment.newInstance();
        SettingPresenter settingPresenter = new SettingPresenter(settingFragment);
        bindVP(settingFragment, settingPresenter);
        showDialogFragment(settingFragment);
    }

    @Override
    public boolean isTeacherOrAssistant() {
        return getLiveRoom().getCurrentUser().getType() == LPConstants.LPUserType.Teacher ||
                getLiveRoom().getCurrentUser().getType() == LPConstants.LPUserType.Assistant;
    }

    @Override
    public String getCurrentVideoPlayingUserId() {
        if (playerPresenter == null) return null;
        else return playerPresenter.getCurrentPlayingUserId();
    }

    @Override
    public void playVideo(String userId) {
        if (playerPresenter == null) {
            playerFragment = new VideoPlayerFragment();
            playerPresenter = new VideoPlayerPresenter(playerFragment);
            bindVP(playerFragment, playerPresenter);
            addFragment(R.id.activity_live_room_foreground_right_container, playerFragment);
        }
        playerPresenter.playVideo(userId);
    }

    @Override
    public void playVideoClose(String userId) {
        checkNotNull(playerPresenter);
        playerPresenter.playAVClose(userId);
        removeFragment(playerFragment);
        playerFragment = null;
        playerPresenter = null;
    }

    @Override
    public void attachVideo() {
        if (recorderFragment == null) {
            recorderFragment = new VideoRecorderFragment();
            bindVP(recorderFragment, new VideoRecorderPresenter(recorderFragment));
            addFragment(R.id.activity_live_room_foreground_left_container, recorderFragment);
        }
    }

    @Override
    public void detachVideo() {
        checkNotNull(recorderFragment);
        removeFragment(recorderFragment);
        getSupportFragmentManager().executePendingTransactions();
        recorderFragment = null;
    }

    @Override
    public void showRecorderDialogFragment() {
        RecorderDialogFragment recorderFragment = new RecorderDialogFragment();
        RecorderDialogPresenter recorderPresenter = new RecorderDialogPresenter(recorderFragment);
        bindVP(recorderFragment, recorderPresenter);
        showDialogFragment(recorderFragment);
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
        orientationEventListener = null;
        if (getLiveRoom().getCurrentUser().getType() == LPConstants.LPUserType.Teacher) {
            liveRoom.requestClassEnd();
        }
        getLiveRoom().quitRoom();

    }

    //初始化时检测屏幕方向，以此设置聊天页面是否可隐藏
    private void checkScreenOrientationInit() {
        Configuration configuration = this.getResources().getConfiguration();
        int orientation = configuration.orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            dlChat.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        } else {
            dlChat.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
        }
    }
}
