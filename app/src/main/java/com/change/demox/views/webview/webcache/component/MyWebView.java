package com.change.demox.views.webview.webcache.component;

import android.content.Context;
import android.util.AttributeSet;

/**
 * @author feilang-liuyansong
 * @date 2017/11/27 17:16
 * @description
 */

public class MyWebView extends FastWebView {
    public MyWebView(Context context) {
        super(context);
    }

    public MyWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        if (mWebViewScrollListener == null) {
            return;
        }
        mWebViewScrollListener.onScrollChanged(l, t, oldl, oldt);
        super.onScrollChanged(l, t, oldl, oldt);
    }

    public void setOnWebViewScrollListener(OnWebViewScrollListener listener) {
        mWebViewScrollListener = listener;
    }

    protected OnWebViewScrollListener mWebViewScrollListener;

}
