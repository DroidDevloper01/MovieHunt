package com.scb.app.core.viewmodels

import com.scb.app.network.data.MovieListResponse
import com.scb.app.network.data.Search
import com.scb.app.AndroidTest
import com.scb.app.ui.home.usecases.GetMovies
import com.scb.app.ui.home.viewmodels.MoviesViewModel
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.amshove.kluent.shouldEqualTo
import org.junit.Before
import org.junit.Test

import com.scb.app.functional.Either.Right

class MoviesViewModelTest : AndroidTest() {


    private lateinit var moviesViewModel: MoviesViewModel

    @MockK private lateinit var getMovies: GetMovies

    @Before
    fun setUp() {
        moviesViewModel = MoviesViewModel(getMovies)
    }

    @Test fun `loading movies should update live data`() {
        val moviesList = MovieListResponse(listOf(Search( "IronMan",1989,"idx01","movie","url"), Search( "Batman",1990,"idx00","movie","url")),2,true)
        coEvery { getMovies.run(any()) } returns Right(moviesList)

        moviesViewModel.movies.observeForever {
            it!!.size shouldEqualTo 1
            it[0].id shouldEqualTo "0"
            it[0].poster shouldEqualTo "IronMan"
            it[1].id shouldEqualTo "1"
            it[1].poster shouldEqualTo "Batman"
        }

        runBlocking { moviesViewModel.loadMovies() }
    }
}