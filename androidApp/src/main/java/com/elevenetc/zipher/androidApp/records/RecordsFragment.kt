package com.elevenetc.zipher.androidApp.records

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.elevenetc.zipher.Record
import com.elevenetc.zipher.androidApp.R
import com.elevenetc.zipher.androidApp.details.RecordDetailsFragment
import com.elevenetc.zipher.androidApp.navigation.Navigator
import com.elevenetc.zipher.shared.Dao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import kotlin.coroutines.CoroutineContext

class RecordsFragment : Fragment(), CoroutineScope {

    val recordsRepository: Dao by inject()
    val navigator: Navigator by inject()

    val backScope = CoroutineScope(Dispatchers.IO)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val allRecords = recordsRepository.getAllRecords()

        view.findViewById<View>(R.id.btn_add_record).setOnClickListener {
            backScope.launch {
                val time = System.currentTimeMillis().toString()
                recordsRepository.insertRecord(time)
            }
        }

        val recordsRecycler = view.findViewById<RecyclerView>(R.id.records_recycler)
        recordsRecycler.layoutManager = LinearLayoutManager(requireContext())

        launch {
            allRecords.collect { records ->
                recordsRecycler.adapter = RecordsAdapter(records) { selectedRecord ->
                    openDetails(selectedRecord)
                }
            }
        }
    }

    private fun openDetails(record: Record) {
        val fragment = RecordDetailsFragment.create(record.id)
        navigator.addRootScreen(fragment, true)
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main
}