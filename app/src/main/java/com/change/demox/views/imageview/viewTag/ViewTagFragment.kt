package com.change.demox.views.imageview.viewTag

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.change.demox.R
import com.change.demox.views.imageview.viewTag.widget.DeviceModel
import com.change.demox.views.imageview.viewTag.widget.ScalableImageView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Created by Fenrir-xingjunchao on 2020/5/18.
 *
 *
 * 点图片增加标记，图片手势缩放，双击缩放，拖动滑动
 */
class ViewTagFragment : Fragment() {

    var imageView: ScalableImageView? = null

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view =
                View.inflate(container!!.context,
                        R.layout.fragment_image, null)
        imageView = view.findViewById(R.id.imageView)

        val url = "https://publicd6tn2upk7h3ce.blob.core.windows.net/kubota/fig/7F6/7F61200/7F61200112/images/7F61200112.png?se=2020-05-18T09%3A00%3A40Z&sig=PbW3NPfPARDXqqKoehYSNbwkkNh0xKRuuWXgaCvKxSE%3D&sp=r&spr=https&sr=b&sv=2015-04-05"
        imageView?.onDeviceClicked = { id, rect ->
            Toast.makeText(context, "click id = $id, position = $rect", Toast.LENGTH_SHORT).show()
        }

        imageView?.setDataSource(url, getData(), true)

        imageView?.postDelayed({
            imageView?.setHighLightDataIds(arrayListOf(5, 6, 7))//part_id
        }, 3000)

