package com.elevenetc.zipher.shared

import com.elevenetc.zipher.shared.LockRepository.State.Locked
import com.elevenetc.zipher.shared.LockRepository.State.Unlocked

class LockRepository(val dao: Dao, val keyValue: KeyValueStorage) {

    var state: State = Locked

    fun isLocked(): Boolean {
        return dao.isLocked()
    }

    fun createAndUnlock(pass: String) {
        if (state == Locked) {
            try {
                dao.unlock(pass)
                state = Unlocked
                keyValue.store("hasKey", true)
            } catch (e: InvalidDbPassword) {

            }
        }
    }

    fun lock() {
        if (!isLocked()) {
            state = Locked
            dao.lock()
        }
    }

    fun lock(pass: List<Char>) {
        if (!isLocked()) {
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

    fun hasPassword(): Boolean {
        return keyValue.get("hasKey", false)
    }

    sealed class State {
        object Locked : State()
        object Unlocked : State()
    }
}