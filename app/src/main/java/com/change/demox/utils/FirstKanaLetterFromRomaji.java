package com.change.demox.utils;

import java.util.HashMap;

/**
 * Created by xjc on 2019/10/18.
 */

public class FirstKanaLetterFromRomaji {

    private static HashMap<String, String> mKanaMap;

    public static String getFirstKanaLetter(String kana) {
        if (mKanaMap == null) {
            setDataToKanaMap();
        }
        String res = mKanaMap.get(kana);
        return res == null ? "" : res;
    }

    private static void setDataToKanaMap() {
        mKanaMap = new HashMap<>();
        mKanaMap.put("あ", "あ");
        mKanaMap.put("え", "あ");
        mKanaMap.put("い", "あ");
        mKanaMap.put("お", "あ");
        mKanaMap.put("う", "あ");

        mKanaMap.put("か", "か");
        mKanaMap.put("き", "か");
        mKanaMap.put("く", "か");
        mKanaMap.put("け", "か");
        mKanaMap.put("こ", "か");

        mKanaMap.put("さ", "さ");
        mKanaMap.put("し", "さ");
        mKanaMap.put("す", "さ");
        mKanaMap.put("せ", "さ");
        mKanaMap.put("そ", "さ");

        mKanaMap.put("た", "た");
        mKanaMap.put("ち", "た");
        mKanaMap.put("つ", "た");
        mKanaMap.put("て", "た");
        mKanaMap.put("と", "た");

        mKanaMap.put("な", "な");
        mKanaMap.put("に", "な");
        mKanaMap.put("ぬ", "な");
        mKanaMap.put("ね", "な");
        mKanaMap.put("の", "な");

        mKanaMap.put("は", "は");
        mKanaMap.put("ひ", "は");
        mKanaMap.put("ふ", "は");
        mKanaMap.put("へ", "は");
        mKanaMap.put("ほ", "は");

        mKanaMap.put("ま", "ま");
        mKanaMap.put("み", "ま");
        mKanaMap.put("む", "ま");
        mKanaMap.put("め", "ま");
        mKanaMap.put("も", "ま");

        mKanaMap.put("や", "や");
        mKanaMap.put("ゆ", "や");
        mKanaMap.put("よ", "や");

        mKanaMap.put("ら", "ら");
        mKanaMap.put("り", "ら");
        mKanaMap.put("る", "ら");
        mKanaMap.put("れ", "ら");
        mKanaMap.put("ろ", "ら");

        mKanaMap.put("わ", "わ");
        mKanaMap.put("を", "わ");

        mKanaMap.put("が", "か");
        mKanaMap.put("ぎ", "か");
        mKanaMap.put("ぐ", "か");
        mKanaMap.put("げ", "か");
        mKanaMap.put("ご", "か");

        mKanaMap.put("ざ", "さ");
        mKanaMap.put("じ", "さ");
        mKanaMap.put("ず", "さ");
        mKanaMap.put("ぜ", "さ");
        mKanaMap.put("ぞ", "さ");

        mKanaMap.put("だ", "た");
        mKanaMap.put("ぢ", "た");
        mKanaMap.put("づ", "た");
        mKanaMap.put("で", "た");
        mKanaMap.put("ど", "た");

        mKanaMap.put("ば", "は");
        mKanaMap.put("び", "は");
        mKanaMap.put("ぶ", "は");
        mKanaMap.put("べ", "は");
        mKanaMap.put("ぼ", "は");

        mKanaMap.put("ぱ", "は");
        mKanaMap.put("ぴ", "は");
        mKanaMap.put("ぷ", "は");
        mKanaMap.put("ぺ", "は");
        mKanaMap.put("ぽ", "は");
    }

}
