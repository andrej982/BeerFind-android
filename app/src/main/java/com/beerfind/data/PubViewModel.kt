package com.beerfind.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class PubViewModel(application: Application): AndroidViewModel(application) {

    val favouritePubsLiveData = MutableLiveData<List<Pub>>()

    private val repository: PubRepository

    init {
        val pubDao = PubRoomDatabase.getDatabase(application).pubDao()
        repository = PubRepository(pubDao)
        viewModelScope.launch {
            repository.readAllData.collect { pubs ->
                favouritePubsLiveData.postValue(pubs)
            }
        }
    }

    fun addPub(pub: Pub) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addPub(pub)
        }
    }

    fun deleteAll() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAll()
        }
    }

    fun deletePub(name: String, address: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deletePub(name, address)
        }
    }
}
