package com.baijiahulian.live.ui.utils;

/**
 * Created by Shubo on 2017/4/6.
 */

public class AliCloudImageUtil {

    public static String getRoundedAvatarUrl(String url, int radius) {
        if (url.contains("@")) {
            url = url.substring(0, url.indexOf("@"));
        }
        return url + "@" + radius + "-" + "1ci.png";
    }

}
