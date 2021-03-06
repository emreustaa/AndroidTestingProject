package com.example.artbooktesting.dependencyinjection

import android.content.Context
import androidx.room.Room
import com.example.artbooktesting.roomdb.ArtsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TestAppModule {

    @Singleton
    @Provides
    @Named("testDatabase")
    fun injectInMemoryRoom(@ApplicationContext context: Context) =
        Room.inMemoryDatabaseBuilder(context, ArtsDatabase::class.java).allowMainThreadQueries()
            .build()

}