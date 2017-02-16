package com.baijiahulian.live.ui.utils;

import rx.Subscription;

/**
 * Created by Shubo on 2017/2/13.
 */

public class RxUtils {

    public static void unSubscribe(Subscription subscription) {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }
}
