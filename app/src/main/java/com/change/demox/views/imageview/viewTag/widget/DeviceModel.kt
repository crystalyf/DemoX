package com.change.demox.views.imageview.viewTag.widget

class DeviceModel {
    var part_id = 0
    var illustrations: List<IllustrationsBean>? = null

    class IllustrationsBean {
        /**
         * illustration_id : 192347
         * positions : [{"start_x":1885,"end_x":1982,"start_y":300,"end_y":349}]
         */
        var illustration_id = 0
        var positions: List<PositionsBean>? = null

        /**
         * 零件编号的可点击范围
         */
        class PositionsBean {
            /**
             * start_x : 1885
             * end_x : 1982
             * start_y : 300
             * end_y : 349
             */
            var start_x = 0
            var end_x = 0
            var start_y = 0
            var end_y = 0

        }
    }
}