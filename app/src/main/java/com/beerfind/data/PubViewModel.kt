package com.beerfind.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class PubViewModel(application: Application): AndroidViewModel(application) {

    private val readAllData: Flow<List<Pub>>
    private val repository: PubRepository

    init {
        val pubDao = PubRoomDatabase.getDatabase(application).pubDao()
        repository = PubRepository(pubDao)
        readAllData = repository.readAllData
    }

    fun addPub(pub: Pub) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addPub(pub)
        }
    }
}
