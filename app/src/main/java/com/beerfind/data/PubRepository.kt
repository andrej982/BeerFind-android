package com.beerfind.data

import kotlinx.coroutines.flow.Flow

class PubRepository(private val pubDao: PubDao) {

    val readAllData: Flow<List<Pub>> = pubDao.getItems()

    suspend fun addPub(pub: Pub) {
        pubDao.insert(pub)
    }
}
