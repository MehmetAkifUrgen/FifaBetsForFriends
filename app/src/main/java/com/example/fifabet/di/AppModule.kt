package com.example.fifabet.di

import android.content.Context
import androidx.room.Room
import com.example.fifabet.db.BetDao
import com.example.fifabet.db.BetRepository
import com.example.fifabet.db.FifaBetsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Singleton
    @Provides
    fun provideRoomDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(
        context.applicationContext,
        FifaBetsDatabase::class.java,
        "bets_data_database"
    ).allowMainThreadQueries().fallbackToDestructiveMigration().build()

    @Singleton
    @Provides
    fun provideDao(database: FifaBetsDatabase) = database.betDao()

    @Singleton
    @Provides
    fun provideNoteRepository(dao : BetDao) = BetRepository(dao)
}