package com.elevenetc.zipher.shared

fun String.containsIdx(idx: Int): Boolean {
    if (isEmpty()) return false
    return idx in 0 until length
}

fun String.forEachFrom(idx: Int, handler: (Char) -> Unit) {
    if (idx < 0 || !containsIdx(idx)) return
    for (i in idx until length) handler(get(i))
}