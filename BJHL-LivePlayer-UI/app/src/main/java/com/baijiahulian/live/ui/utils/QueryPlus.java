package com.baijiahulian.live.ui.utils;

import android.util.SparseArray;
import android.view.View;

/**
 * 缓存引用，减少findViewById次数
 * Created by Shubo on 2017/3/15.
 */

public class QueryPlus extends Query {

    private SparseArray<View> viewRefCache;

    private QueryPlus(View contentView) {
        super(contentView);
        viewRefCache = new SparseArray<>();
    }

    public static QueryPlus with(View contentView) {
        return new QueryPlus(contentView);
    }

    @Override
    public QueryPlus id(int id) {
        View cachedView = viewRefCache.get(id);
        if (cachedView != null) {
            view = cachedView;
        } else {
            super.id(id);
            viewRefCache.put(id, super.view());
        }
        return this;
    }
}
