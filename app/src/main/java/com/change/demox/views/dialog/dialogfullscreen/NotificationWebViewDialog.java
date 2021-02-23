package com.change.demox.views.dialog.dialogfullscreen;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.change.demox.R;


/**
 * Created by xingjunchao on 2021/02/22.
 * 全屏显示的dialog
 */
public class NotificationWebViewDialog extends Dialog implements View.OnClickListener {

    private View contentView;
    private Activity context;
    private ImageView img_close;
    private WebView webView;

    public NotificationWebViewDialog(Activity _context, String webUrl) {
        super(_context);
        context = _context;
        contentView = LayoutInflater.from(context).inflate(R.layout.notification_webview_dialog, null);
        this.setContentView(contentView);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //边界透明
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        //边距
        ((FrameLayout.LayoutParams) contentView.getLayoutParams()).leftMargin = 50;
        ((FrameLayout.LayoutParams) contentView.getLayoutParams()).rightMargin = 50;
        ((FrameLayout.LayoutParams) contentView.getLayoutParams()).topMargin = 30;
        ((FrameLayout.LayoutParams) contentView.getLayoutParams()).bottomMargin = 30;
        webView = contentView.findViewById(R.id.webview);
        img_close = contentView.findViewById(R.id.img_close);
        img_close.setOnClickListener(this);
        webView.loadUrl(webUrl);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //点击网页内容触发重定向的话，都交给外部浏览器来做
                startBrowser(context, url);
                closeDialog();
                return true;
            }
        });
    }

    /**
     * 打开外部浏览器
     *
     * @param context 環境
     * @param url
     */
    private void startBrowser(Context context, String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri content_url = Uri.parse(url);
        intent.setData(content_url);
        try {
            context.startActivity(intent);
        } catch (Exception e) {

        }
    }

    /**
     * 关闭Dialog
     */
    private void closeDialog() {
        this.dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_close:
                closeDialog();
                break;
        }
    }
}
