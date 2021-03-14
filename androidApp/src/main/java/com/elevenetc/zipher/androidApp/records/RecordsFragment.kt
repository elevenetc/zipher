package com.elevenetc.zipher.androidApp.records

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.elevenetc.zipher.Record
import com.elevenetc.zipher.androidApp.App
import com.elevenetc.zipher.androidApp.BaseFragment
import com.elevenetc.zipher.androidApp.R
import com.elevenetc.zipher.androidApp.details.RecordDetailsFragment
import com.elevenetc.zipher.androidApp.navigation.Navigator
import com.elevenetc.zipher.shared.Dao
import com.elevenetc.zipher.shared.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class RecordsFragment : BaseFragment(R.layout.fragment_home) {

    val recordsRepository: Dao by inject()
    val navigator: Navigator by inject()

    val backScope = CoroutineScope(Dispatchers.IO)

    val vm = RecordsViewModel(App.dao)
    val adapter = RecordsAdapter { selectedRecord ->
        openDetails(selectedRecord)
    }
    var page = 0
    val limit = 10

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //val allRecords = recordsRepository.getRecords(0, 10)

        view.findViewById<View>(R.id.btn_add_record).setOnClickListener {
            backScope.launch {
                val time = System.currentTimeMillis().toString()
                recordsRepository.insertRecord(time)
            }
        }

        val recordsRecycler = view.findViewById<RecyclerView>(R.id.records_recycler)
        recordsRecycler.layoutManager = LinearLayoutManager(requireContext())

        recordsRecycler.adapter = adapter

        recordsRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (!recyclerView.canScrollVertically(RecyclerView.FOCUS_DOWN)) {
                    //if (!reachedEnd && vm.state.value !is Loading) {
                    if (!adapter.allLoaded) {
                        page++
                        vm.onUserAction(RecordsViewModel.GetRecords(page, page * limit, limit))
                    }
                }
            }
        })

        launch {

            vm.state.collect {
                val state = it.currentState

                if (state is ViewModel.InitState) {
                    vm.onUserAction(RecordsViewModel.GetRecords(page, 0, limit))
                } else if (state is RecordsViewModel.Result) {
                    adapter.add(state.flow, state.page)
                }
            }

//            allRecords.collect { records ->
//                recordsRecycler.adapter = RecordsAdapter(records) { selectedRecord ->
//                    openDetails(selectedRecord)
//                }
//            }
        }
    }

    private fun openDetails(record: Record) {
        val fragment = RecordDetailsFragment.create(record.id)
        navigator.addRootScreen(fragment, true)
    }
}