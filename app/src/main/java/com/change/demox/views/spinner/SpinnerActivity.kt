package com.change.demox.views.spinner

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListPopupWindow
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.change.demox.R
import com.change.demox.utils.Event
import com.change.demox.utils.EventObserver
import com.change.demox.views.spinner.widget.SpinnerTextView
import kotlinx.android.synthetic.main.activity_spinner.*
import java.lang.reflect.Field


class SpinnerActivity : AppCompatActivity() {

    private val leftContent1 = "W"
    private val leftContent2 = "99"

    //after select spinner LiveData
    var afterSelectSpinner = MutableLiveData<Event<Unit>>()

    //button enable LiveData
    var buttonEnable = MutableLiveData<Boolean>(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spinner)
        initOriginalSpinner()
        initCustomSpinner()

        afterSelectSpinner.observe(this, EventObserver {
            checkOkButtonState()
        })
        buttonEnable.observe(this, Observer {
            button_train_number_ok.isEnabled = it!!
        })
    }

    private fun initOriginalSpinner() {
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
                getSpinnerBaseData(),
                true,
                resources
        )
        spinner_content_base.adapter = leftSpinnerAdapter
        spinner_content_base.onItemSelectedListener =
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
            val popupWindow = popup[spinner_content_base] as ListPopupWindow
            popupWindow.height = 196
        } catch (e: NoClassDefFoundError) {
        } catch (e: ClassCastException) {
        } catch (e: NoSuchFieldException) {
        } catch (e: IllegalAccessException) {
        }
    }


    private fun initCustomSpinner() {
        var leftValue: String? = ""
        var rightValue: String? = ""
        leftValue = leftContent2
        rightValue = leftContent2
        //リセット時の右スピナーの表示値
        spinner_content_left.text = leftValue
        spinner_content_right.text = rightValue
        spinner_content_left.setDataList(getSpinnerLeftData())
        spinner_content_right.setDataList(getSpinnerRightData(spinner_content_left.text.toString()))
        spinner_content_left.onItemSelectListener =
                object : SpinnerTextView.OnItemSelectListener {
                    override fun OnItemSelected(position: Int, text: String?) {
                        val list = getSpinnerRightData(spinner_content_left.text.toString())
                        spinner_content_right.setDataList(list)
                        spinner_content_right.text = list[0]
                        afterSelectSpinner.value = Event(Unit)
                    }
                }
        spinner_content_right.onItemSelectListener =
                object : SpinnerTextView.OnItemSelectListener {
                    override fun OnItemSelected(position: Int, text: String?) {
                        afterSelectSpinner.value = Event(Unit)
                    }
                }
    }

    private fun getSpinnerBaseData(): MutableList<String> {
        val listLeft = mutableListOf<String>()
        listLeft.add("")
        listLeft.add("W")
        listLeft.add("99")
        return listLeft
    }

    fun getSpinnerLeftData(): MutableList<String> {
        val listLeft = mutableListOf<String>()
        listLeft.add(leftContent1)
        listLeft.add(leftContent2)
        return listLeft
    }

    fun getSpinnerRightData(leftSpinnerValue: String = ""): MutableList<String> {
        val listRight = mutableListOf<String>()
        if (!TextUtils.isEmpty(leftSpinnerValue)) {
            if (TextUtils.equals(leftSpinnerValue, leftContent2)) {
                listRight.add(leftContent2)
            } else if (TextUtils.equals(leftSpinnerValue, leftContent1)) {
                for (i in 1..50) {
                    listRight.add(i.toString())
                }
            }
        }
        return listRight
    }

    private fun checkOkButtonState() {
        //リセット時、ボタンの状態を変更しないでください
        buttonEnable.value = true
    }
}