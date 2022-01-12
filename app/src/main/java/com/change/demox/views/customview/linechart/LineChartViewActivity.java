package com.change.demox.views.customview.linechart;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.change.demox.R;

public class LineChartViewActivity extends AppCompatActivity {

    private ChartView mChartView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_chart_view);
        init();
    }

    private void init() {
        mChartView1 = (ChartView) findViewById(R.id.chart_view_1);
        setData();
    }

    private void setData() {
        String title = "7日年化收益率(%)";
        String[] xLabel1 = {"12-11", "12-12", "12-13", "12-14", "12-15", "12-16", "12-17"};
        String[] xLabel2 = {"2-13", "2-14", "2-15", "2-16", "2-17", "2-18", "2-19"};
        String[] data1 = {"2.92", "2.99", "3.20", "2.98", "2.92", "2.94", "2.90"};
        String[] data2 = {"2.50", "2.50", "2.50", "2.50", "2.50", "2.50", "2.50"};
        mChartView1.setTitle(title);
        mChartView1.setxLabel(xLabel1);
        mChartView1.setData(data1);
        mChartView1.fresh();
    }

    public void load(View view) {
        Toast.makeText(this, "正在加载数据...", Toast.LENGTH_SHORT).show();
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(LineChartViewActivity.this, "数据加载成功", Toast.LENGTH_SHORT).show();
                setData();
            }
        }, 2000);
    }
}
