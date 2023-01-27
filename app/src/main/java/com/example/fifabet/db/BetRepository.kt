package com.example.fifabet.db

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BetRepository @Inject constructor(private val dao: BetDao) {

    val bets=dao.getAllBets()
    val byRooms=dao.getAllByRoom()
    val kupons=dao.getAllKupon()


    suspend fun insert(bahis: Bahis) {
        dao.insertBet(bahis)
    }

    suspend fun insertByRoom(byRoom: ByRoom) {
        dao.insertByRoom(byRoom)
    }

    suspend fun deleteByRoom(byRoom: ByRoom) {
        dao.deleteByRoom(byRoom)
    }

    suspend fun insertKupon(kupon: Kupon) {
        dao.insertKupon(kupon)
    }

    suspend fun deleteKupon(kupon: Kupon) {
        dao.deleteKupon(kupon)
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