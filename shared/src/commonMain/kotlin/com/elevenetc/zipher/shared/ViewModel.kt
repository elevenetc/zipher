package com.elevenetc.zipher.shared

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.coroutines.CoroutineContext

open class ViewModel : CoroutineScope {

    var currentState: ViewState = InitState
    val state = MutableStateFlow(StateTransition(currentState, currentState))
    val job = Job()

    open fun onUserAction(action: UserAction) {

    }

    protected fun updateState(newState: ViewState) {
        val oldState = currentState
        currentState = newState
        state.tryEmit(StateTransition(currentState, oldState))
    }

    fun onDestroy() {
        job.cancel()
    }

    open class UserAction
    open class ViewState
    object InitState : ViewState()

    data class StateTransition(val currentState: ViewState, val prevState: ViewState)

    object Loading : ViewState()
    data class Error(val t: Throwable) : ViewState()
    data class Result<T>(val result: T) : ViewState()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job
}