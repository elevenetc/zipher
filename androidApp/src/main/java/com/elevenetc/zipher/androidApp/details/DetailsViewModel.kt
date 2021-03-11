package com.elevenetc.zipher.androidApp.details

import com.elevenetc.zipher.Record
import com.elevenetc.zipher.shared.Dao
import com.elevenetc.zipher.shared.ViewModel
import kotlinx.coroutines.launch

class DetailsViewModel(private val repository: Dao) : ViewModel() {

    override fun onUserAction(action: UserAction) {
        when (action) {
            is GetRecord -> {
                launch {
                    val recordId = action.id
                    repository.getAllRecords()
                    val record = repository.getById(recordId)
                    if (record == null) {
                        updateState(RecordNotFoundResult(recordId))
                    } else {
                        updateState(RecordResult(record))
                    }

                }
            }
            is DeleteRecord -> {
                launch {
                    updateState(DeletingRecord(action.id))
                    repository.deleteById(action.id)
                    updateState(DeletedSuccessfully(action.id))
                }
            }
            is UpdateRecord -> {
                launch {
                    val record = action.updatedRecord
                    updateState(UpdatingRecord(action.id))
                    val updated = repository.update(record)
                    val get = repository.getById(record.id)
                    updateState(RecordResult(get!!))
                }
            }
        }
    }

    /**
     * User actions
     */
    data class GetRecord(val id: String) : UserAction()
    data class DeleteRecord(val id: String) : UserAction()
    data class UpdateRecord(val id: String, val updatedRecord: Record) : UserAction()

    /**
     * View states
     */
    data class RecordResult(val record: Record) : ViewState()
    data class RecordNotFoundResult(val id: String) : ViewState()
    data class DeletingRecord(val id: String) : ViewState()
    data class UpdatingRecord(val id: String) : ViewState()
    data class DeletedSuccessfully(val id: String) : ViewState()


}