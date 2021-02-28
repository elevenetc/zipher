package com.elevenetc.zipher.shared


actual class Platform actual constructor() {
    actual val platform: String
        get() {
            return randomUUID()
        }
}