package com.beerfind.data

import kotlinx.coroutines.flow.Flow

class PubRepository(private val pubDao: PubDao) {

    val readAllData: Flow<List<Pub>> = pubDao.getItems()

    suspend fun addPub(pub: Pub) {
        pubDao.insert(pub)
    }

    suspend fun deleteAll() {
        pubDao.deleteAll()
    }

    suspend fun deletePub(name: String, address: String){
        pubDao.delete(name, address)
    }
}
