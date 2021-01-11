package com.change.demox.views.recyclerview.banner.common

import com.change.demox.views.recyclerview.banner.HomeViewModel
import com.change.demox.views.recyclerview.banner.component.BasePresenter
import com.google.gson.Gson

/**
 * Created by xuhao on 2017/11/8.
 * 首页精选的 Presenter
 * (数据是 Banner 数据和一页数据组合而成的 HomeBean,查看接口然后在分析就明白了)
 */

class HomePresenter : BasePresenter<HomeContract.View>(), HomeContract.Presenter {


    private var bannerHomeBean: HomeBean? = null

    private var nextPageUrl: String? = null     //加载首页的Banner 数据+一页数据合并后，nextPageUrl没 add

    private val homeViewModel: HomeViewModel by lazy {

        HomeViewModel()
    }

    /**
     * 获取首页精选数据 banner 加 一页数据
     */
    override fun requestHomeData(num: Int) {
        // 检测是否绑定 View
        checkViewAttached()
        mRootView?.showLoading()
        val disposable = homeViewModel.requestHomeData(num)
                .flatMap { homeBean ->
                    //过滤掉 Banner2(包含广告,等不需要的 Type), 具体查看接口分析
                    val bannerItemList = homeBean.issueList[0].itemList
                    bannerItemList.filter { item ->
                        item.type == "banner2" || item.type == "horizontalScrollCard"
                    }.forEach { item ->
                        //移除 item
                        bannerItemList.remove(item)
                    }
                    bannerHomeBean = homeBean //记录第一页是当做 banner 数据
                    //gson解析
                    val gson: Gson? = Gson()
                    val str = gson?.toJson(bannerHomeBean)
                    //根据 nextPageUrl 请求（banner下面第一页的数据）
                    homeViewModel.loadMoreData(homeBean.nextPageUrl)
                }.subscribe({ homeBean ->
                    mRootView?.apply {
                        dismissLoading()
                        nextPageUrl = homeBean.nextPageUrl
                        //过滤掉 Banner2(包含广告,等不需要的 Type), 具体查看接口分析
                        val newBannerItemList = homeBean.issueList[0].itemList
                        newBannerItemList.filter { item ->
                            item.type == "banner2" || item.type == "horizontalScrollCard"
                        }.forEach { item ->
                            //移除 item
                            newBannerItemList.remove(item)
                        }
                        // 重新赋值 Banner 长度
                        bannerHomeBean!!.issueList[0].count = bannerHomeBean!!.issueList[0].itemList.size
                        //赋值过滤后的数据 + banner 数据
                        bannerHomeBean?.issueList!![0].itemList.addAll(newBannerItemList)
                        //gson解析，获得数据json字符串
                        val gson: Gson? = Gson()
                        val str = gson?.toJson(bannerHomeBean)
                        print("start: " + str + ":end")
                        //mvp架构，P层setHomeData通知view层执行页面逻辑
                        setHomeData(bannerHomeBean!!)
                    }

                }, { t ->
                    mRootView?.apply {
                        dismissLoading()
                    }
                })

        addSubscription(disposable)

    }

    /**
     * 下拉加载更多
     */
    override fun loadMoreData() {
        val disposable = nextPageUrl?.let {
            //根据 nextPageUrl 请求下一页数据
            homeViewModel.loadMoreData(it)
                    .subscribe({ homeBean ->
                        mRootView?.apply {
                            //过滤掉 Banner2(包含广告,等不需要的 Type), 具体查看接口分析
                            val newItemList = homeBean.issueList[0].itemList
                            newItemList.filter { item ->
                                item.type == "banner2" || item.type == "horizontalScrollCard"
                            }.forEach { item ->
                                //移除 item
                                newItemList.remove(item)
                            }
                            nextPageUrl = homeBean.nextPageUrl
                            setMoreData(newItemList)
                        }

                    }, { t ->
                        mRootView?.apply {
                            //Error异常系处理
                            //    showError(ExceptionHandle.handleException(t),ExceptionHandle.errorCode)
                        }
                    })


        }
        if (disposable != null) {
            addSubscription(disposable)
        }
    }


