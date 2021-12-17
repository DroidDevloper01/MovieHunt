package com.scb.app.ui.home.viewmodels

import com.scb.app.AndroidTest
import com.scb.app.exception.Failure
import com.scb.app.functional.Either
import com.scb.app.network.data.MovieListResponse
import com.scb.app.network.data.Search
import com.scb.app.ui.home.usecases.GetMovies
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.amshove.kluent.shouldEqualTo
import org.junit.Assert.*
import org.junit.Before

import org.junit.Test

class MoviesViewModelTest  : AndroidTest() {


    private lateinit var moviesViewModel: MoviesViewModel

    @MockK
    private lateinit var getMovies: GetMovies

    @Before
    fun setUp() {
        moviesViewModel = MoviesViewModel(getMovies)
    }

    @Test
    fun getMovies() {
        val moviesList = MovieListResponse(listOf(Search( "IronMan",1989,"idx01","movie","url"), Search( "Batman",1990,"idx00","movie","url")),2,true)
        coEvery { getMovies.run(any()) } returns Either.Left(Failure.NetworkConnection)
        moviesViewModel.movies.observeForever {
            it!!.size shouldEqualTo 1
            it[0].id shouldEqualTo "0"
            it[0].poster shouldEqualTo "IronMan"
            it[1].id shouldEqualTo "1"
            it[1].poster shouldEqualTo "Batman"
        }

        runBlocking { moviesViewModel.loadMovies() }
    }

    @Test
    fun getTotalPages() {
    }

    @Test
    fun setTotalPages() {
    }
}