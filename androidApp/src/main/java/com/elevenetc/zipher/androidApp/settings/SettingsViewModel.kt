package com.elevenetc.zipher.androidApp.settings

import com.elevenetc.zipher.shared.LockRepository
import com.elevenetc.zipher.shared.ViewModel

class SettingsViewModel(val lockRepository: LockRepository) : ViewModel() {

    override fun onUserAction(action: UserAction) {
        if (action is Lock) {
            lockRepository.lock()
            updateState(Locked)
        } else if (action is Clear) {
            lockRepository.deleteDb()
            updateState(DbDeleted)
        }
    }

    object Locked : ViewState()
    object DbDeleted : ViewState()

    object Lock : UserAction()
    object Clear : UserAction()
}