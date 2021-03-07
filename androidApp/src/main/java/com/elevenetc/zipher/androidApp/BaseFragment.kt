package com.elevenetc.zipher.androidApp

import androidx.fragment.app.Fragment
import com.elevenetc.zipher.shared.ViewModel.StateTransition
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

open class BaseFragment(val layout: Int) : Fragment(layout), CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main


    open fun handleState(transition: StateTransition) {

    }
}