    /**
    第一次获取到的banner的数据源： bannerHomeBean，总共10条

    {
    "issueList":[
    {
    "count":10,
    "date":1610067600000,
    "itemList":[
    {
    "data":{
    "adTrack":[

    ],
    "author":{
    "description":"全球最有趣、最劲爆的超新鲜短视频收纳站！",
    "icon":"http://img.kaiyanapp.com/7c758fa328e8ac6c8615f72aed605df4.jpeg?imageMogr2/quality/60/format/jpg",
    "name":"特别·版本 Special Edition"
    },
    "category":"广告",
    "collected":false,
    "consumption":{
    "collectionCount":87,
    "replyCount":2,
    "shareCount":65
    },
    "cover":{
    "blurred":"http://img.kaiyanapp.com/c29b1a7cea39fde20bca01ea24614408.png?imageMogr2/quality/60/format/jpg",
    "detail":"http://img.kaiyanapp.com/7187c3de14b7ccbc6a61c7f55dd2eb49.png?imageMogr2/quality/60/format/jpg",
    "feed":"http://img.kaiyanapp.com/7187c3de14b7ccbc6a61c7f55dd2eb49.png?imageMogr2/quality/60/format/jpg",
    "homepage":"http://img.kaiyanapp.com/7187c3de14b7ccbc6a61c7f55dd2eb49.png?imageView2/1/w/720/h/560/format/jpg/q/75|watermark/1/image/aHR0cDovL2ltZy5rYWl5YW5hcHAuY29tL2JsYWNrXzMwLnBuZw==/dissolve/100/gravity/Center/dx/0/dy/0|imageslim"
    },
    "createTime":0,
    "dataType":"VideoBeanForClient",
    "date":1610067600000,
    "description":"Keep 在这则广告中将原本的口号「自律给我自由」改为「懒惰才自由」，并创造了懒神的形象，描绘了人们犯懒时背后的大魔王。懒神会在人们运动的时候出现，在暗中阻止他们。影片以搞笑的方式提醒人们直面懒惰、坚持自律，在新的一年向懒神宣战。From Keep",
    "descriptionEditor":"Keep 在这则广告中将原本的口号「自律给我自由」改为「懒惰才自由」，并创造了懒神的形象，描绘了人们犯懒时背后的大魔王。懒神会在人们运动的时候出现，在暗中阻止他们。影片以搞笑的方式提醒人们直面懒惰、坚持自律，在新的一年向懒神宣战。From Keep",
    "descriptionPgc":"",
    "duration":210,
    "id":229971,
    "idx":0,
    "labelList":[

    ],
    "library":"DAILY",
    "likeCount":0,
    "playInfo":[

    ],
    "playUrl":"http://baobab.kaiyanapp.com/api/v1/playUrl?vid=229971&resourceType=video&editionType=default&source=aliyun&playUrlType=url_oss&udid=",
    "played":false,
    "provider":{
    "alias":"PGC2",
    "icon":"",
    "name":"投稿"
    },
    "remark":"立好的 flag 总是倒，原来是他惹的祸",
    "slogan":"",
    "subtitles":[

    ],
    "tags":[
    {
    "actionUrl":"eyepetizer://tag/748/?title=%E8%BF%99%E4%BA%9B%E5%B9%BF%E5%91%8A%E8%B6%85%E6%9C%89%E6%A2%97",
    "id":748,
    "name":"这些广告超有梗"
    },
    {
    "actionUrl":"eyepetizer://tag/766/?title=%E8%84%91%E6%B4%9E%E5%B9%BF%E5%91%8A",
    "id":766,
    "name":"脑洞广告"
    },
    {
    "actionUrl":"eyepetizer://tag/490/?title=%E8%84%91%E6%B4%9E",
    "id":490,
    "name":"脑洞"
    },
    {
    "actionUrl":"eyepetizer://tag/1022/?title=%E8%BF%90%E5%8A%A8",
    "id":1022,
    "name":"运动"
    },
    {
    "actionUrl":"eyepetizer://tag/140/?title=%E6%90%9E%E7%AC%91",
    "id":140,
    "name":"搞笑"
    },
    {
    "actionUrl":"eyepetizer://tag/170/?title=%E5%B9%BD%E9%BB%98",
    "id":170,
    "name":"幽默"
    },
    {
    "actionUrl":"eyepetizer://tag/162/?title=%E6%97%A0%E5%8E%98%E5%A4%B4",
    "id":162,
    "name":"无厘头"
    },
    {
    "actionUrl":"eyepetizer://tag/2/?title=%E5%88%9B%E6%84%8F",
    "id":2,
    "name":"创意"
    },
    {
    "actionUrl":"eyepetizer://tag/16/?title=%E5%B9%BF%E5%91%8A",
    "id":16,
    "name":"广告"
    }
    ],
    "thumbPlayUrl":"",
    "title":"Keep 的这碗毒鸡汤，喝还是不喝？",
    "titlePgc":"",
    "type":"NORMAL",
    "webUrl":{
    "forWeibo":"https://m.eyepetizer.net/u1/video-detail?video_id=229971&resource_type=video&utm_campaign=routine&utm_medium=share&utm_source=weibo&uid=0",
    "raw":"http://www.eyepetizer.net/detail.html?vid=229971"
    }
    },
    "type":"video"
    },
    {
    "data":{
    "adTrack":[

    ],
    "author":{
    "description":"有趣的人永远不缺童心",
    "icon":"http://img.kaiyanapp.com/482c741c06644f5566c7218096dbaf26.jpeg",
    "name":"开眼动画精选"
    },
    "category":"动画",
    "collected":false,
    "consumption":{
    "collectionCount":91,
    "replyCount":0,
    "shareCount":18
    },
    "cover":{
    "blurred":"http://img.kaiyanapp.com/d3fcff863b6081c3311c96903c7ef789.png?imageMogr2/quality/60/format/jpg",
    "detail":"http://img.kaiyanapp.com/bd595cb62facd515092070bc3723cf92.png?imageMogr2/quality/60/format/jpg",
    "feed":"http://img.kaiyanapp.com/bd595cb62facd515092070bc3723cf92.png?imageMogr2/quality/60/format/jpg",
    "homepage":"http://img.kaiyanapp.com/bd595cb62facd515092070bc3723cf92.png?imageView2/1/w/720/h/560/format/jpg/q/75|watermark/1/image/aHR0cDovL2ltZy5rYWl5YW5hcHAuY29tL2JsYWNrXzMwLnBuZw==/dissolve/100/gravity/Center/dx/0/dy/0|imageslim"
    },
    "createTime":0,
    "dataType":"VideoBeanForClient",
    "date":1610067600000,
    "description":"Melanie Gohin 是一名动作设计师和二维动画师，作为一名自由职业者，她不断开发自己的项目，发现新的创作灵感。Melanie 的这则动画以抽象的方式，描绘了一个动人的爱情故事，讲述了两人互相陪伴，成为彼此的缪斯。From Melanie Gohin",
    "descriptionEditor":"Melanie Gohin 是一名动作设计师和二维动画师，作为一名自由职业者，她不断开发自己的项目，发现新的创作灵感。Melanie 的这则动画以抽象的方式，描绘了一个动人的爱情故事，讲述了两人互相陪伴，成为彼此的缪斯。From Melanie Gohin",
    "duration":90,
    "id":229948,
    "idx":0,
    "labelList":[

    ],
    "library":"DAILY",
    "likeCount":0,
    "playInfo":[
    {
    "name":"高清",
    "type":"high",
    "url":"http://baobab.kaiyanapp.com/api/v1/playUrl?vid=229948&resourceType=video&editionType=high&source=aliyun&playUrlType=url_oss&udid=",
    "urlList":[
    {
    "size":6160939
    },
    {
    "size":6160939
    }
    ]
    }
    ],
    "playUrl":"http://baobab.kaiyanapp.com/api/v1/playUrl?vid=229948&resourceType=video&editionType=default&source=aliyun&playUrlType=url_oss&udid=",
    "played":false,
    "provider":{
    "alias":"vimeo",
    "icon":"http://img.kaiyanapp.com/c3ad630be461cbb081649c9e21d6cbe3.png",
    "name":"Vimeo"
    },
    "subtitles":[

    ],
    "tags":[
    {
    "actionUrl":"eyepetizer://tag/14/?title=%E5%8A%A8%E7%94%BB%E6%A2%A6%E5%B7%A5%E5%8E%82",
    "id":14,
    "name":"动画梦工厂"
    },
    {
    "actionUrl":"eyepetizer://tag/202/?title=%E5%94%AF%E7%BE%8E",
    "id":202,
    "name":"唯美"
    },
    {
    "actionUrl":"eyepetizer://tag/160/?title=%E8%99%90%E7%8B%97",
    "id":160,
    "name":"虐狗"
    },
    {
    "actionUrl":"eyepetizer://tag/2/?title=%E5%88%9B%E6%84%8F",
    "id":2,
    "name":"创意"
    },
    {
    "actionUrl":"eyepetizer://tag/176/?title=%E6%84%9F%E4%BA%BA",
    "id":176,
    "name":"感人"
    },
    {
    "actionUrl":"eyepetizer://tag/174/?title=%E6%B2%BB%E6%84%88",
    "id":174,
    "name":"治愈"
    },
    {
    "actionUrl":"eyepetizer://tag/108/?title=%E7%88%B1%E6%83%85",
    "id":108,
    "name":"爱情"
    },
    {
    "actionUrl":"eyepetizer://tag/1023/?title=%E5%8A%A8%E7%94%BB",
    "id":1023,
    "name":"动画"
    }
    ],
    "title":"「未完待续」，是我听过最美的情书",
    "type":"NORMAL",
    "webUrl":{
    "forWeibo":"https://m.eyepetizer.net/u1/video-detail?video_id=229948&resource_type=video&utm_campaign=routine&utm_medium=share&utm_source=weibo&uid=0",
    "raw":"http://www.eyepetizer.net/detail.html?vid=229948"
    }
    },
    "type":"video"
    },
    {
    "data":{
    "adTrack":[

    ],
    "author":{
    "description":"发现世界的奇妙和辽阔",
    "icon":"http://img.kaiyanapp.com/75bc791c5f6cc239d6056e0a52d077fd.jpeg?imageMogr2/quality/60/format/jpg",
    "name":"开眼旅行精选"
    },
    "category":"旅行",
    "collected":false,
    "consumption":{
    "collectionCount":50,
    "replyCount":1,
    "shareCount":0
    },
    "cover":{
    "blurred":"http://img.kaiyanapp.com/1739fda239d42f0aa0c00924b628e75e.png?imageMogr2/quality/60/format/jpg",
    "detail":"http://img.kaiyanapp.com/b402f57487f8162eec2a8d9f0940aaeb.png?imageMogr2/quality/60/format/jpg",
    "feed":"http://img.kaiyanapp.com/b402f57487f8162eec2a8d9f0940aaeb.png?imageMogr2/quality/60/format/jpg",
    "homepage":"http://img.kaiyanapp.com/b402f57487f8162eec2a8d9f0940aaeb.png?imageView2/1/w/720/h/560/format/jpg/q/75|watermark/1/image/aHR0cDovL2ltZy5rYWl5YW5hcHAuY29tL2JsYWNrXzMwLnBuZw==/dissolve/100/gravity/Center/dx/0/dy/0|imageslim"
    },
    "createTime":0,
    "dataType":"VideoBeanForClient",
    "date":1610067600000,
    "description":"索菲亚是保加利亚的首都，这个城市被山腰包围，这也让它成为继安道尔拉维拉和马德里之后，欧洲第三高的首都。在这个移轴延时视频中，无人机带领我们领略卫星尺寸下的索菲亚，展示城市的建筑街道，以及川流不息的车和来来往往的人。From Joerg Daiber",
    "descriptionEditor":"索菲亚是保加利亚的首都，这个城市被山腰包围，这也让它成为继安道尔拉维拉和马德里之后，欧洲第三高的首都。在这个移轴延时视频中，无人机带领我们领略卫星尺寸下的索菲亚，展示城市的建筑街道，以及川流不息的车和来来往往的人。From Joerg Daiber",
    "duration":151,
    "id":229936,
    "idx":0,
    "labelList":[

    ],
    "library":"DAILY",
    "likeCount":0,
    "playInfo":[
    {
    "name":"高清",
    "type":"high",
    "url":"http://baobab.kaiyanapp.com/api/v1/playUrl?vid=229936&resourceType=video&editionType=high&source=aliyun&playUrlType=url_oss&udid=",
    "urlList":[
    {
    "size":31220754
    },
    {
    "size":31220754
    }
    ]
    }
    ],
    "playUrl":"http://baobab.kaiyanapp.com/api/v1/playUrl?vid=229936&resourceType=video&editionType=default&source=aliyun&playUrlType=url_oss&udid=",
    "played":false,
    "provider":{
    "alias":"vimeo",
    "icon":"http://img.kaiyanapp.com/c3ad630be461cbb081649c9e21d6cbe3.png",
    "name":"Vimeo"
    },
    "subtitles":[

    ],
    "tags":[
    {
    "actionUrl":"eyepetizer://tag/10/?title=%E8%B7%9F%E7%9D%80%E5%BC%80%E7%9C%BC%E7%9C%8B%E4%B8%96%E7%95%8C",
    "id":10,
    "name":"跟着开眼看世界"
    },
    {
    "actionUrl":"eyepetizer://tag/24/?title=%E8%AE%B0%E5%BD%95",
    "id":24,
    "name":"记录"
    },
    {
    "actionUrl":"eyepetizer://tag/702/?title=%E4%BA%BA%E7%89%A9",
    "id":702,
    "name":"人物"
    },
    {
    "actionUrl":"eyepetizer://tag/1099/?title=%E5%9F%8E%E5%B8%82",
    "id":1099,
    "name":"城市"
    },
    {
    "actionUrl":"eyepetizer://tag/368/?title=%E4%B8%9C%E6%AC%A7",
    "id":368,
    "name":"东欧"
    },
    {
    "actionUrl":"eyepetizer://tag/370/?title=%E6%AC%A7%E6%B4%B2",
    "id":370,
    "name":"欧洲"
    },
    {
    "actionUrl":"eyepetizer://tag/78/?title=%E5%BB%B6%E6%97%B6",
    "id":78,
    "name":"延时"
    },
    {
    "actionUrl":"eyepetizer://tag/80/?title=%E7%A7%BB%E8%BD%B4",
    "id":80,
    "name":"移轴"
    },
    {
    "actionUrl":"eyepetizer://tag/721/?title=%E6%97%85%E8%A1%8C%E7%81%B5%E6%84%9F",
    "id":721,
    "name":"旅行灵感"
    }
    ],
    "title":"移轴摄影大神，让这个东欧城市变成小人国",
    "type":"NORMAL",
    "webUrl":{
    "forWeibo":"https://m.eyepetizer.net/u1/video-detail?video_id=229936&resource_type=video&utm_campaign=routine&utm_medium=share&utm_source=weibo&uid=0",
    "raw":"http://www.eyepetizer.net/detail.html?vid=229936"
    }
    },
    "type":"video"
    },
    {
    "data":{
    "adTrack":[

    ],
    "author":{
    "description":"关于电影、剧集的一切",
    "icon":"http://img.kaiyanapp.com/003829087e85ce7310b2210d9575ce67.jpeg",
    "name":"开眼影视精选"
    },
    "category":"影视",
    "collected":false,
    "consumption":{
    "collectionCount":40,
    "replyCount":0,
    "shareCount":0
    },
    "cover":{
    "blurred":"http://img.kaiyanapp.com/8d0a487517451fc937b87be0c58a3e7d.jpeg?imageMogr2/quality/60/format/jpg",
    "detail":"http://img.kaiyanapp.com/54b90104aa9bda83995949f758952fae.jpeg?imageMogr2/quality/60/format/jpg",
    "feed":"http://img.kaiyanapp.com/54b90104aa9bda83995949f758952fae.jpeg?imageMogr2/quality/60/format/jpg",
    "homepage":"http://img.kaiyanapp.com/54b90104aa9bda83995949f758952fae.jpeg?imageView2/1/w/720/h/560/format/jpg/q/75|watermark/1/image/aHR0cDovL2ltZy5rYWl5YW5hcHAuY29tL2JsYWNrXzMwLnBuZw==/dissolve/100/gravity/Center/dx/0/dy/0|imageslim"
    },
    "createTime":0,
    "dataType":"VideoBeanForClient",
    "date":1610067600000,
    "description":"Pablo Ferro 是一名美国古巴裔的平面及电影标题设计师，也是 Pablo Ferro 电影公司的创始人。他曾参与多部经典电影的制作，包括「奇爱博士」、「发条橙」等。本片作者 Florent Tarrieux 受 Pablo Ferro 的委托，将他最好的作品快剪成一个短片。这段视频包括了他被电影公司拒绝的镜头，以及他早期的广告、预告片和电影的片尾曲。From Florent Tarrieux",
    "descriptionEditor":"Pablo Ferro 是一名美国古巴裔的平面及电影标题设计师，也是 Pablo Ferro 电影公司的创始人。他曾参与多部经典电影的制作，包括「奇爱博士」、「发条橙」等。本片作者 Florent Tarrieux 受 Pablo Ferro 的委托，将他最好的作品快剪成一个短片。这段视频包括了他被电影公司拒绝的镜头，以及他早期的广告、预告片和电影的片尾曲。From Florent Tarrieux",
    "duration":274,
    "id":229879,
    "idx":0,
    "labelList":[

    ],
    "library":"DAILY",
    "likeCount":0,
    "playInfo":[
    {
    "name":"高清",
    "type":"high",
    "url":"http://baobab.kaiyanapp.com/api/v1/playUrl?vid=229879&resourceType=video&editionType=high&source=aliyun&playUrlType=url_oss&udid=",
    "urlList":[
    {
    "size":39907071
    },
    {
    "size":39907071
    }
    ]
    }
    ],
    "playUrl":"http://baobab.kaiyanapp.com/api/v1/playUrl?vid=229879&resourceType=video&editionType=default&source=aliyun&playUrlType=url_oss&udid=",
    "played":false,
    "provider":{
    "alias":"vimeo",
    "icon":"http://img.kaiyanapp.com/c3ad630be461cbb081649c9e21d6cbe3.png",
    "name":"Vimeo"
    },
    "subtitles":[

    ],
    "tags":[
    {
    "actionUrl":"eyepetizer://tag/796/?title=%E8%BF%B7%E5%BD%B1%E6%94%BE%E6%98%A0%E5%AE%A4",
    "id":796,
    "name":"迷影放映室"
    },
    {
    "actionUrl":"eyepetizer://tag/684/?title=%E8%AE%BE%E8%AE%A1",
    "id":684,
    "name":"设计"
    },
    {
    "actionUrl":"eyepetizer://tag/24/?title=%E8%AE%B0%E5%BD%95",
    "id":24,
    "name":"记录"
    },
    {
    "actionUrl":"eyepetizer://tag/594/?title=%E7%A5%9E%E5%89%AA%E8%BE%91",
    "id":594,
    "name":"神剪辑"
    },
    {
    "actionUrl":"eyepetizer://tag/36/?title=%E9%9B%86%E9%94%A6",
    "id":36,
    "name":"集锦"
    },
    {
    "actionUrl":"eyepetizer://tag/34/?title=%E6%B7%B7%E5%89%AA",
    "id":34,
    "name":"混剪"
    },
    {
    "actionUrl":"eyepetizer://tag/570/?title=%E7%94%B5%E5%BD%B1%E7%9B%B8%E5%85%B3",
    "id":570,
    "name":"电影相关"
    }
    ],
    "title":"这位古巴裔导演不能播的镜头，才是精华",
    "type":"NORMAL",
    "webUrl":{
    "forWeibo":"https://m.eyepetizer.net/u1/video-detail?video_id=229879&resource_type=video&utm_campaign=routine&utm_medium=share&utm_source=weibo&uid=0",
    "raw":"http://www.eyepetizer.net/detail.html?vid=229879"
    }
    },
    "type":"video"
    },
    {
    "data":{
    "adTrack":[

    ],
    "author":{
    "description":"关于电影、剧集的一切",
    "icon":"http://img.kaiyanapp.com/003829087e85ce7310b2210d9575ce67.jpeg",
    "name":"开眼影视精选"
    },
    "category":"影视",
    "collected":false,
    "consumption":{
    "collectionCount":80,
    "replyCount":0,
    "shareCount":0
    },
    "cover":{
    "blurred":"http://img.kaiyanapp.com/13311199defc84fd7523331ef3e2af17.jpeg?imageMogr2/quality/60/format/jpg",
    "detail":"http://img.kaiyanapp.com/21d2924fb1b56e237ce69dcb1fc943cd.jpeg?imageMogr2/quality/60/format/jpg",
    "feed":"http://img.kaiyanapp.com/21d2924fb1b56e237ce69dcb1fc943cd.jpeg?imageMogr2/quality/60/format/jpg",
    "homepage":"http://img.kaiyanapp.com/21d2924fb1b56e237ce69dcb1fc943cd.jpeg?imageView2/1/w/720/h/560/format/jpg/q/75|watermark/1/image/aHR0cDovL2ltZy5rYWl5YW5hcHAuY29tL2JsYWNrXzMwLnBuZw==/dissolve/100/gravity/Center/dx/0/dy/0|imageslim"
    },
    "createTime":0,
    "dataType":"VideoBeanForClient",
    "date":1610067600000,
    "description":"2020 对许多院线大片来说是艰难的一年，但却给了小电影很多机会。Screened 在这则视频中，总结了 11 部他最喜欢的影片，并讲解了他在不同层面上喜欢这些电影的原因。作者将这些影片分成三大类：现实主义、超现实主义和人们想要逃离的故事，分别讲述了每个类别电影的精彩之处。From Screened",
    "descriptionEditor":"2020 对许多院线大片来说是艰难的一年，但却给了小电影很多机会。Screened 在这则视频中，总结了 11 部他最喜欢的影片，并讲解了他在不同层面上喜欢这些电影的原因。作者将这些影片分成三大类：现实主义、超现实主义和人们想要逃离的故事，分别讲述了每个类别电影的精彩之处。From Screened",
    "duration":1161,
    "id":229753,
    "idx":0,
    "labelList":[

    ],
    "library":"DAILY",
    "likeCount":0,
    "playInfo":[
    {
    "name":"高清",
    "type":"high",
    "url":"http://baobab.kaiyanapp.com/api/v1/playUrl?vid=229753&resourceType=video&editionType=high&source=aliyun&playUrlType=url_oss&udid=",
    "urlList":[
    {
    "size":123257163
    },
    {
    "size":123257163
    }
    ]
    }
    ],
    "playUrl":"http://baobab.kaiyanapp.com/api/v1/playUrl?vid=229753&resourceType=video&editionType=default&source=aliyun&playUrlType=url_oss&udid=",
    "played":false,
    "provider":{
    "alias":"youtube",
    "icon":"http://img.kaiyanapp.com/fa20228bc5b921e837156923a58713f6.png",
    "name":"YouTube"
    },
    "subtitles":[

    ],
    "tags":[
    {
    "actionUrl":"eyepetizer://tag/796/?title=%E8%BF%B7%E5%BD%B1%E6%94%BE%E6%98%A0%E5%AE%A4",
    "id":796,
    "name":"迷影放映室"
    },
    {
    "actionUrl":"eyepetizer://tag/976/?title=%E5%AE%89%E5%88%A9%E4%B8%80%E4%B8%AA%E7%94%B5%E5%BD%B1",
    "id":976,
    "name":"安利一个电影"
    },
    {
    "actionUrl":"eyepetizer://tag/92/?title=%E8%B6%85%E7%8E%B0%E5%AE%9E",
    "id":92,
    "name":"超现实"
    },
    {
    "actionUrl":"eyepetizer://tag/570/?title=%E7%94%B5%E5%BD%B1%E7%9B%B8%E5%85%B3",
    "id":570,
    "name":"电影相关"
    },
    {
    "actionUrl":"eyepetizer://tag/48/?title=%E7%9B%98%E7%82%B9",
    "id":48,
    "name":"盘点"
    },
    {
    "actionUrl":"eyepetizer://tag/36/?title=%E9%9B%86%E9%94%A6",
    "id":36,
    "name":"集锦"
    },
    {
    "actionUrl":"eyepetizer://tag/24/?title=%E8%AE%B0%E5%BD%95",
    "id":24,
    "name":"记录"
    }
    ],
    "title":"11 部 2020 最佳电影总结，绝对不想错过",
    "type":"NORMAL",
    "webUrl":{
    "forWeibo":"https://m.eyepetizer.net/u1/video-detail?video_id=229753&resource_type=video&utm_campaign=routine&utm_medium=share&utm_source=weibo&uid=0",
    "raw":"http://www.eyepetizer.net/detail.html?vid=229753"
    }
    },
    "type":"video"
    },
    {
    "data":{
    "adTrack":[

    ],
    "author":{
    "description":"全球最酷、最炫、最有态度的音乐集合",
    "icon":"http://img.kaiyanapp.com/9279c17b4da5ba5e7e4f21afb5bb0a74.jpeg",
    "name":"开眼音乐精选"
    },
    "category":"音乐",
    "collected":false,
    "consumption":{
    "collectionCount":80,
    "replyCount":0,
    "shareCount":9
    },
    "cover":{
    "blurred":"http://img.kaiyanapp.com/807adfd2d940d2b9d16a3f0f89af44a0.png?imageMogr2/quality/60/format/jpg",
    "detail":"http://img.kaiyanapp.com/0cd1955a604f24a13573edce7810e03b.png?imageMogr2/quality/60/format/jpg",
    "feed":"http://img.kaiyanapp.com/0cd1955a604f24a13573edce7810e03b.png?imageMogr2/quality/60/format/jpg",
    "homepage":"http://img.kaiyanapp.com/0cd1955a604f24a13573edce7810e03b.png?imageView2/1/w/720/h/560/format/jpg/q/75|watermark/1/image/aHR0cDovL2ltZy5rYWl5YW5hcHAuY29tL2JsYWNrXzMwLnBuZw==/dissolve/100/gravity/Center/dx/0/dy/0|imageslim"
    },
    "createTime":0,
    "dataType":"VideoBeanForClient",
    "date":1610067600000,
    "description":"这则视频是日本歌手 Cuushe 歌曲 Magic 的 MV，来自即将发行的专辑「WAKEN」，由田岛太雄执导。MV 中加入了插画师久野遥子的精美动画，将动画融入进现实的风景当中，更衬托出 Cuushe 歌声的空灵，也将歌曲与景色融为一体。From tao tajima",
    "descriptionEditor":"这则视频是日本歌手 Cuushe 歌曲 Magic 的 MV，来自即将发行的专辑「WAKEN」，由田岛太雄执导。MV 中加入了插画师久野遥子的精美动画，将动画融入进现实的风景当中，更衬托出 Cuushe 歌声的空灵，也将歌曲与景色融为一体。From tao tajima",
    "duration":227,
    "id":229066,
    "idx":0,
    "labelList":[

    ],
    "library":"DAILY",
    "likeCount":0,
    "playInfo":[

    ],
    "playUrl":"http://baobab.kaiyanapp.com/api/v1/playUrl?vid=229066&resourceType=video&editionType=default&source=aliyun&playUrlType=url_oss&udid=",
    "played":false,
    "provider":{
    "alias":"vimeo",
    "icon":"http://img.kaiyanapp.com/c3ad630be461cbb081649c9e21d6cbe3.png",
    "name":"Vimeo"
    },
    "subtitles":[

    ],
    "tags":[
    {
    "actionUrl":"eyepetizer://tag/18/?title=%E9%9F%B3%E4%B9%90%E7%94%B5%E5%8F%B0",
    "id":18,
    "name":"音乐电台"
    },
    {
    "actionUrl":"eyepetizer://tag/350/?title=%E6%97%A5%E6%9C%AC",
    "id":350,
    "name":"日本"
    },
    {
    "actionUrl":"eyepetizer://tag/836/?title=%E5%8A%A8%E7%94%BB%20MV",
    "id":836,
    "name":"动画 MV"
    },
    {
    "actionUrl":"eyepetizer://tag/1023/?title=%E5%8A%A8%E7%94%BB",
    "id":1023,
    "name":"动画"
    },
    {
    "actionUrl":"eyepetizer://tag/801/?title=%E5%88%9B%E6%84%8F%20MV",
    "id":801,
    "name":"创意 MV"
    },
    {
    "actionUrl":"eyepetizer://tag/747/?title=MV",
    "id":747,
    "name":"MV"
    },
    {
    "actionUrl":"eyepetizer://tag/174/?title=%E6%B2%BB%E6%84%88",
    "id":174,
    "name":"治愈"
    },
    {
    "actionUrl":"eyepetizer://tag/704/?title=%E6%B5%81%E8%A1%8C%E9%9F%B3%E4%B9%90",
    "id":704,
    "name":"流行音乐"
    }
    ],
    "title":"这个日本歌手的 MV，用漫画治愈现实",
    "type":"NORMAL",
    "webUrl":{
    "forWeibo":"http://www.eyepetizer.net/detail.html?vid=229066",
    "raw":"http://www.eyepetizer.net/detail.html?vid=229066"
    }
    },
    "type":"video"
    },
    {
    "data":{
    "adTrack":[

    ],
    "author":{
    "description":"技术与审美结合，探索视觉的无限可能",
    "icon":"http://img.kaiyanapp.com/f4a9aba1c6857ee0cefcdc5aee0a1fc9.png?imageMogr2/quality/60/format/jpg",
    "name":"开眼创意精选"
    },
    "category":"创意",
    "collected":false,
    "consumption":{
    "collectionCount":10,
    "replyCount":0,
    "shareCount":0
    },
    "cover":{
    "blurred":"http://img.kaiyanapp.com/431d3626c9cba03beffb157c4808e25f.png?imageMogr2/quality/60/format/jpg",
    "detail":"http://img.kaiyanapp.com/55c0b2aa861f3cb5307c2a3bf8b38220.png?imageMogr2/quality/60/format/jpg",
    "feed":"http://img.kaiyanapp.com/55c0b2aa861f3cb5307c2a3bf8b38220.png?imageMogr2/quality/60/format/jpg",
    "homepage":"http://img.kaiyanapp.com/55c0b2aa861f3cb5307c2a3bf8b38220.png?imageView2/1/w/720/h/560/format/jpg/q/75|watermark/1/image/aHR0cDovL2ltZy5rYWl5YW5hcHAuY29tL2JsYWNrXzMwLnBuZw==/dissolve/100/gravity/Center/dx/0/dy/0|imageslim"
    },
    "createTime":0,
    "dataType":"VideoBeanForClient",
    "date":1610067600000,
    "description":"这则视频捕捉了伦敦 Whitechapel 地区的行人面貌。影片聚焦在了人们的面孔和行为上，我们能看到来自各个种族、地区的人，它为观众展示了这个社区的文化多样性。BGM 是由 Four Tet 演唱的 Lion，影片使用 5D Mark III 拍摄。From Finn Keenan",
    "descriptionEditor":"这则视频捕捉了伦敦 Whitechapel 地区的行人面貌。影片聚焦在了人们的面孔和行为上，我们能看到来自各个种族、地区的人，它为观众展示了这个社区的文化多样性。BGM 是由 Four Tet 演唱的 Lion，影片使用 5D Mark III 拍摄。From Finn Keenan",
    "duration":173,
    "id":229564,
    "idx":0,
    "labelList":[

    ],
    "library":"DAILY",
    "likeCount":0,
    "playInfo":[

    ],
    "playUrl":"http://baobab.kaiyanapp.com/api/v1/playUrl?vid=229564&resourceType=video&editionType=default&source=aliyun&playUrlType=url_oss&udid=",
    "played":false,
    "provider":{
    "alias":"vimeo",
    "icon":"http://img.kaiyanapp.com/c3ad630be461cbb081649c9e21d6cbe3.png",
    "name":"Vimeo"
    },
    "subtitles":[

    ],
    "tags":[
    {
    "actionUrl":"eyepetizer://tag/744/?title=%E6%AF%8F%E6%97%A5%E5%88%9B%E6%84%8F%E7%81%B5%E6%84%9F",
    "id":744,
    "name":"每日创意灵感"
    },
    {
    "actionUrl":"eyepetizer://tag/702/?title=%E4%BA%BA%E7%89%A9",
    "id":702,
    "name":"人物"
    },
    {
    "actionUrl":"eyepetizer://tag/534/?title=%E4%BA%BA%E6%96%87",
    "id":534,
    "name":"人文"
    },
    {
    "actionUrl":"eyepetizer://tag/864/?title=%E4%BA%BA%E6%96%87%E7%A4%BE%E7%A7%91",
    "id":864,
    "name":"人文社科"
    },
    {
    "actionUrl":"eyepetizer://tag/1264/?title=%E6%96%87%E5%8C%96%E8%89%BA%E6%9C%AF",
    "id":1264,
    "name":"文化艺术"
    },
    {
    "actionUrl":"eyepetizer://tag/1153/?title=%E4%BC%97%E7%94%9F%E7%9B%B8",
    "id":1153,
    "name":"众生相"
    },
    {
    "actionUrl":"eyepetizer://tag/2/?title=%E5%88%9B%E6%84%8F",
    "id":2,
    "name":"创意"
    }
    ],
    "title":"伦敦多文化社区群像，聚焦我们「每个人」",
    "type":"NORMAL",
    "webUrl":{
    "forWeibo":"https://m.eyepetizer.net/u1/video-detail?video_id=229564&resource_type=video&utm_campaign=routine&utm_medium=share&utm_source=weibo&uid=0",
    "raw":"http://www.eyepetizer.net/detail.html?vid=229564"
    }
    },
    "type":"video"
    },
    {
    "data":{
    "adTrack":[

    ],
    "author":{
    "description":"技术与审美结合，探索视觉的无限可能",
    "icon":"http://img.kaiyanapp.com/f4a9aba1c6857ee0cefcdc5aee0a1fc9.png?imageMogr2/quality/60/format/jpg",
    "name":"开眼创意精选"
    },
    "category":"创意",
    "collected":false,
    "consumption":{
    "collectionCount":10,
    "replyCount":0,
    "shareCount":0
    },
    "cover":{
    "blurred":"http://img.kaiyanapp.com/a473b6ab86cadcda1f383650e4fe6890.png?imageMogr2/quality/60/format/jpg",
    "detail":"http://img.kaiyanapp.com/7348134d0a0b43665bb7020114adee7b.png?imageMogr2/quality/60/format/jpg",
    "feed":"http://img.kaiyanapp.com/7348134d0a0b43665bb7020114adee7b.png?imageMogr2/quality/60/format/jpg",
    "homepage":"http://img.kaiyanapp.com/7348134d0a0b43665bb7020114adee7b.png?imageView2/1/w/720/h/560/format/jpg/q/75|watermark/1/image/aHR0cDovL2ltZy5rYWl5YW5hcHAuY29tL2JsYWNrXzMwLnBuZw==/dissolve/100/gravity/Center/dx/0/dy/0|imageslim"
    },
    "createTime":0,
    "dataType":"VideoBeanForClient",
    "date":1610067600000,
    "description":"英国女装品牌 Luna Del Pinal 的 2021 春夏系列由西班牙导演 Mariano Schoendorff 创作，讲述了一个女子不断逃离，但又无法摆脱另一个人的故事。影片营造出一种神秘的悬疑氛围，而女子想要逃离的那个人，其实只是她脑海中来自记忆深处的自己。From Mariano Schoendorff",
    "descriptionEditor":"英国女装品牌 Luna Del Pinal 的 2021 春夏系列由西班牙导演 Mariano Schoendorff 创作，讲述了一个女子不断逃离，但又无法摆脱另一个人的故事。影片营造出一种神秘的悬疑氛围，而女子想要逃离的那个人，其实只是她脑海中来自记忆深处的自己。From Mariano Schoendorff",
    "duration":260,
    "id":225842,
    "idx":0,
    "labelList":[

    ],
    "library":"DAILY",
    "likeCount":0,
    "playInfo":[

    ],
    "playUrl":"http://baobab.kaiyanapp.com/api/v1/playUrl?vid=225842&resourceType=video&editionType=default&source=aliyun&playUrlType=url_oss&udid=",
    "played":false,
    "provider":{
    "alias":"vimeo",
    "icon":"http://img.kaiyanapp.com/c3ad630be461cbb081649c9e21d6cbe3.png",
    "name":"Vimeo"
    },
    "subtitles":[

    ],
    "tags":[
    {
    "actionUrl":"eyepetizer://tag/744/?title=%E6%AF%8F%E6%97%A5%E5%88%9B%E6%84%8F%E7%81%B5%E6%84%9F",
    "id":744,
    "name":"每日创意灵感"
    },
    {
    "actionUrl":"eyepetizer://tag/202/?title=%E5%94%AF%E7%BE%8E",
    "id":202,
    "name":"唯美"
    },
    {
    "actionUrl":"eyepetizer://tag/124/?title=%E9%AD%94%E5%B9%BB",
    "id":124,
    "name":"魔幻"
    },
    {
    "actionUrl":"eyepetizer://tag/482/?title=%E4%BA%BA%E7%94%9F%E6%84%9F%E6%82%9F",
    "id":482,
    "name":"人生感悟"
    },
    {
    "actionUrl":"eyepetizer://tag/130/?title=%E6%82%AC%E7%96%91",
    "id":130,
    "name":"悬疑"
    },
    {
    "actionUrl":"eyepetizer://tag/992/?title=%E6%97%B6%E5%B0%9A%E7%BA%AA%E5%AE%9E%E7%B1%BB",
    "id":992,
    "name":"时尚纪实类"
    },
    {
    "actionUrl":"eyepetizer://tag/765/?title=%E6%97%B6%E5%B0%9A%E5%B9%BF%E5%91%8A",
    "id":765,
    "name":"时尚广告"
    },
    {
    "actionUrl":"eyepetizer://tag/26/?title=%E6%97%B6%E5%B0%9A",
    "id":26,
    "name":"时尚"
    },
    {
    "actionUrl":"eyepetizer://tag/2/?title=%E5%88%9B%E6%84%8F",
    "id":2,
    "name":"创意"
    }
    ],
    "title":"她从记忆中逃离，诠释时尚的神秘",
    "type":"NORMAL",
    "webUrl":{
    "forWeibo":"https://m.eyepetizer.net/u1/video-detail?video_id=225842&resource_type=video&utm_campaign=routine&utm_medium=share&utm_source=weibo&uid=0",
    "raw":"http://www.eyepetizer.net/detail.html?vid=225842"
    }
    },
    "type":"video"
    },
    {
    "data":{
    "adTrack":[

    ],
    "author":{
    "description":"一档满足百科知识好奇心的短视频节目，都是些你不知道的。想了解更多你不知道的知识，请在微信公众号搜索「混乱博物馆」，关注我们。",
    "icon":"http://img.kaiyanapp.com/5947bb2e8c24673c340bfaf2c9cc9e42.png?imageMogr2/quality/60/format/jpg",
    "name":"混乱博物馆"
    },
    "category":"科技",
    "collected":false,
    "consumption":{
    "collectionCount":19,
    "replyCount":1,
    "shareCount":0
    },
    "cover":{
    "blurred":"http://img.kaiyanapp.com/f93345815757ea74cc03be415466c2c8.jpeg?imageMogr2/quality/60/format/jpg",
    "detail":"http://img.kaiyanapp.com/101d8cbce0cbb94fa6a73d1467c63c5f.png?imageMogr2/quality/60/format/jpg",
    "feed":"http://img.kaiyanapp.com/101d8cbce0cbb94fa6a73d1467c63c5f.png?imageMogr2/quality/60/format/jpg"
    },
    "createTime":0,
    "dataType":"VideoBeanForClient",
    "date":1610067600000,
    "description":"2020 年的尾巴，我们发布了这支第 300 期特别篇。这是个充满了财富、贪婪、野蛮、鲜血、神秘与冒险勇气味道的旧故事，虽然虚无缥缈，但是是对人类当年历史的最好注解。",
    "descriptionEditor":"2020 年的尾巴，我们发布了这支第 300 期特别篇。这是个充满了财富、贪婪、野蛮、鲜血、神秘与冒险勇气味道的旧故事，虽然虚无缥缈，但是是对人类当年历史的最好注解。",
    "descriptionPgc":"2020年的尾巴，我们发布了这支第300期特别篇。这是个充满了财富、贪婪、野蛮、鲜血、神秘与冒险勇气味道的旧故事，虽然虚无缥缈，但是是对人类当年历史的最好注解。",
    "duration":514,
    "id":229159,
    "idx":0,
    "labelList":[

    ],
    "library":"DAILY",
    "likeCount":0,
    "playInfo":[

    ],
    "playUrl":"http://baobab.kaiyanapp.com/api/v1/playUrl?vid=229159&resourceType=video&editionType=default&source=aliyun&playUrlType=url_oss&udid=",
    "played":false,
    "provider":{
    "alias":"PGC",
    "icon":"",
    "name":"PGC"
    },
    "remark":"",
    "subtitles":[

    ],
    "tags":[
    {
    "actionUrl":"eyepetizer://tag/44/?title=5%20%E5%88%86%E9%92%9F%E6%96%B0%E7%9F%A5",
    "id":44,
    "name":"5 分钟新知"
    },
    {
    "actionUrl":"eyepetizer://tag/864/?title=%E4%BA%BA%E6%96%87%E7%A4%BE%E7%A7%91",
    "id":864,
    "name":"人文社科"
    },
    {
    "actionUrl":"eyepetizer://tag/534/?title=%E4%BA%BA%E6%96%87",
    "id":534,
    "name":"人文"
    },
    {
    "actionUrl":"eyepetizer://tag/24/?title=%E8%AE%B0%E5%BD%95",
    "id":24,
    "name":"记录"
    },
    {
    "actionUrl":"eyepetizer://tag/1024/?title=%E7%A7%91%E6%8A%80",
    "id":1024,
    "name":"科技"
    },
    {
    "actionUrl":"eyepetizer://tag/1250/?title=%E6%8E%A2%E7%B4%A2%E9%A2%91%E9%81%93",
    "id":1250,
    "name":"探索频道"
    },
    {
    "actionUrl":"eyepetizer://tag/1040/?title=%E7%A7%91%E6%99%AE",
    "id":1040,
    "name":"科普"
    },
    {
    "actionUrl":"eyepetizer://tag/572/?title=%E5%8E%86%E5%8F%B2",
    "id":572,
    "name":"历史"
    }
    ],
    "title":"寻找黄金国 300 期跨年特别篇丨混乱博物馆",
    "titlePgc":"寻找黄金国300期跨年特别篇丨混乱博物馆",
    "type":"NORMAL",
    "webUrl":{
    "forWeibo":"https://m.eyepetizer.net/u1/video-detail?video_id=229159&resource_type=video&utm_campaign=routine&utm_medium=share&utm_source=weibo&uid=0",
    "raw":"http://www.eyepetizer.net/detail.html?vid=229159"
    }
    },
    "type":"video"
    },
    {
    "data":{
    "adTrack":[

    ],
    "author":{
    "description":"开眼运动精选",
    "icon":"http://img.kaiyanapp.com/f2449da39a584c982866b0636bd30c58.png?imageMogr2/quality/60/format/jpg",
    "name":"开眼运动精选"
    },
    "category":"运动",
    "collected":false,
    "consumption":{
    "collectionCount":20,
    "replyCount":0,
    "shareCount":0
    },
    "cover":{
    "blurred":"http://img.kaiyanapp.com/4d73824fb3ab074bafc5d18067ec12ac.png?imageMogr2/quality/60/format/jpg",
    "detail":"http://img.kaiyanapp.com/efca7041445452df60b7e2fbeb0046a5.png?imageMogr2/quality/60/format/jpg",
    "feed":"http://img.kaiyanapp.com/efca7041445452df60b7e2fbeb0046a5.png?imageMogr2/quality/60/format/jpg",
    "homepage":"http://img.kaiyanapp.com/efca7041445452df60b7e2fbeb0046a5.png?imageView2/1/w/720/h/560/format/jpg/q/75|watermark/1/image/aHR0cDovL2ltZy5rYWl5YW5hcHAuY29tL2JsYWNrXzMwLnBuZw==/dissolve/100/gravity/Center/dx/0/dy/0|imageslim"
    },
    "createTime":0,
    "dataType":"VideoBeanForClient",
    "date":1610067600000,
    "description":"David Tharan 是目前法国唯一的 ATV 越野车自由式表演者，他与 YAMAHA 合作的这则视频，展示了他对这项运动的激情。在家人的鼓励下，David 不断朝着自己的目标前进。影片展示了越野车表演包括空翻在内的多种花样动作，十分惊心动魄。From Mat Bourgeois",
    "descriptionEditor":"David Tharan 是目前法国唯一的 ATV 越野车自由式表演者，他与 YAMAHA 合作的这则视频，展示了他对这项运动的激情。在家人的鼓励下，David 不断朝着自己的目标前进。影片展示了越野车表演包括空翻在内的多种花样动作，十分惊心动魄。From Mat Bourgeois",
    "duration":200,
    "id":227375,
    "idx":0,
    "labelList":[

    ],
    "library":"DAILY",
    "likeCount":0,
    "playInfo":[
    {
    "name":"高清",
    "type":"high",
    "url":"http://baobab.kaiyanapp.com/api/v1/playUrl?vid=227375&resourceType=video&editionType=high&source=aliyun&playUrlType=url_oss&udid=",
    "urlList":[
    {
    "size":33264027
    },
    {
    "size":33264027
    }
    ]
    }
    ],
    "playUrl":"http://baobab.kaiyanapp.com/api/v1/playUrl?vid=227375&resourceType=video&editionType=default&source=aliyun&playUrlType=url_oss&udid=",
    "played":false,
    "provider":{
    "alias":"vimeo",
    "icon":"http://img.kaiyanapp.com/c3ad630be461cbb081649c9e21d6cbe3.png",
    "name":"Vimeo"
    },
    "subtitles":[

    ],
    "tags":[
    {
    "actionUrl":"eyepetizer://tag/4/?title=%E8%BF%90%E5%8A%A8%E5%81%A5%E8%BA%AB",
    "id":4,
    "name":"运动健身"
    },
    {
    "actionUrl":"eyepetizer://tag/1394/?title=%E7%83%AD%E8%A1%80",
    "id":1394,
    "name":"热血"
    },
    {
    "actionUrl":"eyepetizer://tag/16/?title=%E5%B9%BF%E5%91%8A",
    "id":16,
    "name":"广告"
    },
    {
    "actionUrl":"eyepetizer://tag/508/?title=%E7%87%83",
    "id":508,
    "name":"燃"
    },
    {
    "actionUrl":"eyepetizer://tag/1022/?title=%E8%BF%90%E5%8A%A8",
    "id":1022,
    "name":"运动"
    },
    {
    "actionUrl":"eyepetizer://tag/296/?title=%E6%B1%BD%E8%BD%A6%E7%AB%9E%E6%8A%80",
    "id":296,
    "name":"汽车竞技"
    },
    {
    "actionUrl":"eyepetizer://tag/316/?title=%E8%8A%B1%E5%BC%8F%E8%BF%90%E5%8A%A8",
    "id":316,
    "name":"花式运动"
    },
    {
    "actionUrl":"eyepetizer://tag/681/?title=%E6%9E%81%E9%99%90%E8%BF%90%E5%8A%A8",
    "id":681,
    "name":"极限运动"
    }
    ],
    "title":"戴上头盔，和他进入赛车手的星球",
    "type":"NORMAL",
    "webUrl":{
    "forWeibo":"https://m.eyepetizer.net/u1/video-detail?video_id=227375&resource_type=video&utm_campaign=routine&utm_medium=share&utm_source=weibo&uid=0",
    "raw":"http://www.eyepetizer.net/detail.html?vid=227375"
    }
    },
    "type":"video"
    }
    ],
    "publishTime":1610067600000,
    "releaseTime":1610067600000,
    "total":0,
    "type":"morning"
    }
    ],
    "newestIssueType":"morning",
    "nextPageUrl":"http://baobab.kaiyanapp.com/api/v2/feed?date=1609981200000&num=1",
    "nextPublishTime":1610154000000
    }

    -------------

    第二次加上一页的page 数据：

    {
    "issueList":[
    {
    "count":10,
    "date":1610067600000,
    "itemList":Array[21],   //前10条是banner第一次获取的那些数据的值。
    "publishTime":1610067600000,
    "releaseTime":1610067600000,
    "total":0,
    "type":"morning"
    }
    ],
    "newestIssueType":"morning",
    "nextPageUrl":"http://baobab.kaiyanapp.com/api/v2/feed?date=1609981200000&num=1",
    "nextPublishTime":1610154000000
    }





     */

}
