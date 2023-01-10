package com.example.fifabet.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Bahis::class], version = 7)
abstract class FifaBetsDatabase : RoomDatabase() {

    abstract val betDao: BetDao

    companion object {
        @Volatile
        private var INSTANCE: FifaBetsDatabase? = null
        fun getInstance(context: Context): FifaBetsDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        FifaBetsDatabase::class.java,
                        "bets_data_database"
                    ).allowMainThreadQueries().fallbackToDestructiveMigration().build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}


