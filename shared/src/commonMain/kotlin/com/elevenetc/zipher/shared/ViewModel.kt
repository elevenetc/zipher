package com.elevenetc.zipher.shared

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.coroutines.CoroutineContext

open class ViewModel : CoroutineScope {

    val state = MutableStateFlow<StateTransition>(StateTransition(InitState, InitState))

    open fun onUserAction(action: UserAction) {

    }

    open class UserAction
    open class ViewState
    object InitState : ViewState()

    data class StateTransition(val currentState: ViewState, val prevState: ViewState)

    object Loading : ViewState()
    data class Error(val t: Throwable) : ViewState()
    data class Result<T>(val result: T) : ViewState()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main
}