package com.scb.app.core.viewmodels

import com.scb.app.AndroidTest
import com.scb.app.network.data.MovieSpec
import com.scb.app.ui.details.usecases.GetMovieDetails
import com.scb.app.ui.details.viewmodels.MovieDetailsViewModel
import com.scb.app.ui.extensions.empty
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.amshove.kluent.shouldEqualTo
import org.junit.Before
import org.junit.Test

import com.scb.app.functional.Either.Right
import org.amshove.kluent.any
import org.hamcrest.core.Every

class MovieDetailsViewModelTest : AndroidTest() {

    private lateinit var movieDetailsViewModel: MovieDetailsViewModel

    @MockK
    private lateinit var getMovieDetails: GetMovieDetails

    @Before
    fun setUp() {
        movieDetailsViewModel = MovieDetailsViewModel(getMovieDetails)
    }

    @Test
    fun `loading movie details should update live data`() {
        val movieDetails = MovieSpec(
            "IronMan",
            "2008", "5", String.empty(), String.empty(), String.empty(), String.empty(),
            "Jon Favreau",
            " Robert Downey Jr",
            "summary",
            String.empty(), String.empty(), String.empty(),
            "Poster",
            listOf(),
            String.empty(), String.empty(), String.empty(),
            "idx008",
            "movie",
            String.empty(), String.empty(), String.empty(), String.empty(), String.empty()
        )
        coEvery { getMovieDetails.run(any()) } returns Right(movieDetails)

        movieDetailsViewModel.movieDetails.observeForever {
            with(it!!) {
                imdbID shouldEqualTo "0"
                Title shouldEqualTo "IronMan"
                Poster shouldEqualTo "poster"
                Plot shouldEqualTo "summary"
                Actors shouldEqualTo " Robert Downey Jr"
                Director shouldEqualTo "Jon Favreau"
                Year shouldEqualTo "2018"
                Type shouldEqualTo "movie"
                imdbID shouldEqualTo "idx008"
            }
        }

        runBlocking { movieDetailsViewModel.loadMovieDetails(APIKEY, MOVIE_ID) }
    }

    companion object {
        private const val MOVIE_ID: String = "1"
        private const val APIKEY = "key"
    }
}