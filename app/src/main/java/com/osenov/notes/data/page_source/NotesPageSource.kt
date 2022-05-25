package com.osenov.notes.data.page_source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.osenov.notes.data.local.NoteDao
import com.osenov.notes.data.model.Note
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class NotesPageSource @AssistedInject constructor(
    @Assisted("query") private val query: String,
    private val noteDao: NoteDao
) : PagingSource<Int, Note>() {

    companion object {
        const val INITIAL_PAGE_NUMBER = 1
    }

    override fun getRefreshKey(state: PagingState<Int, Note>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchorPosition) ?: return null
        return page.prevKey?.plus(1) ?: page.nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Note> {
        val numberPage: Int = params.key ?: INITIAL_PAGE_NUMBER
        val prevKey = if (numberPage == INITIAL_PAGE_NUMBER) null else numberPage - 1
        val notes = noteDao.getNotes("%${query}%")
            .map { charactersWithEpisodesUrl ->
                charactersWithEpisodesUrl.toNote()
            }

        val nextKey = if (notes.size < 40) null else numberPage + 1
        return LoadResult.Page(notes, prevKey, nextKey)
    }

    @AssistedFactory
    interface Factory {

        fun create(
            @Assisted("query") query: String
        ): NotesPageSource
    }
}