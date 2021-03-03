package com.elevenetc.zipher.shared

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.coroutines.CoroutineContext

open class ViewModel : CoroutineScope {

    val state = MutableStateFlow<ViewState>(Loading)

    open fun onUserAction(action: UserAction) {

    }

    open class UserAction
    open class ViewState

    object Loading : ViewState()
    data class Error(val t: Throwable) : ViewState()
    data class Result<T>(val result: T) : ViewState()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main
}