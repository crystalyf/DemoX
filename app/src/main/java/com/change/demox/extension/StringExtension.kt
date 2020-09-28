package com.change.demox.extension

import java.lang.StringBuilder

fun String.insertSpaceInPhoneNumber(): String {
    //123-1234,578-5678
    if (this.isEmpty()) return this
    val result = StringBuilder(this)
    if (this.length > 2)
        result.insert(2, "-")
    if (this.length > 5)
        result.insert(6, "-")
    return result.toString()
}

fun String.getPhoneNumber(): String {
    return this.replace("-", "")
}

fun String.isPhoneNumberFormat(): Boolean {
    if (this.isEmpty()) return true
    if (" ".equals(this[this.length - 1].toString())) return false
    for (i in 1..this.length) {
        if (i % 5 != 0) continue
        if (!" ".equals(this[i - 1].toString())) return false
    }
    return true
}