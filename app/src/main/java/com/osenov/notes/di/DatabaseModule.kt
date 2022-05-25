package com.osenov.notes.di

import android.content.ContentValues
import android.content.Context
import androidx.room.OnConflictStrategy
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.osenov.notes.data.local.NoteDao
import com.osenov.notes.data.local.NotesDatabase
import com.osenov.notes.data.local.NotesDatabase.Companion.DB_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): NotesDatabase {
        return Room.databaseBuilder(
            appContext,
            NotesDatabase::class.java,
            DB_NAME
        ).addCallback(object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                val cv = ContentValues()
                cv.put("id", 0L)
                cv.put("title", "First Note")
                cv.put("description", "Your Note Description")
                cv.put("dateCreate", System.currentTimeMillis())
                db.insert("notes", OnConflictStrategy.REPLACE, cv)
            }
        })
            .build()
    }

    @Provides
    @Singleton
    fun provideCharacterDao(noteDatabase: NotesDatabase): NoteDao {
        return noteDatabase.noteDao()
    }
}