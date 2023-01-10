package com.example.fifabet.db

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

@Dao
interface BetDao {

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