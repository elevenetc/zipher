package com.elevenetc.zipher.androidApp

import android.os.Bundle
import android.view.View
import com.elevenetc.zipher.androidApp.home.HomeFragment
import com.elevenetc.zipher.androidApp.navigation.Navigator
import com.elevenetc.zipher.shared.LockViewModel
import com.elevenetc.zipher.shared.LockViewModel.*
import com.elevenetc.zipher.shared.ViewModel.StateTransition
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject

class LockFragment : BaseFragment<LockViewModel>(R.layout.fragment_lock) {

    init {
        vm = get()
    }

    val navigator: Navigator by inject()

    //lateinit var editPassword: EditText
    lateinit var stateView: LockStateView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        launch {
            vm.onUserAction(GetLockState)
            vm.state.collect { state ->
                handleState(state)
            }
        }

        stateView = view.findViewById(R.id.lock_state_view)

//        editPassword = view.findViewById(R.id.edit_password)
//        editPassword.doAfterTextChanged {
//            val chars = it!!.chars()
//            vm.onUserAction(PasswordEntry(chars.toArray().map { intCh -> intCh.toChar() }))
//        }
//
//        editPassword.setOnKeyListener { _, keyCode, event ->
//            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN) {
//                vm.onUserAction(Next)
//                true
//            }
//            false
//        }

//        stateView.setOnKeyListener(object : View.OnKeyListener {
//            override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
//                if (keyCode == KeyEvent.KEYCODE_ENTER) {
//
//                } else {
//
//                }
//
//                return true
//            }
//        })

        stateView.userActionListener = { action ->
            vm.onUserAction(action)
        }
    }

    override fun onResume() {
        super.onResume()
        //stateView.focusAndShowKeyboard()
    }

    override fun handleState(transition: StateTransition) {

        val state = transition.currentState

        stateView.handleState(transition)

        if (state is LockCreated || state is Unlocked) {
            navigator.replaceRootScreen(HomeFragment.create())
        }

        //val stateView = view.findViewById<LockStateView>(R.id.lock_state_view)
        //val keyboardView = view.findViewById<LockKeyboard>(R.id.lock_keyboard)

        //stateView.textState.text = state.javaClass.simpleName

        if (state is Unlocked) {

        } else if (state is CreatingLock) {
            //editPassword.setText(state.lock.toString())
        } else if (state is CreatingLockVerify) {
            //editPassword.setText(state.verify.toString())
        }
    }
}