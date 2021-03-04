package com.elevenetc.zipher.androidApp

import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.LayoutInflater
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.elevenetc.zipher.shared.LockViewModel.*
import com.elevenetc.zipher.shared.ViewModel


class LockStateView(context: Context, attrs: AttributeSet?) : ConstraintLayout(context, attrs) {

    val textState: TextView
    val textLock: TextView

    lateinit var userActionListener: (ViewModel.UserAction) -> Unit
    val entry = mutableListOf<Char>()

    init {
        LayoutInflater.from(context).inflate(R.layout.view_lock_state, this)
        textState = findViewById(R.id.text_state)
        textLock = findViewById(R.id.text_lock)
        isFocusable = true
        isFocusableInTouchMode = true

        setOnClickListener {
            focusAndShowKeyboard()
        }

//        addOnUnhandledKeyEventListener(object : OnUnhandledKeyEventListener {
//            override fun onUnhandledKeyEvent(v: View?, event: KeyEvent): Boolean {
//
//
//                if (event.action == KeyEvent.ACTION_UP) {
//                    val keyCode = event.keyCode
//                    if (event.isPrintingKey) {
//                        userActionListener(PassEntry(event.unicodeChar.toChar()))
//                        return true
//                    } else {
//
//                        if (keyCode == KeyEvent.KEYCODE_DEL) {
//                            userActionListener(Delete)
//                        } else if (keyCode == KeyEvent.KEYCODE_ENTER) {
//                            userActionListener(Next)
//                        }
//
//                        return false
//                    }
//                } else {
//                    return false
//                }
//            }
//        })
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean {
        if (event.action == KeyEvent.ACTION_UP) {
            if (event.isPrintingKey) {
                userActionListener(PassEntry(event.unicodeChar.toChar()))
                return true
            } else {

                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    userActionListener(Delete)
                } else if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    userActionListener(Next)
                }

                return false
            }
        } else {
            return super.onKeyDown(keyCode, event)
        }
    }

    fun handleState(state: ViewModel.ViewState) {

        val stateName = state.javaClass.simpleName


        if (state is Unlocked) {
            //navigator.
        } else if (state is CreatingLock) {
            textState.text = stateName + ": " + state.lock
            //editPassword.setText(state.lock.toString())
        } else if (state is CreatingLockVerify) {
            textState.text = stateName + " lock: " + state.lock + " verify:" + state.verify
            //editPassword.setText(state.verify.toString())
        } else if (state is InvalidPasswordVerification) {
            textState.text = "Verification is invalid. Enter new pass."
        } else if (state is UnlockingLock) {
            textState.text = stateName + ": " + state.lock
        } else {
            textState.text = stateName
        }
    }
}