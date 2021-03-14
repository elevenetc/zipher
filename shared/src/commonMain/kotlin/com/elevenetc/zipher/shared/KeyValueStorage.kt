package com.elevenetc.zipher.shared

expect class KeyValueStorage {
    fun store(key: String, value: String)
    fun get(key: String, defaultValue: String = ""): String
    fun store(key: String, value: Boolean)
    fun get(key: String, defaultValue: Boolean = false): Boolean
    fun clear()
}