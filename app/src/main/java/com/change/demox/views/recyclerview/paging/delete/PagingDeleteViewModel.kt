package com.change.demox.views.recyclerview.paging.delete

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.change.demox.views.recyclerview.paging.delete.bean.Book
import com.change.demox.views.recyclerview.paging.delete.usecase.GetBooksUseCase

class PagingDeleteViewModel(
        private val GetBooksUseCase: GetBooksUseCase
) : ViewModel() {

    lateinit var books: LiveData<PagedList<Book>>
    var deleteButtonEnable = MutableLiveData<Boolean>()
    /**
     * 获取数据源
     *
     * 数据采用的是网络API获取，用的是APIStub
     *
     */
    fun getDataList() {
        books = GetBooksUseCase.getBooks()
    }

    /**
     * 下拉刷新
     *
     */
    fun refresh() {
        GetBooksUseCase.refresh()
    }

    /**
     * 删除按钮的状态
     *
     * @property bookId　bookID
     */
    fun refreshDeleteButtonState(
            bookId: String? = "",
            checkState: HashMap<Int, Boolean>? = null
    ) {
        var i = 0
        //checkbox選択済み
        /**
         * 選択（いいえ->はい）
         * 選択されている場合は、代わりに選択します
         * 選択済み（はい->いいえ）
         * 何も選択されていない場合は、なしに変更します
         */
        if (TextUtils.isEmpty(bookId)) {
            while (i < books.value?.size!!) {
                books.value!![i]?.isChecked = false
                i++
            }
        }
        deleteButtonEnable.value = (checkState?.containsValue(true) == true)
    }


    /**
     *
     * 从网络取得的API数据如下，可以在APISTUB上复制粘贴修改数据内容：
     *
     *
     * {
    "total": 33,
    "books": [{
    "book_id": 38,
    "model_name": "EP-2",
    "remarks": "",
    "is_favorite": false,
    "category_id": 355,
    "category_division_code": 2,
    "categories": [{
    "category_id": 355,
    "category_name": "EP",
    "category_division_code": 2,
    "parent_category_id": 46,
    "sort_orders": [0, 0, 0]
    }, {
    "category_id": 46,
    "category_name": "播種機",
    "category_division_code": 2,
    "parent_category_id": 1,
    "sort_orders": [0, 0, 0]
    }, {
    "category_id": 1,
    "category_name": "アグリテクノ矢崎(ATY)",
    "category_division_code": 2,
    "parent_category_id": 0,
    "sort_orders": [0, 0, 0]
    }],
    "distributor_code": "",
    "pdfs": ["https://publicd6tn2upk7h3ce.blob.core.windows.net/relatedmaker/1/agritecno_EP-2_4.pdf?se=2020-10-06T09%3A40%3A06Z\u0026sig=EEFgi8vXox70%2FO60P4AjcRWOc8j30UTgwOuBQBqwieg%3D\u0026sp=r\u0026spr=https\u0026sr=b\u0026sv=2015-04-05"],
    "remarks_info": "合本ﾌｧｲﾙ：　EP-2・4"
    }, {
    "book_id": 40,
    "model_name": "EP-2N",
    "remarks": "",
    "is_favorite": false,
    "category_id": 355,
    "category_division_code": 2,
    "categories": [{
    "category_id": 355,
    "category_name": "EP",
    "category_division_code": 2,
    "parent_category_id": 46,
    "sort_orders": [0, 0, 0]
    }, {
    "category_id": 46,
    "category_name": "播種機",
    "category_division_code": 2,
    "parent_category_id": 1,
    "sort_orders": [0, 0, 0]
    }, {
    "category_id": 1,
    "category_name": "アグリテクノ矢崎(ATY)",
    "category_division_code": 2,
    "parent_category_id": 0,
    "sort_orders": [0, 0, 0]
    }],
    "distributor_code": "",
    "pdfs": ["https://publicd6tn2upk7h3ce.blob.core.windows.net/relatedmaker/1/agritecno_EP-2N.pdf?se=2020-10-06T09%3A40%3A06Z\u0026sig=cpd08HZxE%2B5UGkVo%2Bg%2BUapoXIX3rol2B2vFU%2FhUf2vU%3D\u0026sp=r\u0026spr=https\u0026sr=b\u0026sv=2015-04-05"]
    }, {
    "book_id": 39,
    "model_name": "EP-4",
    "remarks": "",
    "is_favorite": false,
    "category_id": 355,
    "category_division_code": 2,
    "categories": [{
    "category_id": 355,
    "category_name": "EP",
    "category_division_code": 2,
    "parent_category_id": 46,
    "sort_orders": [0, 0, 0]
    }, {
    "category_id": 46,
    "category_name": "播種機",
    "category_division_code": 2,
    "parent_category_id": 1,
    "sort_orders": [0, 0, 0]
    }, {
    "category_id": 1,
    "category_name": "アグリテクノ矢崎(ATY)",
    "category_division_code": 2,
    "parent_category_id": 0,
    "sort_orders": [0, 0, 0]
    }],
    "distributor_code": "",
    "pdfs": ["https://publicd6tn2upk7h3ce.blob.core.windows.net/relatedmaker/1/agritecno_EP-2_4.pdf?se=2020-10-06T09%3A40%3A06Z\u0026sig=EEFgi8vXox70%2FO60P4AjcRWOc8j30UTgwOuBQBqwieg%3D\u0026sp=r\u0026spr=https\u0026sr=b\u0026sv=2015-04-05"],
    "remarks_info": "合本ﾌｧｲﾙ：　EP-2・4"
    }, {
    "book_id": 15799,
    "model_name": "EP10D",
    "remarks": "乗用10条田植え機,ディーゼルエンジン仕様",
    "is_favorite": false,
    "category_id": 584,
    "category_division_code": 1,
    "categories": [{
    "category_id": 584,
    "category_name": "EPｼﾘｰｽﾞ",
    "category_division_code": 1,
    "parent_category_id": 650,
    "sort_orders": [0, 0, 0]
    }, {
    "category_id": 650,
    "category_name": "田植機本機",
    "category_division_code": 1,
    "parent_category_id": 0,
    "sort_orders": [0, 0, 0]
    }],
    "distributor_code": "KBT"
    }, {
    "book_id": 10640,
    "model_name": "EP3",
    "remarks": "乗用3条田植え機",
    "is_favorite": false,
    "category_id": 584,
    "category_division_code": 1,
    "categories": [{
    "category_id": 584,
    "category_name": "EPｼﾘｰｽﾞ",
    "category_division_code": 1,
    "parent_category_id": 650,
    "sort_orders": [0, 0, 0]
    }, {
    "category_id": 650,
    "category_name": "田植機本機",
    "category_division_code": 1,
    "parent_category_id": 0,
    "sort_orders": [0, 0, 0]
    }],
    "distributor_code": "KBT"
    }, {
    "book_id": 13652,
    "model_name": "EP4-D-CS/EP4-DF-CS",
    "remarks": "乗用4条田植え機:予備苗台,除草剤散布機付き(F:粒状側条施肥装置付き)",
    "is_favorite": false,
    "category_id": 584,
    "category_division_code": 1,
    "categories": [{
    "category_id": 584,
    "category_name": "EPｼﾘｰｽﾞ",
    "category_division_code": 1,
    "parent_category_id": 650,
    "sort_orders": [0, 0, 0]
    }, {
    "category_id": 650,
    "category_name": "田植機本機",
    "category_division_code": 1,
    "parent_category_id": 0,
    "sort_orders": [0, 0, 0]
    }],
    "distributor_code": "KBT",
    "options": [{
    "book_id": 11805,
    "model_name": "CS-10M",
    "remarks": "田植え同時除草剤散布機(こまきちゃんmini)",
    "is_favorite": false,
    "amount_of_service_bulletins": 0,
    "amount_of_memos": 0,
    "ParentBookID": 13652
    }]
    }, {
    "book_id": 15411,
    "model_name": "EP4-GP",
    "remarks": "",
    "is_favorite": false,
    "category_id": 584,
    "category_division_code": 1,
    "categories": [{
    "category_id": 584,
    "category_name": "EPｼﾘｰｽﾞ",
    "category_division_code": 1,
    "parent_category_id": 650,
    "sort_orders": [0, 0, 0]
    }, {
    "category_id": 650,
    "category_name": "田植機本機",
    "category_division_code": 1,
    "parent_category_id": 0,
    "sort_orders": [0, 0, 0]
    }],
    "distributor_code": "KBT"
    }, {
    "book_id": 13651,
    "model_name": "EP4-S/EP4-SF",
    "remarks": "乗用4条田植え機:標準仕様(F:粒状側条施肥装置付き)",
    "is_favorite": false,
    "category_id": 584,
    "category_division_code": 1,
    "categories": [{
    "category_id": 584,
    "category_name": "EPｼﾘｰｽﾞ",
    "category_division_code": 1,
    "parent_category_id": 650,
    "sort_orders": [0, 0, 0]
    }, {
    "category_id": 650,
    "category_name": "田植機本機",
    "category_division_code": 1,
    "parent_category_id": 0,
    "sort_orders": [0, 0, 0]
    }],
    "distributor_code": "KBT",
    "options": [{
    "book_id": 11805,
    "model_name": "CS-10M",
    "remarks": "田植え同時除草剤散布機(こまきちゃんmini)",
    "is_favorite": false,
    "amount_of_service_bulletins": 0,
    "amount_of_memos": 0,
    "ParentBookID": 13651
    }]
    }, {
    "book_id": 10641,
    "model_name": "EP4-TC",
    "remarks": "乗用4条田植え機:鉄コーティング直播仕様",
    "is_favorite": false,
    "category_id": 584,
    "category_division_code": 1,
    "categories": [{
    "category_id": 584,
    "category_name": "EPｼﾘｰｽﾞ",
    "category_division_code": 1,
    "parent_category_id": 650,
    "sort_orders": [0, 0, 0]
    }, {
    "category_id": 650,
    "category_name": "田植機本機",
    "category_division_code": 1,
    "parent_category_id": 0,
    "sort_orders": [0, 0, 0]
    }],
    "distributor_code": "KBT"
    }, {
    "book_id": 15412,
    "model_name": "EP4-TC-GP",
    "remarks": "",
    "is_favorite": false,
    "category_id": 584,
    "category_division_code": 1,
    "categories": [{
    "category_id": 584,
    "category_name": "EPｼﾘｰｽﾞ",
    "category_division_code": 1,
    "parent_category_id": 650,
    "sort_orders": [0, 0, 0]
    }, {
    "category_id": 650,
    "category_name": "田植機本機",
    "category_division_code": 1,
    "parent_category_id": 0,
    "sort_orders": [0, 0, 0]
    }],
    "distributor_code": "KBT"
    }, {
    "book_id": 13687,
    "model_name": "EP50",
    "remarks": "乗用5条田植え機",
    "is_favorite": false,
    "category_id": 584,
    "category_division_code": 1,
    "categories": [{
    "category_id": 584,
    "category_name": "EPｼﾘｰｽﾞ",
    "category_division_code": 1,
    "parent_category_id": 650,
    "sort_orders": [0, 0, 0]
    }, {
    "category_id": 650,
    "category_name": "田植機本機",
    "category_division_code": 1,
    "parent_category_id": 0,
    "sort_orders": [0, 0, 0]
    }],
    "distributor_code": "KBT",
    "options": [{
    "book_id": 11803,
    "model_name": "CS-20",
    "remarks": "田植え同時除草剤散布機(こまきちゃん)",
    "is_favorite": false,
    "amount_of_service_bulletins": 0,
    "amount_of_memos": 0,
    "ParentBookID": 13687
    }, {
    "book_id": 11804,
    "model_name": "CS-30",
    "remarks": "田植え同時除草剤散布機(こまきちゃん)",
    "is_favorite": false,
    "amount_of_service_bulletins": 0,
    "amount_of_memos": 0,
    "ParentBookID": 13687
    }, {
    "book_id": 11796,
    "model_name": "HSY50(NSU50,NSU55)",
    "remarks": "箱施用剤散布機(箱まきちゃん),NSU50/NSU55対応",
    "is_favorite": false,
    "amount_of_service_bulletins": 0,
    "amount_of_memos": 0,
    "ParentBookID": 13687
    }, {
    "book_id": 11799,
    "model_name": "HSY55(EP50,EP55)",
    "remarks": "箱施用剤散布機(箱まきちゃん),EP50/EP55対応",
    "is_favorite": false,
    "amount_of_service_bulletins": 0,
    "amount_of_memos": 0,
    "ParentBookID": 13687
    }]
    }, {
    "book_id": 13688,
    "model_name": "EP55",
    "remarks": "乗用5条田植え機",
    "is_favorite": false,
    "category_id": 584,
    "category_division_code": 1,
    "categories": [{
    "category_id": 584,
    "category_name": "EPｼﾘｰｽﾞ",
    "category_division_code": 1,
    "parent_category_id": 650,
    "sort_orders": [0, 0, 0]
    }, {
    "category_id": 650,
    "category_name": "田植機本機",
    "category_division_code": 1,
    "parent_category_id": 0,
    "sort_orders": [0, 0, 0]
    }],
    "distributor_code": "KBT",
    "options": [{
    "book_id": 11803,
    "model_name": "CS-20",
    "remarks": "田植え同時除草剤散布機(こまきちゃん)",
    "is_favorite": false,
    "amount_of_service_bulletins": 0,
    "amount_of_memos": 0,
    "ParentBookID": 13688
    }, {
    "book_id": 11804,
    "model_name": "CS-30",
    "remarks": "田植え同時除草剤散布機(こまきちゃん)",
    "is_favorite": false,
    "amount_of_service_bulletins": 0,
    "amount_of_memos": 0,
    "ParentBookID": 13688
    }, {
    "book_id": 11796,
    "model_name": "HSY50(NSU50,NSU55)",
    "remarks": "箱施用剤散布機(箱まきちゃん),NSU50/NSU55対応",
    "is_favorite": false,
    "amount_of_service_bulletins": 0,
    "amount_of_memos": 0,
    "ParentBookID": 13688
    }, {
    "book_id": 11799,
    "model_name": "HSY55(EP50,EP55)",
    "remarks": "箱施用剤散布機(箱まきちゃん),EP50/EP55対応",
    "is_favorite": false,
    "amount_of_service_bulletins": 0,
    "amount_of_memos": 0,
    "ParentBookID": 13688
    }]
    }, {
    "book_id": 15838,
    "model_name": "EP60",
    "remarks": "乗用6条田植え機",
    "is_favorite": false,
    "category_id": 584,
    "category_division_code": 1,
    "categories": [{
    "category_id": 584,
    "category_name": "EPｼﾘｰｽﾞ",
    "category_division_code": 1,
    "parent_category_id": 650,
    "sort_orders": [0, 0, 0]
    }, {
    "category_id": 650,
    "category_name": "田植機本機",
    "category_division_code": 1,
    "parent_category_id": 0,
    "sort_orders": [0, 0, 0]
    }],
    "distributor_code": "KBT",
    "options": [{
    "book_id": 11803,
    "model_name": "CS-20",
    "remarks": "田植え同時除草剤散布機(こまきちゃん)",
    "is_favorite": false,
    "amount_of_service_bulletins": 0,
    "amount_of_memos": 0,
    "ParentBookID": 15838
    }, {
    "book_id": 11804,
    "model_name": "CS-30",
    "remarks": "田植え同時除草剤散布機(こまきちゃん)",
    "is_favorite": false,
    "amount_of_service_bulletins": 0,
    "amount_of_memos": 0,
    "ParentBookID": 15838
    }, {
    "book_id": 11797,
    "model_name": "HSY60(NSU60,NSU65,NSU67)",
    "remarks": "箱施用剤散布機(箱まきちゃん),NSU60/NSU65/NSU67対応",
    "is_favorite": false,
    "amount_of_service_bulletins": 0,
    "amount_of_memos": 0,
    "ParentBookID": 15838
    }, {
    "book_id": 11800,
    "model_name": "HSY65(EP60,EP65,EP67)",
    "remarks": "箱施用剤散布機(箱まきちゃん),EP60/EP65/EP67対応",
    "is_favorite": false,
    "amount_of_service_bulletins": 0,
    "amount_of_memos": 0,
    "ParentBookID": 15838
    }]
    }, {
    "book_id": 15839,
    "model_name": "EP65",
    "remarks": "乗用6条田植え機",
    "is_favorite": false,
    "category_id": 584,
    "category_division_code": 1,
    "categories": [{
    "category_id": 584,
    "category_name": "EPｼﾘｰｽﾞ",
    "category_division_code": 1,
    "parent_category_id": 650,
    "sort_orders": [0, 0, 0]
    }, {
    "category_id": 650,
    "category_name": "田植機本機",
    "category_division_code": 1,
    "parent_category_id": 0,
    "sort_orders": [0, 0, 0]
    }],
    "distributor_code": "KBT",
    "options": [{
    "book_id": 11803,
    "model_name": "CS-20",
    "remarks": "田植え同時除草剤散布機(こまきちゃん)",
    "is_favorite": false,
    "amount_of_service_bulletins": 0,
    "amount_of_memos": 0,
    "ParentBookID": 15839
    }, {
    "book_id": 11804,
    "model_name": "CS-30",
    "remarks": "田植え同時除草剤散布機(こまきちゃん)",
    "is_favorite": false,
    "amount_of_service_bulletins": 0,
    "amount_of_memos": 0,
    "ParentBookID": 15839
    }, {
    "book_id": 11797,
    "model_name": "HSY60(NSU60,NSU65,NSU67)",
    "remarks": "箱施用剤散布機(箱まきちゃん),NSU60/NSU65/NSU67対応",
    "is_favorite": false,
    "amount_of_service_bulletins": 0,
    "amount_of_memos": 0,
    "ParentBookID": 15839
    }, {
    "book_id": 11800,
    "model_name": "HSY65(EP60,EP65,EP67)",
    "remarks": "箱施用剤散布機(箱まきちゃん),EP60/EP65/EP67対応",
    "is_favorite": false,
    "amount_of_service_bulletins": 0,
    "amount_of_memos": 0,
    "ParentBookID": 15839
    }]
    }, {
    "book_id": 10680,
    "model_name": "EP67",
    "remarks": "C仕様（条間33ｃｍ）のみ",
    "is_favorite": false,
    "category_id": 584,
    "category_division_code": 1,
    "categories": [{
    "category_id": 584,
    "category_name": "EPｼﾘｰｽﾞ",
    "category_division_code": 1,
    "parent_category_id": 650,
    "sort_orders": [0, 0, 0]
    }, {
    "category_id": 650,
    "category_name": "田植機本機",
    "category_division_code": 1,
    "parent_category_id": 0,
    "sort_orders": [0, 0, 0]
    }],
    "distributor_code": "KBT",
    "options": [{
    "book_id": 11803,
    "model_name": "CS-20",
    "remarks": "田植え同時除草剤散布機(こまきちゃん)",
    "is_favorite": false,
    "amount_of_service_bulletins": 0,
    "amount_of_memos": 0,
    "ParentBookID": 10680
    }, {
    "book_id": 11804,
    "model_name": "CS-30",
    "remarks": "田植え同時除草剤散布機(こまきちゃん)",
    "is_favorite": false,
    "amount_of_service_bulletins": 0,
    "amount_of_memos": 0,
    "ParentBookID": 10680
    }]
    }, {
    "book_id": 10683,
    "model_name": "EP67(F・Q・Q2)",
    "remarks": "乗用6条田植え機(F:粒状側条施肥機,Q:ペースト側条施肥機,Q2:ペースト2段施肥機)",
    "is_favorite": false,
    "category_id": 584,
    "category_division_code": 1,
    "categories": [{
    "category_id": 584,
    "category_name": "EPｼﾘｰｽﾞ",
    "category_division_code": 1,
    "parent_category_id": 650,
    "sort_orders": [0, 0, 0]
    }, {
    "category_id": 650,
    "category_name": "田植機本機",
    "category_division_code": 1,
    "parent_category_id": 0,
    "sort_orders": [0, 0, 0]
    }],
    "distributor_code": "KBT",
    "options": [{
    "book_id": 11803,
    "model_name": "CS-20",
    "remarks": "田植え同時除草剤散布機(こまきちゃん)",
    "is_favorite": false,
    "amount_of_service_bulletins": 0,
    "amount_of_memos": 0,
    "ParentBookID": 10683
    }, {
    "book_id": 11804,
    "model_name": "CS-30",
    "remarks": "田植え同時除草剤散布機(こまきちゃん)",
    "is_favorite": false,
    "amount_of_service_bulletins": 0,
    "amount_of_memos": 0,
    "ParentBookID": 10683
    }, {
    "book_id": 15813,
    "model_name": "DS-60NK(F)/NDS-6(F)/NDS-60(F)",
    "remarks": "6条直播アタッチメント",
    "is_favorite": false,
    "amount_of_service_bulletins": 0,
    "amount_of_memos": 0,
    "ParentBookID": 10683
    }, {
    "book_id": 11797,
    "model_name": "HSY60(NSU60,NSU65,NSU67)",
    "remarks": "箱施用剤散布機(箱まきちゃん),NSU60/NSU65/NSU67対応",
    "is_favorite": false,
    "amount_of_service_bulletins": 0,
    "amount_of_memos": 0,
    "ParentBookID": 10683
    }, {
    "book_id": 11800,
    "model_name": "HSY65(EP60,EP65,EP67)",
    "remarks": "箱施用剤散布機(箱まきちゃん),EP60/EP65/EP67対応",
    "is_favorite": false,
    "amount_of_service_bulletins": 0,
    "amount_of_memos": 0,
    "ParentBookID": 10683
    }]
    }, {
    "book_id": 10681,
    "model_name": "EP87",
    "remarks": "C仕様（条間33ｃｍ）のみ",
    "is_favorite": false,
    "category_id": 584,
    "category_division_code": 1,
    "categories": [{
    "category_id": 584,
    "category_name": "EPｼﾘｰｽﾞ",
    "category_division_code": 1,
    "parent_category_id": 650,
    "sort_orders": [0, 0, 0]
    }, {
    "category_id": 650,
    "category_name": "田植機本機",
    "category_division_code": 1,
    "parent_category_id": 0,
    "sort_orders": [0, 0, 0]
    }],
    "distributor_code": "KBT",
    "options": [{
    "book_id": 11803,
    "model_name": "CS-20",
    "remarks": "田植え同時除草剤散布機(こまきちゃん)",
    "is_favorite": false,
    "amount_of_service_bulletins": 0,
    "amount_of_memos": 0,
    "ParentBookID": 10681
    }, {
    "book_id": 11804,
    "model_name": "CS-30",
    "remarks": "田植え同時除草剤散布機(こまきちゃん)",
    "is_favorite": false,
    "amount_of_service_bulletins": 0,
    "amount_of_memos": 0,
    "ParentBookID": 10681
    }, {
    "book_id": 13686,
    "model_name": "HSY80C",
    "remarks": "箱施用剤散布機(箱まきちゃん),NSD8-C対応",
    "is_favorite": false,
    "amount_of_service_bulletins": 0,
    "amount_of_memos": 0,
    "ParentBookID": 10681
    }]
    }, {
    "book_id": 10682,
    "model_name": "EP87(F・Q・Q2)",
    "remarks": "乗用8条田植え機(F:粒状側条施肥機,Q:ペースト側条施肥機,Q2:ペースト2段施肥機)",
    "is_favorite": false,
    "category_id": 584,
    "category_division_code": 1,
    "categories": [{
    "category_id": 584,
    "category_name": "EPｼﾘｰｽﾞ",
    "category_division_code": 1,
    "parent_category_id": 650,
    "sort_orders": [0, 0, 0]
    }, {
    "category_id": 650,
    "category_name": "田植機本機",
    "category_division_code": 1,
    "parent_category_id": 0,
    "sort_orders": [0, 0, 0]
    }],
    "distributor_code": "KBT",
    "options": [{
    "book_id": 11803,
    "model_name": "CS-20",
    "remarks": "田植え同時除草剤散布機(こまきちゃん)",
    "is_favorite": false,
    "amount_of_service_bulletins": 0,
    "amount_of_memos": 0,
    "ParentBookID": 10682
    }, {
    "book_id": 11804,
    "model_name": "CS-30",
    "remarks": "田植え同時除草剤散布機(こまきちゃん)",
    "is_favorite": false,
    "amount_of_service_bulletins": 0,
    "amount_of_memos": 0,
    "ParentBookID": 10682
    }, {
    "book_id": 15814,
    "model_name": "DS-80NK(F)/NDS-8(F)/NDS-80(F)",
    "remarks": "8条直播アタッチメント",
    "is_favorite": false,
    "amount_of_service_bulletins": 0,
    "amount_of_memos": 0,
    "ParentBookID": 10682
    }, {
    "book_id": 11798,
    "model_name": "HSY80(NSU87,NSD8)",
    "remarks": "箱施用剤散布機(箱まきちゃん),NSU87/NSD8対応",
    "is_favorite": false,
    "amount_of_service_bulletins": 0,
    "amount_of_memos": 0,
    "ParentBookID": 10682
    }, {
    "book_id": 11801,
    "model_name": "HSY85(EP87)",
    "remarks": "箱施用剤散布機(箱まきちゃん),EP87対応",
    "is_favorite": false,
    "amount_of_service_bulletins": 0,
    "amount_of_memos": 0,
    "ParentBookID": 10682
    }]
    }, {
    "book_id": 16120,
    "model_name": "EP8D(C)",
    "remarks": "",
    "is_favorite": false,
    "category_id": 584,
    "category_division_code": 1,
    "categories": [{
    "category_id": 584,
    "category_name": "EPｼﾘｰｽﾞ",
    "category_division_code": 1,
    "parent_category_id": 650,
    "sort_orders": [0, 0, 0]
    }, {
    "category_id": 650,
    "category_name": "田植機本機",
    "category_division_code": 1,
    "parent_category_id": 0,
    "sort_orders": [0, 0, 0]
    }],
    "distributor_code": "KBT"
    }, {
    "book_id": 16121,
    "model_name": "EP8D-F(C)",
    "remarks": "",
    "is_favorite": false,
    "category_id": 584,
    "category_division_code": 1,
    "categories": [{
    "category_id": 584,
    "category_name": "EPｼﾘｰｽﾞ",
    "category_division_code": 1,
    "parent_category_id": 650,
    "sort_orders": [0, 0, 0]
    }, {
    "category_id": 650,
    "category_name": "田植機本機",
    "category_division_code": 1,
    "parent_category_id": 0,
    "sort_orders": [0, 0, 0]
    }],
    "distributor_code": "KBT"
    }, {
    "book_id": 16122,
    "model_name": "EP8D-Q,Q2",
    "remarks": "",
    "is_favorite": false,
    "category_id": 584,
    "category_division_code": 1,
    "categories": [{
    "category_id": 584,
    "category_name": "EPｼﾘｰｽﾞ",
    "category_division_code": 1,
    "parent_category_id": 650,
    "sort_orders": [0, 0, 0]
    }, {
    "category_id": 650,
    "category_name": "田植機本機",
    "category_division_code": 1,
    "parent_category_id": 0,
    "sort_orders": [0, 0, 0]
    }],
    "distributor_code": "KBT"
    }, {
    "book_id": 41,
    "model_name": "EPZ-5",
    "remarks": "",
    "is_favorite": false,
    "category_id": 356,
    "category_division_code": 2,
    "categories": [{
    "category_id": 356,
    "category_name": "EPZ-5・10",
    "category_division_code": 2,
    "parent_category_id": 46,
    "sort_orders": [0, 0, 0]
    }, {
    "category_id": 46,
    "category_name": "播種機",
    "category_division_code": 2,
    "parent_category_id": 1,
    "sort_orders": [0, 0, 0]
    }, {
    "category_id": 1,
    "category_name": "アグリテクノ矢崎(ATY)",
    "category_division_code": 2,
    "parent_category_id": 0,
    "sort_orders": [0, 0, 0]
    }],
    "distributor_code": "",
    "pdfs": ["https://publicd6tn2upk7h3ce.blob.core.windows.net/relatedmaker/1/agritecno_EPZ-5_10.pdf?se=2020-10-06T09%3A40%3A06Z\u0026sig=z%2Ba4pfS4FwqHSWYlcO2J6mwDF%2B5WWacg18REFG2huZg%3D\u0026sp=r\u0026spr=https\u0026sr=b\u0026sv=2015-04-05"],
    "remarks_info": "合本ﾌｧｲﾙ：　EPZ-5・10"
    }, {
    "book_id": 42,
    "model_name": "EPZ10",
    "remarks": "",
    "is_favorite": false,
    "category_id": 356,
    "category_division_code": 2,
    "categories": [{
    "category_id": 356,
    "category_name": "EPZ-5・10",
    "category_division_code": 2,
    "parent_category_id": 46,
    "sort_orders": [0, 0, 0]
    }, {
    "category_id": 46,
    "category_name": "播種機",
    "category_division_code": 2,
    "parent_category_id": 1,
    "sort_orders": [0, 0, 0]
    }, {
    "category_id": 1,
    "category_name": "アグリテクノ矢崎(ATY)",
    "category_division_code": 2,
    "parent_category_id": 0,
    "sort_orders": [0, 0, 0]
    }],
    "distributor_code": "",
    "pdfs": ["https://publicd6tn2upk7h3ce.blob.core.windows.net/relatedmaker/1/agritecno_EPZ-5_10.pdf?se=2020-10-06T09%3A40%3A06Z\u0026sig=z%2Ba4pfS4FwqHSWYlcO2J6mwDF%2B5WWacg18REFG2huZg%3D\u0026sp=r\u0026spr=https\u0026sr=b\u0026sv=2015-04-05"],
    "remarks_info": "合本ﾌｧｲﾙ：　EPZ-5・10"
    }]
    }
     *
     */

}

