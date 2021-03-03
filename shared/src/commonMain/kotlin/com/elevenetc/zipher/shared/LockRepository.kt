package com.elevenetc.zipher.shared

import com.elevenetc.zipher.shared.LockRepository.State.Locked
import com.elevenetc.zipher.shared.LockRepository.State.Unlocked

class LockRepository(val dao: Dao) {

    var state: State = Locked

    fun hasPassword(): Boolean {
        return dao.isLocked()
    }

    fun createAndUnlock(pass: String) {
        if (state == Locked) {
            try {
                dao.unlock(pass)
                state = Unlocked
            } catch (e: InvalidDbPassword) {

            }
        }
    }

    fun lock() {
        if (state is Unlocked && hasPassword()) {
            state = Locked
        }
    }

    fun lock(pass: List<Char>) {
        if (state is Unlocked && !hasPassword()) {
            dao.lock()
            state = Locked
        }
    }

    fun unlock(pass: String) {
        if (state == Locked) {
            try {
                dao.unlock(pass)
                state = Unlocked
            } catch (e: InvalidDbPassword) {

            }
        }
    }

    sealed class State {
        object Locked : State()
        object Unlocked : State()
    }
}