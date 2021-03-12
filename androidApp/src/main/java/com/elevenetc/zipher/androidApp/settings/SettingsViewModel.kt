package com.elevenetc.zipher.androidApp.settings

import com.elevenetc.zipher.shared.LockRepository
import com.elevenetc.zipher.shared.ViewModel

class SettingsViewModel(val lockRepository: LockRepository) : ViewModel() {

    override fun onUserAction(action: UserAction) {
        if (action is Lock) {
            lockRepository.lock()
            updateState(Locked)
        }
    }

    object Locked : ViewState()

    object Lock : UserAction()
}