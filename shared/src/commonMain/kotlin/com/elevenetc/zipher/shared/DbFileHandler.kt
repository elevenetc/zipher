package com.elevenetc.zipher.shared

expect class DbFileHandler {
    fun delete()
    fun getName(): String
    fun exists(): Boolean
}