        return view
    }

    private fun getData(): List<DeviceModel> {
        val type = object : TypeToken<List<DeviceModel>>() {}.type
        val list = Gson().fromJson<List<DeviceModel>>(datasource, type)
        return list
    }

    // 数据源
    var datasource: String = "[\n" +
            "        {\n" +
            "         \"part_id\":1,\n" +
            "        \"illustrations\": [{\n" +
            "        \"illustration_id\": 192347,\n" +
            "        \"positions\": [{\n" +
            "        \"start_x\": 1885,\n" +
            "        \"end_x\": 1982,\n" +
            "        \"start_y\": 300,\n" +
            "        \"end_y\": 349\n" +
            "        }]\n" +
            "        }]\n" +
            "        },\n" +
            "        {\n" +
            "         \"part_id\":2,\n" +
            "        \"illustrations\": [{\n" +
            "        \"illustration_id\": 192347,\n" +
            "        \"positions\": [{\n" +
            "        \"start_x\": 365,\n" +
            "        \"end_x\": 445,\n" +
            "        \"start_y\": 1035,\n" +
            "        \"end_y\": 1076\n" +
            "        }]\n" +
            "        }]\n" +
            "        },\n" +
            "        {\n" +
            "         \"part_id\":3,\n" +
            "        \"illustrations\": [{\n" +
            "        \"illustration_id\": 192347,\n" +
            "        \"positions\": [{\n" +
            "        \"start_x\": 2059,\n" +
            "        \"end_x\": 2139,\n" +
            "        \"start_y\": 755,\n" +
            "        \"end_y\": 797\n" +
            "        }]\n" +
            "        }]\n" +
            "        },\n" +
            "        {\n" +
            "         \"part_id\":4,\n" +
            "        \"illustrations\": [{\n" +
            "        \"illustration_id\": 192347,\n" +
            "        \"positions\": [{\n" +
            "        \"start_x\": 1585,\n" +
            "        \"end_x\": 1665,\n" +
            "        \"start_y\": 489,\n" +
            "        \"end_y\": 530\n" +
            "        }]\n" +
            "        }]\n" +
            "        },\n" +
            "        {\n" +
            "         \"part_id\":5,\n" +
            "        \"illustrations\": [{\n" +
            "        \"illustration_id\": 192347,\n" +
            "        \"positions\": [{\n" +
            "        \"start_x\": 762,\n" +
            "        \"end_x\": 841,\n" +
            "        \"start_y\": 664,\n" +
            "        \"end_y\": 705\n" +
            "        }, {\n" +
            "        \"start_x\": 1887,\n" +
            "        \"end_x\": 1966,\n" +
            "        \"start_y\": 626,\n" +
            "        \"end_y\": 667\n" +
            "        }]\n" +
            "        }]\n" +
            "        },\n" +
            "        {\n" +
            "         \"part_id\":6,\n" +
            "        \"illustrations\": [{\n" +
            "        \"illustration_id\": 192347,\n" +
            "        \"positions\": [{\n" +
            "        \"start_x\": 1874,\n" +
            "        \"end_x\": 1952,\n" +
            "        \"start_y\": 746,\n" +
            "        \"end_y\": 787\n" +
            "        }, {\n" +
            "        \"start_x\": 749,\n" +
            "        \"end_x\": 827,\n" +
            "        \"start_y\": 783,\n" +
            "        \"end_y\": 825\n" +
            "        }]\n" +
            "        }]\n" +
            "        },\n" +
            "        {\n" +
            "         \"part_id\":7,\n" +
            "        \"illustrations\": [{\n" +
            "        \"illustration_id\": 192347,\n" +
            "        \"positions\": [{\n" +
            "        \"start_x\": 1104,\n" +
            "        \"end_x\": 1184,\n" +
            "        \"start_y\": 1497,\n" +
            "        \"end_y\": 1539\n" +
            "        }]\n" +
            "        }]\n" +
            "        },\n" +
            "        {\n" +
            "         \"part_id\":8,\n" +
            "        \"illustrations\": [{\n" +
            "        \"illustration_id\": 192347,\n" +
            "        \"positions\": [{\n" +
            "        \"start_x\": 1156,\n" +
            "        \"end_x\": 1235,\n" +
            "        \"start_y\": 232,\n" +
            "        \"end_y\": 273\n" +
            "        }]\n" +
            "        }]\n" +
            "        },\n" +
            "        {\n" +
            "         \"part_id\":9,\n" +
            "        \"illustrations\": [{\n" +
            "        \"illustration_id\": 192347,\n" +
            "        \"positions\": [{\n" +
            "        \"start_x\": 1351,\n" +
            "        \"end_x\": 1430,\n" +
            "        \"start_y\": 345,\n" +
            "        \"end_y\": 387\n" +
            "        }, {\n" +
            "        \"start_x\": 932,\n" +
            "        \"end_x\": 1011,\n" +
            "        \"start_y\": 1394,\n" +
            "        \"end_y\": 1436\n" +
            "        }]\n" +
            "        }]\n" +
            "        },\n" +
            "        {\n" +
            "         \"part_id\":10,\n" +
            "        \"illustrations\": [{\n" +
            "        \"illustration_id\": 192347,\n" +
            "        \"positions\": [{\n" +
            "        \"start_x\": 910,\n" +
            "        \"end_x\": 989,\n" +
            "        \"start_y\": 90,\n" +
            "        \"end_y\": 132\n" +
            "        }, {\n" +
            "        \"start_x\": 1244,\n" +
            "        \"end_x\": 1323,\n" +
            "        \"start_y\": 934,\n" +
            "        \"end_y\": 975\n" +
            "        }]\n" +
            "        }]\n" +
            "        },\n" +
            "        {\n" +
            "         \"part_id\":11,\n" +
            "        \"illustrations\": [{\n" +
            "        \"illustration_id\": 192347,\n" +
            "        \"positions\": [{\n" +
            "        \"start_x\": 224,\n" +
            "        \"end_x\": 297,\n" +
            "        \"start_y\": 560,\n" +
            "        \"end_y\": 601\n" +
            "        }, {\n" +
            "        \"start_x\": 2025,\n" +
            "        \"end_x\": 2098,\n" +
            "        \"start_y\": 1266,\n" +
            "        \"end_y\": 1307\n" +
            "        }]\n" +
            "        }]\n" +
            "        },\n" +
            "        {\n" +
            "         \"part_id\":12,\n" +
            "        \"illustrations\": [{\n" +
            "        \"illustration_id\": 192347,\n" +
            "        \"positions\": [{\n" +
            "        \"start_x\": 716,\n" +
            "        \"end_x\": 789,\n" +
            "        \"start_y\": 1270,\n" +
            "        \"end_y\": 1311\n" +
            "        }]\n" +
            "        }]\n" +
            "        }\n" +
            "        ]"
}