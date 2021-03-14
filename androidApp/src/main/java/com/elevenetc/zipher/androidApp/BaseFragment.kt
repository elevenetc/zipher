package com.elevenetc.zipher.androidApp

import androidx.fragment.app.Fragment
import com.elevenetc.zipher.shared.ViewModel
import com.elevenetc.zipher.shared.ViewModel.StateTransition
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

open class BaseFragment<T : ViewModel>(layout: Int) : Fragment(layout), CoroutineScope {

    lateinit var vm: T

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main


    open fun handleState(transition: StateTransition) {

    }

    override fun onDestroy() {
        if (this::vm.isInitialized) vm.onDestroy()
        super.onDestroy()
    }
}