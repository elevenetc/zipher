package com.elevenetc.zipher.androidApp.settings

import android.os.Bundle
import android.view.View
import com.elevenetc.zipher.androidApp.BaseFragment
import com.elevenetc.zipher.androidApp.LockFragment
import com.elevenetc.zipher.androidApp.R
import com.elevenetc.zipher.androidApp.navigation.Navigator
import com.elevenetc.zipher.shared.ViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject

class SettingsFragment : BaseFragment<SettingsViewModel>(R.layout.fragment_settings) {

    init {
        vm = get()
    }

    private val navigator: Navigator by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        launch {
            vm.state.collect { state ->
                handleState(state)
            }
        }

        view.findViewById<View>(R.id.btn_lock).setOnClickListener {
            vm.onUserAction(SettingsViewModel.Lock)
        }

        view.findViewById<View>(R.id.btn_clean).setOnClickListener {
            vm.onUserAction(SettingsViewModel.Clear)
        }
    }

    override fun handleState(transition: ViewModel.StateTransition) {
        val state = transition.currentState
        if (state is SettingsViewModel.Locked) {
            navigator.replaceRootScreen(LockFragment())
        } else if (state is SettingsViewModel.DbDeleted) {
            navigator.replaceRootScreen(LockFragment())
        }
    }
}