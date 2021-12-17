package com.scb.app.network

import com.scb.app.UnitTest
import com.scb.app.network.api.MoviesRepository
import com.scb.app.ui.details.usecases.GetMovieDetails
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import com.scb.app.functional.Either.Left
import com.scb.app.functional.Either.Right
import com.scb.app.exception.Failure.ServerError
import com.scb.app.exception.Failure.NetworkConnection
import com.scb.app.network.data.MovieSpec

class GetMovieDetailsTest : UnitTest() {

    private lateinit var getMovieDetails: GetMovieDetails

    @MockK private lateinit var moviesRepository: MoviesRepository

    @Before fun setUp() {
        getMovieDetails = GetMovieDetails(moviesRepository)
        every { moviesRepository.movieDetails(APIKEY, MOVIE_ID)} returns Right(MovieSpec.empty)
    }

    @Test fun `should get data from repository`() {
        runBlocking { getMovieDetails.run(GetMovieDetails.Params(APIKEY, MOVIE_ID)) }

        verify(exactly = 1) { moviesRepository.movieDetails(APIKEY, MOVIE_ID) }
    }

    companion object {
        private const val MOVIE_ID: String = "1"
        private const val APIKEY = "key"
    }
}
