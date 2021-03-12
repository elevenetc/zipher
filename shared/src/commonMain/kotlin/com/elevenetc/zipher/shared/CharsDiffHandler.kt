package com.elevenetc.zipher.shared

class CharsDiffHandler {

    private var currentValue = ""
    var handler: (Char) -> Unit = {}

    fun update(newValue: String) {
        if (currentValue.isEmpty()) {
            newValue.toCharArray().forEach { handler(it) }
            currentValue = newValue
        } else {
            var currentIdx = 0
            var newIdx = 0

            while (currentValue.containsIdx(currentIdx) || newValue.containsIdx(newIdx)) {
                val containsCurrentIdx = currentValue.containsIdx(currentIdx)
                val containsNewIdx = newValue.containsIdx(newIdx)
                if (containsCurrentIdx && containsNewIdx) {
                    val chCurrent = currentValue[currentIdx]
                    val chNew = newValue[newIdx]

                    if (chCurrent == chNew) {
                        currentIdx++
                        newIdx++
                    } else {
                        newValue.forEachFrom(newIdx) {
                            handler(it)
                        }
                        currentValue = newValue
                        break
                    }
                } else if (!containsCurrentIdx && containsNewIdx) {
                    //added more values
                    newValue.forEachFrom(newIdx) {
                        handler(it)
                    }
                    currentValue = newValue
                    break
                } else if (containsCurrentIdx && !containsNewIdx) {
                    //removed values
                    //current: abc
                    //new: ab
                }
            }
        }
    }
}