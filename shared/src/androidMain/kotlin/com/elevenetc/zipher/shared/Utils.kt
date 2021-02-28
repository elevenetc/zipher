package com.elevenetc.zipher.shared

import java.util.*

actual fun randomUUID(): String {
    return UUID.randomUUID().toString()
}
