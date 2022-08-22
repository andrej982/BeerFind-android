package com.beerfind.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PubDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(pub: Pub)

    @Update
    suspend fun update(pub: Pub)

    @Query("DELETE FROM pubs WHERE name = :name AND address = :address")
    suspend fun delete(name: String, address: String)

    @Query("SELECT * from pubs WHERE name = :name")
    fun getItem(name: String): Flow<Pub>

    @Query("SELECT * from pubs ORDER BY id ASC")
    fun getItems(): Flow<List<Pub>>

    @Query("DELETE FROM pubs")
    suspend fun deleteAll()
}
