package com.change.demox;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.change.demox.themecolor.screen.DynamicThemeColorActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btn_dynamic_theme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        btn_dynamic_theme = findViewById(R.id.btn_dynamic_theme);
        btn_dynamic_theme.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_dynamic_theme:
                Intent intent = new Intent(this, DynamicThemeColorActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }

    }
}
