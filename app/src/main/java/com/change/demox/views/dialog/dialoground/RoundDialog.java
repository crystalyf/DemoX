package com.change.demox.views.dialog.dialoground;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.change.demox.R;


/**
 * Created by xingjunchao on 2021/02/22.
 * 全屏显示的dialog
 */
public class RoundDialog extends Dialog implements View.OnClickListener {

    private View contentView;
    private Activity context;
    private ImageView img_close;

    public RoundDialog(Activity _context) {
        super(_context);
        context = _context;
        contentView = LayoutInflater.from(context).inflate(R.layout.round_dialog, null);
        this.setContentView(contentView);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //边界透明
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        //边距
        ((FrameLayout.LayoutParams) contentView.getLayoutParams()).leftMargin = 50;
        ((FrameLayout.LayoutParams) contentView.getLayoutParams()).rightMargin = 50;
        ((FrameLayout.LayoutParams) contentView.getLayoutParams()).topMargin = 50;
        ((FrameLayout.LayoutParams) contentView.getLayoutParams()).bottomMargin = 50;
        img_close = contentView.findViewById(R.id.img_close);
        img_close.setOnClickListener(this);
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
