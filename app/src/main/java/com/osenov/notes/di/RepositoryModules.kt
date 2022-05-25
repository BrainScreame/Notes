package com.osenov.notes.di

import com.osenov.notes.data.repository.NotesRepository
import com.osenov.notes.data.repository.impl.NotesRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModules {
    @Binds
    fun provideNotesRepositoryImpl(repository: NotesRepositoryImpl): NotesRepository
}