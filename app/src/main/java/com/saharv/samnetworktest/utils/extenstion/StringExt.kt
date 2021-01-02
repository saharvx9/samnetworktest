package com.saharv.hilt_tut.utils.extenstion

fun String.fixImageUrl(): String {
    var fixedImage = this
    if (this.contains("http://")) {
        fixedImage = this.replace("http://", "https://")
    }
    return fixedImage
}

fun CharSequence?.isEmpty(): Boolean = this == null || length == 0

fun CharSequence?.isNotEmpty(): Boolean = this != null && length > 0
