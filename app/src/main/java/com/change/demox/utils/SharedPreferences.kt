package com.change.demox.utils

import android.content.Context
import android.content.SharedPreferences
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Created by xingjunchao on 2020/08/24.
 *
 * @param context Context
 */
open class SharedPreferences(context: Context) {

    /**
     * データ保存操作の対象
     */
    private val preferences: SharedPreferences =
            context.getSharedPreferences("file", Context.MODE_PRIVATE)

    /**
     * 是否是橙色系统色 true:是，false:否
     */
    var isOrangeThemeColor by SharedPreferenceDelegates.boolean(false)


    fun clear() {
        isOrangeThemeColor = false
    }

    private object SharedPreferenceDelegates {

        fun int(defaultValue: Int = 0) =
                object : ReadWriteProperty<com.change.demox.utils.SharedPreferences, Int> {

                    override fun getValue(
                            thisRef: com.change.demox.utils.SharedPreferences,
                            property: KProperty<*>
                    ): Int {
                        return thisRef.preferences.getInt(property.name, defaultValue)
                    }

                    override fun setValue(
                            thisRef: com.change.demox.utils.SharedPreferences,
                            property: KProperty<*>,
                            value: Int
                    ) {
                        thisRef.preferences.edit().putInt(property.name, value).apply()
                    }
                }

        fun boolean(defaultValue: Boolean = false) =
                object : ReadWriteProperty<com.change.demox.utils.SharedPreferences, Boolean> {
                    override fun getValue(
                            thisRef: com.change.demox.utils.SharedPreferences,
                            property: KProperty<*>
                    ): Boolean {
                        return thisRef.preferences.getBoolean(property.name, defaultValue)
                    }

                    override fun setValue(
                            thisRef: com.change.demox.utils.SharedPreferences,
                            property: KProperty<*>,
                            value: Boolean
                    ) {
                        thisRef.preferences.edit().putBoolean(property.name, value).apply()
                    }
                }

        fun string(defaultValue: String? = null) =
                object : ReadWriteProperty<com.change.demox.utils.SharedPreferences, String?> {
                    override fun getValue(
                            thisRef: com.change.demox.utils.SharedPreferences,
                            property: KProperty<*>
                    ): String? {
                        return thisRef.preferences.getString(property.name, defaultValue)
                    }

                    override fun setValue(
                            thisRef: com.change.demox.utils.SharedPreferences,
                            property: KProperty<*>,
                            value: String?
                    ) {
                        thisRef.preferences.edit().putString(property.name, value).apply()
                    }
                }
    }

}