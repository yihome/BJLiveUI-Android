# BJLiveUI-Android

## 集成SDK

* 添加maven仓库
```groovy
maven { url 'https://raw.github.com/baijia/maven/master/' }
```
* 在build.gradle中添加依赖
```groovy
dependencies {
	compile 'com.baijia.live:liveplayer-sdk-ui:0.0.7'
}
```

## API调用说明
* 通过参加码进入直播间
```java
LiveSDKWithUI.enterRoom(context, code, name, listener);
```
--code 为参加码
--name 为用户昵称
--listener 为出错回调

* 其他API请参照本demo