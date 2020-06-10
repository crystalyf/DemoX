package com.change.demox.themecolor.screen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.change.demox.R;

public class DynamicThemeColorActivity extends BaseColorfulActivity implements View.OnClickListener {

    Button btn_theme1, btn_theme2, btn_jump;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_theme_color);
        initView();
    }

    private void initView() {
        btn_theme1 = findViewById(R.id.btn_theme1);
        btn_theme2 = findViewById(R.id.btn_theme2);
        btn_jump = findViewById(R.id.btn_jump);
        btn_theme1.setOnClickListener(this);
        btn_theme2.setOnClickListener(this);
        btn_jump.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 对话框
            case R.id.btn_theme1:
                Colorful.config(getApplicationContext())
                        .primaryColor(Colorful.ThemeColor.BLUE)
                        .accentColor(Colorful.ThemeColor.BLUE)
                        .translucent(false)
                        .dark(true)
                        .apply();
                recreate();
                break;
            // 关闭
            case R.id.btn_theme2:
                Colorful.config(getApplicationContext())
                        .primaryColor(Colorful.ThemeColor.GREEN)
                        .accentColor(Colorful.ThemeColor.GREEN)
                        .translucent(false)
                        .dark(true)
                        .apply();
                recreate();
                break;
            case R.id.btn_jump:
                Intent intent = new Intent(this, SecondActivity.class);
                startActivity(intent);
            default:
                break;
        }
    }
}
