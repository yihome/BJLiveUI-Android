package com.baijiahulian.live.ui;

import com.baijiahulian.livecore.utils.LPErrorPrintSubscriber;

import java.util.concurrent.TimeUnit;

import rx.Observable;

/**
 * Created by Shubo on 2017/4/8.
 */

public class Test {
    public static void main(String[] args) {
        Observable.interval(1, TimeUnit.SECONDS).subscribe(new LPErrorPrintSubscriber<Long>() {
            @Override
            public void call(Long aLong) {
                System.out.print(aLong);
            }
        });

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
