package com.change.demox.views.chart

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import androidx.appcompat.app.AppCompatActivity
import com.change.demox.R
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.android.synthetic.main.activity_chart.*
import java.util.*

class ChartActivity : AppCompatActivity() {

    private val rangeValue = 20f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chart)
        initView()
    }

    private fun initView() {
        chart1.extraBottomOffset = 0f
        chart1.extraLeftOffset = 0f
        chart1.extraRightOffset = 0f
        chart1.setBackgroundColor(Color.WHITE)
        chart1.description.isEnabled = false
        chart1.isDrawHoleEnabled = true
        //饼状图中间的圆的绘制颜色
        chart1.setHoleColor(Color.WHITE)
        chart1.setTransparentCircleColor(Color.WHITE)
        chart1.setTransparentCircleAlpha(110)
        //饼状图中间的圆的半径大小
        chart1.holeRadius = 48f
        //设置圆环的半径值
        chart1.transparentCircleRadius = 51f

        chart1.setDrawCenterText(true)

        chart1.isRotationEnabled = false
        chart1.isHighlightPerTapEnabled = true

        //半圆180f, 整圆360f
        chart1.maxAngle = 360f
        chart1.rotationAngle = 360f
        chart1.setCenterTextOffset(0f, -10f)

        setData(rangeValue, 100f)
        chart1.centerText = generateCenterSpannableText(rangeValue)

        chart1.animateY(1400, Easing.EaseInOutQuad)

        //颜色标示的实体块(比例尺)
        val l: Legend = chart1.legend
        l.yOffset = -50f
        //去掉比例尺
        l.isEnabled = false

        //        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
//        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
//        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
//        l.setDrawInside(false);
//        l.setXEntrySpace(7f);
//        l.setYEntrySpace(0f);
//        l.setYOffset(0f);

        // entry label styling
        chart1.setEntryLabelColor(Color.WHITE)
        chart1.setEntryLabelTextSize(12f)
    }

    /**
     * @param range 范围
     * @param max   最大值
     */
    private fun setData(range: Float, max: Float) {
        val values = ArrayList<PieEntry>()

        //设置了2段的饼状图数据
        values.add(PieEntry(range, ""))
        values.add(PieEntry(max - range, ""))
        val dataSet = PieDataSet(values, "")
        dataSet.sliceSpace = 3f
        dataSet.selectionShift = 5f

        //设置每一段饼状图的颜色
        dataSet.setColors(*ColorTemplate.MATERIAL_COLORS)
        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter())
        //饼状图中百分比文字的大小，颜色
        data.setValueTextSize(11f)
        data.setValueTextColor(Color.WHITE)
        chart1.data = data
        chart1.invalidate()
    }

    /**
     * 设置半圆中央的文字
     *
     * @return
     */
    private fun generateCenterSpannableText(range: Float): SpannableString {
        return SpannableString(range.toString() + "")
    }
}