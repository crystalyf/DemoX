package com.change.demox.themecolor.screen;

import android.content.Context;

import androidx.annotation.StyleRes;


/**
 * @decs: ThemeDelegate
 * @author: 郑少鹏
 * @date: 2019/7/17 15:07
 */
public class ThemeDelegate {
    private Colorful.ThemeColor primaryColor;
    private Colorful.ThemeColor accentColor;
    private boolean translucent;
    private boolean dark;
    @StyleRes
    private int styleResPrimary;
    @StyleRes
    private int styleResAccent;


    ThemeDelegate(Context context, Colorful.ThemeColor primary, Colorful.ThemeColor accent, boolean translucent, boolean dark) {
        this.primaryColor = primary;
        this.accentColor = accent;
        this.translucent = translucent;
        this.dark = dark;
        long curTime = System.currentTimeMillis();
        styleResPrimary = context.getResources().getIdentifier("primary" + primary.ordinal(), "style", context.getPackageName());
        styleResAccent = context.getResources().getIdentifier("accent" + accent.ordinal(), "style", context.getPackageName());
    }

    @StyleRes
    int getStyleResPrimary() {
        return styleResPrimary;
    }

    @StyleRes
    int getStyleResAccent() {
        return styleResAccent;
    }

    Colorful.ThemeColor getPrimaryColor() {
        return primaryColor;
    }

    Colorful.ThemeColor getAccentColor() {
        return accentColor;
    }

    boolean isTranslucent() {
        return translucent;
    }

    public boolean isDark() {
        return dark;
    }
}
