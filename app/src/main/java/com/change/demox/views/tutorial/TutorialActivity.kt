package com.change.demox.views.tutorial

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.viewpager.widget.ViewPager
import com.change.demox.R
import com.change.demox.databinding.ActivityTutorialBinding
import com.change.demox.extension.getViewModelFactory
import com.change.demox.utils.EventObserver
import kotlinx.android.synthetic.main.activity_tutorial.*
import java.util.*

/**
 * 引导页
 *
 */
class TutorialActivity : AppCompatActivity() {

    companion object {
        const val isChannelFromMenu = "isChannelFromMenu"
    }

    private var tutorialPagerPosition = 0
    private lateinit var viewModel: TutorialViewModel

    //用dataBinding的原因是要在界面中绑定viewModel
    private var dataBinding: ActivityTutorialBinding? = null

    //图像列表数组
    private val imageArray =
            intArrayOf(
                    R.mipmap.ic_tutorial1,
                    R.mipmap.ic_tutorial2,
                    R.mipmap.ic_tutorial3,
                    R.mipmap.ic_tutorial4
            )
    private val viewList: ArrayList<View> =
            ArrayList()

    //底部的小圆点dot数组
    private var dotList: MutableList<ImageView?> = mutableListOf()
    private val idsArray = intArrayOf(
            R.id.imageview_tutorial_dot1,
            R.id.imageview_tutorial_dot2,
            R.id.imageview_tutorial_dot3,
            R.id.imageview_tutorial_dot4
    )

    //引导教程文字
    private var tutorialTextList: Array<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_tutorial)
        viewModel = viewModels<TutorialViewModel> { getViewModelFactory() }.value
        dataBinding?.viewModel = viewModel
        //需要加上这句,否则livedata改变不会刷新UI
        dataBinding?.lifecycleOwner = this
        initView()
    }

    override fun onBackPressed() {
        if (tutorialPagerPosition == 0) {
            finish()
        } else {
            //切换到上一页
            viewpager_tutorial.currentItem = tutorialPagerPosition - 1
        }
    }

    private fun initView() {
        viewModel.channelFromMenu = intent.getBooleanExtra(isChannelFromMenu, false)
        //最初显示的引导页
        tutorialPagerPosition = 0
        tutorialTextList = resources.getStringArray(R.array.tutorial_text)
        initViewPage()
        initDot()
        viewModel.okClick.observe(this, Observer {
            if (it.peekContent()) {
                this.finish()
            } else {
                transferToTop()
            }
        })
        viewModel.nextClick.observe(this, EventObserver {
            //切换到下一页
            viewpager_tutorial.currentItem = tutorialPagerPosition + 1
        })

        viewModel.isLastPage.observe(this, Observer {
            if (it) {
                if (viewModel.channelFromMenu) {
                    button_tutorial_ok.text =
                            resources.getString(R.string.menu_close)
                } else {
                    button_tutorial_ok.text =
                            resources.getString(R.string.tutorial_start)
                }
            }
        })
    }

    /**
     * 初始化圆点dot
     *
     */
    private fun initDot() {
        for (i in viewList.indices) {
            dotList.add(findViewById<View>(idsArray[i]) as ImageView)
        }
    }

    /**
     * viewpager的初始化
     *
     */
    private fun initViewPage() {
        //循环创建引导image并将其添加到viewList
        for (index in imageArray.indices) {
            val view = View.inflate(applicationContext, R.layout.tutorial_view_pager_layout, null)
            val imageview_tutorial = view.findViewById<View>(R.id.imageview_tutorial) as ImageView
            val tutorial_text = view.findViewById<View>(R.id.tutorial_text) as TextView
            imageview_tutorial.setImageResource(imageArray[index])
            tutorial_text.text = this.tutorialTextList!![index]
            viewList.add(view)
        }
        viewpager_tutorial.adapter = ViewPagerAdapter(viewList)
        viewpager_tutorial.addOnPageChangeListener(object :
                ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                // 设置dot的选择状态
                dotList[tutorialPagerPosition]!!.setImageResource(R.drawable.ic_dot_off)
                dotList[position]!!.setImageResource(R.drawable.ic_dot_on)
                tutorialPagerPosition = position
                //确定是否是最后一页、如果是最后一页、按钮显示开始
                viewModel.isLastPage.value = position == imageArray.size - 1
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
    }

    /**
     * 点击迁移到其他画面
     */
    private fun transferToTop() {
        Toast.makeText(this@TutorialActivity, "点击迁移到其他画面", Toast.LENGTH_SHORT).show()
    }

}