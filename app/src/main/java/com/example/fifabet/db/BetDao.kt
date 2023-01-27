package com.example.fifabet.db

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

@Dao
interface BetDao {
    @Insert
    suspend fun insertByRoom(byRoom: ByRoom): Long
    @Delete
    suspend fun deleteByRoom(byRoom: ByRoom)
    @Query("SELECT * FROM byroom_table")
    fun getAllByRoom():LiveData<List<ByRoom>>




    @Insert
    suspend fun insertKupon(kupon: Kupon): Long

    @Delete
    suspend fun deleteKupon(kupon: Kupon)

    @Query("SELECT * FROM kupon_table")
    fun getAllKupon():LiveData<List<Kupon>>




    @Insert
    suspend fun insertBet(bahis: Bahis): Long

    @Update
    suspend fun updateBet(bahis: Bahis)

    @Delete
    suspend fun deleteBet(bahis: Bahis)

    @Query("DELETE FROM bahis_table")
    suspend fun deleteAll()

    @Query("SELECT * FROM bahis_table")
      fun getAllBets():LiveData<List<Bahis>>
}