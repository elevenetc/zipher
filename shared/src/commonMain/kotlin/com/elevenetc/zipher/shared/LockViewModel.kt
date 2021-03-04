package com.elevenetc.zipher.shared

import kotlinx.coroutines.launch

class LockViewModel(private val repository: LockRepository) : ViewModel() {


    override fun onUserAction(action: UserAction) {
        if (action is GetLockState) {
            launch {

                val isLocked = repository.state is LockRepository.State.Locked
                val hasPassword = repository.hasPassword()

                if (isLocked) {
                    if (isLocked) {

                        if (hasPassword) {
                            state.tryEmit(UnlockingLock(""))
                        } else {
                            state.tryEmit(CreatingLock(""))
                        }


                    } else {
                        state.tryEmit(Unlocked)
                    }
                } else {
                    state.tryEmit(Unlocked)
                }
            }
        } else if (action is PassEntry) {
            val s = state.value
            if (s is CreatingLock) {
                state.tryEmit(CreatingLock(s.lock + action.value))
            } else if (s is CreatingLockVerify) {
                state.tryEmit(CreatingLockVerify(s.lock, s.verify + action.value))
            } else if (s is InvalidPasswordVerification) {
                state.tryEmit(CreatingLock(""))
            } else if (s is UnlockingLock) {
                val lock = s.lock + action.value
                state.tryEmit(UnlockingLock(lock))
            } else if (s is InvalidUnlockPass) {
                state.tryEmit(UnlockingLock(action.value + ""))
            }
        } else if (action is Next) {
            val s = state.value
            if (s is CreatingLock) {
                state.tryEmit(CreatingLockVerify(s.lock, ""))
            } else if (s is CreatingLockVerify) {
                if (s.lock == s.verify) {
                    try {
                        repository.createAndUnlock(s.lock)
                    } catch (e: Exception) {
                        //TODO: verify is db already has key
                    }
                    state.tryEmit(LockCreated)
                } else {
                    state.tryEmit(InvalidPasswordVerification)
                }
            } else if (s is UnlockingLock) {
                repository.unlock(s.lock)
                if (repository.state is LockRepository.State.Unlocked) {
                    state.tryEmit(Unlocked)
                } else {
                    state.tryEmit(InvalidUnlockPass)
                }
            }
        } else if (action is Delete) {
            val s = state.value
            if (s is CreatingLock) {
                val lock = s.lock
                if (lock.isNotEmpty()) {
                    state.tryEmit(CreatingLock(lock.substring(0, lock.length - 1)))
                }
            } else if (s is CreatingLockVerify) {
                val lock = s.verify
                if (lock.isNotEmpty()) {
                    state.tryEmit(CreatingLockVerify(s.lock, lock.substring(0, lock.length - 1)))
                }
            } else if (s is UnlockingLock) {
                val lock = s.lock
                if (lock.isNotEmpty()) {
                    state.tryEmit(UnlockingLock(lock.substring(0, lock.length - 1)))
                }
            }
        }
    }

    object GetLockState : UserAction()
    data class PassEntry(val value: Char) : UserAction()
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