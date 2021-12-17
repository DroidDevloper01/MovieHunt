package com.scb.app.network

import com.scb.app.network.data.MovieListResponse
import com.scb.app.network.data.Search
import com.scb.app.UnitTest
import com.scb.app.network.api.MoviesRepository
import com.scb.app.ui.home.usecases.GetMovies
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

import com.scb.app.functional.Either.Right

class GetMoviesTest : UnitTest() {

    private lateinit var getMovies: GetMovies

    @MockK
    private lateinit var moviesRepository: MoviesRepository

    @Before
    fun setUp() {
        getMovies = GetMovies(moviesRepository)
        every { moviesRepository.movies(APIKEY, GENRE, TYPE, PAGE) } returns Right(
            MovieListResponse(
                listOf<Search>(Search("Movie", 1990, "id", "movie", "poster")),
                1,
                true
            )
        )
    }

    @Test
    fun `should get data from repository`() {
        runBlocking { getMovies.run(GetMovies.Params(APIKEY, GENRE, TYPE, PAGE)) }

        verify(exactly = 1) { moviesRepository.movies(APIKEY, GENRE, TYPE, PAGE) }
    }

    companion object {
        private const val MOVIE_ID: String = "1"
        private const val APIKEY = "key"
        private const val TYPE = "movie"
        private const val GENRE = "genre"
        private const val PAGE = "1"
    }
}
