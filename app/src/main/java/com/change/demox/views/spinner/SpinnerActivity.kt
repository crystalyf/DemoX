package com.change.demox.views.spinner

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListPopupWindow
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.change.demox.R
import kotlinx.android.synthetic.main.activity_spinner.*
import java.lang.reflect.Field


class SpinnerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spinner)
        initView()
    }

    private fun initView() {
        val list = ArrayList<String>()
        list.add("W7")
        list.add("W8")
        list.add("W9")
        //构造ArrayAdapter
        val adapter = ArrayAdapter(this,
                R.layout.simple_spinner_item, list)
        //设置下拉样式以后显示的样式
        adapter.setDropDownViewResource(R.layout.my_drop_down_item)
        field_item_spinner_content.adapter = adapter

        /**
         * 自定义基于baseAdapter的spinner adapter
         */
        val leftSpinnerAdapter = SpinnerTrainNumberAdapter(
                this@SpinnerActivity,
                getSpinnerLeftData(),
                true,
                resources
        )
        spinner_content_left.adapter = leftSpinnerAdapter
        spinner_content_left.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                            parent: AdapterView<*>?, view: View?,
                            pos: Int, id: Long
                    ) {
                        //item选中的回调
                        Log.v("printtt", pos.toString())
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                    }
                }


        //如果需要改变dropdown列表的高度，那么需要反射的方式得到原生内置的popupWindow,然后修改
        try {
            val popup: Field = Spinner::class.java.getDeclaredField("mPopup")
            popup.isAccessible = true
            val popupWindow = popup[spinner_content_left] as ListPopupWindow
            popupWindow.height = 196
        } catch (e: NoClassDefFoundError) {
        } catch (e: ClassCastException) {
        } catch (e: NoSuchFieldException) {
        } catch (e: IllegalAccessException) {
        }
    }

    private fun getSpinnerLeftData(): MutableList<String> {
        val listLeft = mutableListOf<String>()
        listLeft.add("")
        listLeft.add("W")
        listLeft.add("99")
        return listLeft
    }
}