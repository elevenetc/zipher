package com.elevenetc.zipher.androidApp

import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener
import com.elevenetc.zipher.shared.LockViewModel.*
import com.elevenetc.zipher.shared.ViewModel
import com.elevenetc.zipher.shared.ViewModel.StateTransition


class LockStateView(context: Context, attrs: AttributeSet?) : ConstraintLayout(context, attrs) {

    val textState: TextView
    val textLock: EditText

    lateinit var userActionListener: (ViewModel.UserAction) -> Unit
    val entry = mutableListOf<Char>()

    init {
        LayoutInflater.from(context).inflate(R.layout.view_lock_state, this)
        textState = findViewById(R.id.text_state)
        textLock = findViewById(R.id.text_lock)
        //isFocusable = true
        //isFocusableInTouchMode = true

//        setOnClickListener {
//            //focusAndShowKeyboard()
//        }

        textLock.addTextChangedListener {
            userActionListener(PassEntry(textLock.text.toString()))
        }

        textLock.setOnEditorActionListener { v, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_NEXT
                || keyEvent.action == KeyEvent.ACTION_DOWN
                || keyEvent.action == KeyEvent.KEYCODE_ENTER
            ) {
                userActionListener(Next)
                true

            }
            false
        }

//        textLock.setOnKeyListener { v, keyCode, event ->
//            if (keyCode == KeyEvent.KEYCODE_DEL) {
//                userActionListener(Delete)
//                true
//            } else if (keyCode == KeyEvent.KEYCODE_ENTER) {
//                userActionListener(Next)
//                true
//            } else {
//                false
//            }
//        }

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
                //userActionListener(PassEntry(event.unicodeChar.toChar()))
                //userActionListener(PassEntry(textLock.text.toString()))
                return true
            } else {

                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    //userActionListener(Delete)
                } else if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    //userActionListener(Next)
                }

                return false
            }
        } else {
            return super.onKeyDown(keyCode, event)
        }
    }

    fun handleState(transition: StateTransition) {

        val state = transition.currentState
        val stateName = state.javaClass.simpleName


        if (state is Unlocked) {
            //navigator.
        } else if (state is CreatingLock) {
            textState.text = stateName + ": " + state.lock
            //editPassword.setText(state.lock.toString())
        } else if (state is CreatingLockVerify) {
            textState.text = stateName + " lock: " + state.lock + " verify:" + state.verify

            if (transition.prevState is CreatingLock) {
                textLock.text.clear()
            }
        } else if (state is InvalidPasswordVerification) {
            textState.text = "Verification is invalid. Enter new pass."
        } else if (state is UnlockingLock) {
            textState.text = stateName + ": " + state.lock
        } else {
            textState.text = stateName
        }
    }
}