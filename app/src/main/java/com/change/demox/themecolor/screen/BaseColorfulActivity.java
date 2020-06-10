package com.change.demox.themecolor.screen;

import android.app.ActivityManager;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.change.demox.R;


/**
 * @decs: BaseColorful页
 * @author: 郑少鹏
 * @date: 2019/7/17 12:18
 */
public abstract class BaseColorfulActivity extends AppCompatActivity {
    private String themeString;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        themeString = Colorful.getThemeString();
        setTheme(R.style.AppTheme);
        getTheme().applyStyle(Colorful.getThemeDelegate().getStyleResPrimary(), true);
        getTheme().applyStyle(Colorful.getThemeDelegate().getStyleResAccent(), true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (Colorful.getThemeDelegate().isTranslucent()) {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
            ActivityManager.TaskDescription taskDescription = new ActivityManager.TaskDescription(null, null,
                    ContextCompat.getColor(this, Colorful.getThemeDelegate().getPrimaryColor().getColorRes()));
            setTaskDescription(taskDescription);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!Colorful.getThemeString().equals(themeString)) {
            recreate();
        }
    }
}
