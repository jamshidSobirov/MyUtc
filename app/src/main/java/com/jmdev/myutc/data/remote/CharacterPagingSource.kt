package com.jmdev.myutc.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.jmdev.myutc.data.api.CharacterApi
import com.jmdev.myutc.data.model.Character
import retrofit2.HttpException
import java.io.IOException

class CharacterPagingSource(
    private val api: CharacterApi
) : PagingSource<Int, Character>() {
    
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Character> {
        val page = params.key ?: 1
        return try {
            val response = api.getCharacters(page)
            LoadResult.Page(
                data = response.results,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (response.info.next == null) null else page + 1
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Character>): Int? {
        return state.anchorPosition?.let { anchor ->
            state.closestPageToPosition(anchor)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchor)?.nextKey?.minus(1)
        }
    }
}
