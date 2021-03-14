package com.elevenetc.zipher.androidApp.records

import com.elevenetc.zipher.Record
import com.elevenetc.zipher.shared.Dao
import com.elevenetc.zipher.shared.ViewModel
import kotlinx.coroutines.flow.Flow

class RecordsViewModel(val dao: Dao) : ViewModel() {

    override fun onUserAction(action: UserAction) {
        if (action is GetRecords) {
            val offset = action.offset
            val limit = action.limit
            val page = action.page
            updateState(Result(dao.getRecords(offset, limit), page, offset, limit))
        }
    }

    data class Result(
        val flow: Flow<List<Record>>,
        val page: Int,
        val offset: Int,
        val limit: Int
    ) : ViewState()

    data class GetRecords(val page: Int, val offset: Int, val limit: Int) : UserAction()
}