package com.baijiahulian.live.ui.activity;

import com.baijiahulian.livecore.context.LPConstants;
import com.baijiahulian.livecore.context.LiveRoom;
import com.baijiahulian.livecore.models.imodels.IMediaModel;

/**
 * Created by Shubo on 2017/2/15.
 */

public interface LiveRoomRouterListener {

    LiveRoom getLiveRoom();

    void setLiveRoom(LiveRoom liveRoom);

    void navigateToMain();

    void clearScreen();

    void unClearScreen();

    void navigateToMessageInput();

    void navigateToPPTDrawing();

    LPConstants.LPPPTShowWay getPPTShowType();

    void setPPTShowType(LPConstants.LPPPTShowWay type);

    void navigateToSpeakers();

    void navigateToUserList();

    void navigateToPPTWareHouse();

    void disableSpeakerMode();

    void enableSpeakerMode();

    void maximiseRecorderView();

    void maximisePlayerView();

    void maximisePPTView();

    void showMorePanel(int anchorX, int anchorY);

    void navigateToShare();

    void navigateToAnnouncement();

    void navigateToCloudRecord(boolean recordStatus);

    @Deprecated
    boolean getCloudRecordStatus();

    void navigateToHelp();

    void navigateToSetting();

    boolean isTeacherOrAssistant();

    // caution: 就算在调用了playVideo后调用该方法，也可能不会及时返回正在播放视频的userId！
    // 请调用同步方法 getCurrentVideoUser().getUser().getUserId()获取
    String getCurrentVideoPlayingUserId();

    void playVideo(String userId);

    void playVideoClose(String userId);

    void attachVideo();

    void detachVideo();

    void showRecorderDialogFragment();

    void showPPTDialogFragment();

    void showRemoteVideoPlayer();

    /**
     * 当前正在互动的用户多媒体对象
     */
    void setCurrentVideoUser(IMediaModel mediaModel);

    IMediaModel getCurrentVideoUser();

    void clearPPTAllShapes();

    void changeScreenOrientation();

    int getCurrentScreenOrientation();

    int getSysRotationSetting();

    //允许自由转屏
    void letScreenRotateItself();

    //不允许自由转屏
    void forbidScreenRotateItself();

    void showBigChatPic(String url);

    void sendImageMessage(String path);

    void showSavePicDialog(byte[] bmpArray);

    void realSaveBmpToFile(byte[] bmpArray);
}
