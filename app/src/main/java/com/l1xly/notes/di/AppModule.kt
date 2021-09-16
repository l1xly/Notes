package com.l1xly.notes.di

import android.content.Context
import com.l1xly.notes.db.NoteDao
import com.l1xly.notes.db.NoteDatabase
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
    fun provideDatabase(@ApplicationContext context: Context): NoteDatabase {
        return NoteDatabase.getDatabase(context)
    }

    @Singleton
    @Provides
    fun provideDao(database: NoteDatabase): NoteDao {
        return database.noteDao()
    }
}