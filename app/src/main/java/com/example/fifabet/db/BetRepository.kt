package com.example.fifabet.db

import javax.inject.Inject

class BetRepository @Inject constructor(private val dao: BetDao) {

    val bets = dao.getAllBets()


    suspend fun insert(bahis: Bahis) {
        dao.insertBet(bahis)
    }


    suspend fun update(bahis: Bahis) {
        dao.updateBet(bahis)
    }

    suspend fun delete(bahis: Bahis) {
        dao.deleteBet(bahis)
    }


    suspend fun deleteAll() {
        dao.deleteAll()
    }

}