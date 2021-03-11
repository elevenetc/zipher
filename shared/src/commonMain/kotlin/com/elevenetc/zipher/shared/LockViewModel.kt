package com.elevenetc.zipher.shared

import kotlinx.coroutines.launch

class LockViewModel(private val repository: LockRepository) : ViewModel() {


    override fun onUserAction(action: UserAction) {

        val s = state.value.currentState

        if (action is GetLockState) {
            launch {

                val isLocked = repository.state is LockRepository.State.Locked
                val hasPassword = repository.hasPassword()

                if (isLocked) {
                    if (isLocked) {

                        if (hasPassword) {
                            updateState(UnlockingLock(""))
                        } else {
                            updateState(CreatingLock(""))
                        }


                    } else {
                        updateState(Unlocked)
                    }
                } else {
                    updateState(Unlocked)
                }
            }
        } else if (action is PassEntry) {

            val newKey = action.value
            if (s is CreatingLock) {
                updateState(CreatingLock(newKey))
            } else if (s is CreatingLockVerify) {
                updateState(CreatingLockVerify(s.lock, newKey))
            } else if (s is InvalidPasswordVerification) {
                updateState(CreatingLock(""))
            } else if (s is UnlockingLock) {
                updateState(UnlockingLock(newKey))
            } else if (s is InvalidUnlockPass) {
                updateState(UnlockingLock(newKey + ""))
            }
        } else if (action is Next) {
            if (s is CreatingLock) {
                updateState(CreatingLockVerify(s.lock, ""))
            } else if (s is CreatingLockVerify) {
                if (s.lock == s.verify) {
                    try {
                        repository.createAndUnlock(s.lock)
                    } catch (e: Exception) {
                        //TODO: verify is db already has key
                    }
                    updateState(LockCreated)
                } else {
                    updateState(InvalidPasswordVerification)
                }
            } else if (s is UnlockingLock) {
                repository.unlock(s.lock)
                if (repository.state is LockRepository.State.Unlocked) {
                    updateState(Unlocked)
                } else {
                    updateState(InvalidUnlockPass)
                }
            }
        } else if (action is Delete) {
            if (s is CreatingLock) {
                val lock = s.lock
                if (lock.isNotEmpty()) {
                    updateState(CreatingLock(lock.substring(0, lock.length - 1)))
                }
            } else if (s is CreatingLockVerify) {
                val lock = s.verify
                if (lock.isNotEmpty()) {
                    updateState(CreatingLockVerify(s.lock, lock.substring(0, lock.length - 1)))
                }
            } else if (s is UnlockingLock) {
                val lock = s.lock
                if (lock.isNotEmpty()) {
                    updateState(UnlockingLock(lock.substring(0, lock.length - 1)))
                }
            }
        }
    }

    object GetLockState : UserAction()
    data class PassEntry(val value: String) : UserAction()
    object Next : UserAction()
    object Delete : UserAction()

    /**
     * View states
     */
    data class UnlockingLock(val lock: String) : ViewState()
    data class CreatingLock(val lock: String) : ViewState()
    data class CreatingLockVerify(val lock: String, val verify: String) : ViewState()
    object LockCreated : ViewState()
    object InvalidPasswordVerification : ViewState()
    object Unlocked : ViewState()
    object InvalidUnlockPass : ViewState()
}