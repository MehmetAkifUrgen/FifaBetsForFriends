package com.example.fifabet.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


@Database(entities = [Bahis::class,ByRoom::class,Kupon::class], version = 15)
@TypeConverters(Converters::class)
abstract class FifaBetsDatabase : RoomDatabase() {

    abstract fun betDao(): BetDao

}


