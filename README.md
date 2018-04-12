BJLiveUI-Android
===============
详细的文档、示例、常见问题等请访问 百家云官方网站 [http://www.baijiayun.com/](http://www.baijiayun.com/)

- [官方文档](http://dev.baijiayun.com/default/wiki/index)
- **[github链接](https://github.com/baijia/BJLiveCore-Android)**
- Product: 云端直播、云端课堂、U-Training
- platform: API 14~25  [BeautyVideoFilter(美颜功能) 18+]
- cpu: ARM, ARMv7a, ARM64v8a
- IDE: **[Android Studio](https://developer.android.com/studio/index.html)** Recommend
- **[Change Log](https://github.com/baijia/BJLiveUI-Android/blob/master/CHANGELOG.md)**
- [更多 SDK](https://github.com/baijia)

## 百家云直播Android SDK简介
百家云直播Android SDK提供了[Core (liveplayer-sdk-core)](https://github.com/baijia/BJLiveCore-Android)、[PPT (liveplayer-sdk-core-ppt)](link_ppt)和[UI (BJLiveUI-Android)](https://github.com/baijia/BJLiveUI-Android)三个库。

- [UI](https://github.com/baijia/BJLiveUI-Android)库基于[Core](https://github.com/baijia/BJLiveCore-Android)和[PPT](link_ppt)实现，提供了一个针对教育场景下师生互动模板，主要包括师生一对一音视频互动，多人音视频互动，课件展示、文字聊天等功能，可以快速接入，集成工作量小，适合需要快速上线的同学，该库计划开源。
- [Core](https://github.com/baijia/BJLiveCore-Android)为核心库，涵盖了直播间几乎所有的功能，包括音视频推拉流、信令服务器通信、聊天服务器通信等功能，该库不含UI资源，如果使用的是eclipse的同学可以将aar其中的jar包拿出来单独使用。
- [PPT](link_ppt)单独把课件模块拆出来了，主要功能包含课件展示、画笔交互、动态PPT效果等，依赖于[Core](https://github.com/baijia/BJLiveCore-Android)。

## 集成SDK
### aar依赖
* 添加maven仓库
```groovy
maven { url 'https://raw.github.com/baijia/maven/master/' }
```
对于部分国内用户，如果github被墙或者访问比较慢，可以使用我们国内的镜像仓库
```groovy
maven { url 'http://git.baijiashilian.com/open-android/maven/raw/master/' }
```
* 在build.gradle中添加依赖
```groovy
dependencies {
	implementation 'com.baijia.live:liveplayer-sdk-ui:1.3.0'
}
```
### 源码依赖
```groovy
dependencies {
	api project(':liveplayer-sdk-ui')
}
```

## API调用说明
###进入直播间
* 通过参加码进入直播间
```java
/**
* 通过参加码进入房间
* @param context
* @param code     参加码
* @param name     昵称
* @param listener 出错回调
*/
LiveSDKWithUI.enterRoom(context, code, name, listener);
```
* 通过**roomId**和**sign**进入进入直播间
```java
/**
*
* @param context
* @param roomId   房间号
* @param sign     签名
* @param model    用户model (包含昵称、头像、角色等)
* @param listener 出错回调
*/
LiveSDKWithUI.enterRoom(context, roomId, sign, model, listener);
```
Sign原则上由后端计算返给前端，[计算规则](http://dev.baijiayun.com/default/wiki/detail/4#h3)

### 直播间分享功能
分享功能可以启用也可以不启用，不启用时直播间内不会显示分享图标，反之则会显示。
```java
LiveSDKWithUI.setShareListener(new LiveSDKWithUI.LPShareListener() {
  @Override
  public void onShareClicked(Context context, int i) {
  // i 为LPShareModel里getShareType的值
  }

  @Override
  public ArrayList<? extends LPShareModel> setShareList() {
  // shareModel请继承LPShareModel
  // getShareIconRes() 返回分享图标 resource
  // getShareIconText() 返回分享图标文字 如：微信、朋友圈
  // hasCorner 默认false
  return null;
  }

  @Override
  public void getShareData(Context context, long roomId) {
  // 触发集成方异步获取分享信息
  }
});
```
### 单点登录
直播间支持单点登录功能，如果使用同一个user_number进入直播，后进直播间的会踢掉先进的直播间的，此时会有回调
```java
LiveSDKWithUI.setEnterRoomConflictListener(new LiveSDKWithUI.RoomEnterConflictListener() {
  @Override
  public void onConflict(Context context, LPConstants.LPEndType endType, final LiveSDKWithUI.LPRoomExitCallback callback) {
  // 单点登录冲突 endType为冲突方终端类型
  // 回调callback.exit(); 退出直播间
  }
});
```
### 离开直播间二次确认
用户在直播间内点击关闭按钮或者点击返回时，会有回调，此时可以友好的提示退出的二次确认
```java
 LiveSDKWithUI.setRoomExitListener(new LiveSDKWithUI.LPRoomExitListener() {
   @Override
   public void onRoomExit(Context context, final LiveSDKWithUI.LPRoomExitCallback lpRoomExitCallback) {
   //退出直播间二次确认回调 无二次确认无需设置
   lpRoomExitCallback.exit();
   // or
   lpRoomExitCallback.cancel();
 });
```

## SDK体验
下图为老师上课视角，包含有PPT展示，老师自己的视频，学生的视频，在线人员列表、PPT管理等按钮
![picture](http://imgs.genshuixue.com/0baijiatools/32fc8da1adbb260d1d44031c57130ffa/teacher_verti.png@480h_270w_1e_1c "capture竖版")

