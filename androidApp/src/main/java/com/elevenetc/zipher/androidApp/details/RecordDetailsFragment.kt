package com.elevenetc.zipher.androidApp.details

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.elevenetc.zipher.androidApp.R
import com.elevenetc.zipher.androidApp.navigation.Navigator
import com.elevenetc.zipher.shared.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import kotlin.coroutines.CoroutineContext

class RecordDetailsFragment : Fragment(R.layout.fragment_record_details), CoroutineScope {

    val vm: DetailsViewModel by inject()
    val navigator: Navigator by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val id = requireArguments().getString("id")!!

        launch {
            vm.onUserAction(DetailsViewModel.GetRecord(id))
            vm.state.collect { state ->
                handleState(state)
            }
        }
    }

    private fun handleState(transition: ViewModel.StateTransition) {

        val state = transition.currentState

        if (state is ViewModel.Loading || state is DetailsViewModel.DeletingRecord) {
            view?.findViewById<View>(R.id.text_loading)?.visibility = View.VISIBLE
            view?.findViewById<View>(R.id.content_container)?.visibility = View.GONE
        } else if (state is DetailsViewModel.RecordResult) {
            val record = state.record
            val editValue = view?.findViewById<TextView>(R.id.edit_text_value)!!

            editValue.text = record.name

            view?.findViewById<View>(R.id.text_loading)?.visibility = View.GONE
            view?.findViewById<View>(R.id.content_container)?.visibility = View.VISIBLE
            view?.findViewById<TextView>(R.id.text_id)?.text = record.id.toString()
            view?.findViewById<TextView>(R.id.text_key)?.text = record.name

            view?.findViewById<TextView>(R.id.btn_delete)?.setOnClickListener {
                vm.onUserAction(DetailsViewModel.DeleteRecord(record.id))
            }

            view?.findViewById<TextView>(R.id.btn_update)?.setOnClickListener {
                val newValue = editValue.text.toString()
                vm.onUserAction(DetailsViewModel.UpdateRecord(record.id, record.copy(name = newValue)))
            }

        } else if (state is DetailsViewModel.DeletedSuccessfully) {
            navigator.goBack()
        }
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    companion object {
        fun create(id: String): RecordDetailsFragment {
            return RecordDetailsFragment().apply {
                arguments = Bundle().apply {
                    putString("id", id)
                }
            }
        }
    }
}