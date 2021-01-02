package com.saharv.hilt_tut.utils.extenstion

fun Boolean.getInt(): Int {
    return if (this) 1 else 0
}