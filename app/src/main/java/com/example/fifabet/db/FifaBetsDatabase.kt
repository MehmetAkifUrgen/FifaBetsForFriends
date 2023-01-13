package com.example.fifabet.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [Bahis::class], version = 9)
abstract class FifaBetsDatabase : RoomDatabase() {

    abstract fun betDao(): BetDao

}


