package com.baijiahulian.live.ui.activity;

import com.baijiahulian.livecore.context.LPConstants;
import com.baijiahulian.livecore.context.LPError;
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

    void navigateToHelp();

    void navigateToSetting();

    boolean isTeacherOrAssistant();

    String getCurrentVideoPlayingUserId();

    void playVideo(String userId);

    void playVideoClose(String userId);

    void attachLocalVideo();

    void detachLocalVideo();

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

    void doReconnectServer();

    void showReconnectSuccess();

    void showMessage(String message);

    void saveTeacherMediaStatus(IMediaModel model);

    void showSavePicDialog(byte[] bmpArray);

    void realSaveBmpToFile(byte[] bmpArray);

    void doReEnterRoom();

    void doHandleErrorNothing();

    void showError(LPError error);

    boolean canStudentDraw();

    boolean isCurrentUserTeacher();

    // 学生是否操作过老师视频
    boolean isVideoManipulated();

    void setVideoManipulated(boolean b);

    int getSpeakApplyStatus();

    boolean switchable();

    void setSwitching();
}
