package com.scb.app.network

import com.scb.app.network.data.MovieListResponse
import com.scb.app.network.data.Search
import com.scb.app.UnitTest
import com.scb.app.network.api.MoviesRepository
import com.scb.app.network.api.MoviesService
import com.scb.app.util.NetworkHandler
import io.mockk.Called
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldEqual
import org.junit.Before
import org.junit.Test
import retrofit2.Call
import retrofit2.Response
import com.scb.app.functional.Either.Right
import com.scb.app.exception.Failure.ServerError
import com.scb.app.exception.Failure.NetworkConnection
import com.scb.app.functional.Either
import com.scb.app.network.data.*
import com.scb.app.ui.extensions.empty

class MoviesRepositoryTest : UnitTest() {

    private lateinit var networkRepository: MoviesRepository.Network

    @MockK
    private lateinit var networkHandler: NetworkHandler

    @MockK
    private lateinit var service: MoviesService

    @MockK
    private lateinit var moviesCall: Call<MovieListResponse>

    @MockK
    private lateinit var moviesResponse: Response<MovieListResponse>

    @MockK
    private lateinit var movieDetailsCall: Call<MovieSpec>

    @MockK
    private lateinit var movieDetailsResponse: Response<MovieSpec>

    @Before
    fun setUp() {
        networkRepository = MoviesRepository.Network(networkHandler, service)
    }

    @Test
    fun `should return empty MovieListResponse by default`() {
        every { networkHandler.isNetworkAvailable() } returns true
        every { moviesResponse.body() } returns null
        every { moviesResponse.isSuccessful } returns true
        every { moviesCall.execute() } returns moviesResponse
        every { service.movies(APIKEY, GENRE, TYPE, PAGE) } returns moviesCall

        val movies = networkRepository.movies(APIKEY, GENRE, TYPE, PAGE)

        movies shouldEqual Right(MovieListResponse.empty)
        verify(exactly = 1) { service.movies(APIKEY, GENRE, TYPE, PAGE) }
    }

    @Test
    fun `should get movie list from service`() {
        every { networkHandler.isNetworkAvailable() } returns true
        every { moviesResponse.body() } returns MovieListResponse(
            listOf(
                Search(
                    "Movie",1990,"id","movie","poster"
                )
            ), 1, true
        )
        every { moviesResponse.isSuccessful } returns true
        every { moviesCall.execute() } returns moviesResponse
        every { service.movies(APIKEY, GENRE, TYPE, PAGE) } returns moviesCall

        val movies = networkRepository.movies(APIKEY, GENRE, TYPE, PAGE)

        movies shouldEqual Right(
            MovieListResponse(
                listOf<Search>(Search("Movie", 1990, "id", "movie", "poster")),
                1,
                true
            )
        )
        verify(exactly = 1) { service.movies(APIKEY, GENRE, TYPE, PAGE) }
    }

    @Test
    fun `movies service should return network failure when no connection`() {
        every { networkHandler.isNetworkAvailable() } returns false

        val movies = networkRepository.movies(APIKEY, GENRE, TYPE, PAGE)

        movies shouldBeInstanceOf Either::class.java
        movies.isLeft shouldEqual true
        movies.fold({ failure -> failure shouldBeInstanceOf NetworkConnection::class.java }, {})
        verify { service wasNot Called }
    }

    @Test
    fun `movies service should return server error if no successful response`() {
        every { networkHandler.isNetworkAvailable() } returns true
        every { moviesResponse.isSuccessful } returns false
        every { moviesCall.execute() } returns moviesResponse
        every { service.movies(APIKEY, GENRE, TYPE, PAGE) } returns moviesCall

        val movies = networkRepository.movies(APIKEY, GENRE, TYPE, PAGE)

        movies shouldBeInstanceOf Either::class.java
        movies.isLeft shouldEqual true
        movies.fold({ failure -> failure shouldBeInstanceOf ServerError::class.java }, {})
    }

    @Test
    fun `movies request should catch exceptions`() {
        every { networkHandler.isNetworkAvailable() } returns true
        every { moviesCall.execute() } returns moviesResponse
        every { service.movies(APIKEY, GENRE, TYPE, PAGE) } returns moviesCall

        val movies = networkRepository.movies(APIKEY, GENRE, TYPE, PAGE)

        movies shouldBeInstanceOf Either::class.java
        movies.isLeft shouldEqual true
        movies.fold({ failure -> failure shouldBeInstanceOf ServerError::class.java }, {})
    }

    @Test
    fun `should return empty movie details by default`() {
        every { networkHandler.isNetworkAvailable() } returns true
        every { movieDetailsResponse.body() } returns null
        every { movieDetailsResponse.isSuccessful } returns true
        every { movieDetailsCall.execute() } returns movieDetailsResponse
        every { service.movieDetails(APIKEY, MOVIE_ID) } returns movieDetailsCall

        val movieDetails = networkRepository.movieDetails(APIKEY, MOVIE_ID)

        movieDetails shouldEqual Right(MovieSpec.empty)
        verify(exactly = 1) { service.movieDetails(APIKEY, MOVIE_ID) }
    }

    @Test
    fun `should get movie details from service`() {
        every { networkHandler.isNetworkAvailable() } returns true
        every { movieDetailsResponse.body() } returns
                MovieSpec(
                    "Title",
                    "0000", "5", String.empty(), String.empty(),
                    String.empty(), String.empty(), String.empty(), String.empty()
                )
        every { movieDetailsResponse.isSuccessful } returns true
        every { movieDetailsCall.execute() } returns movieDetailsResponse
        every { service.movieDetails(APIKEY, MOVIE_ID) } returns movieDetailsCall

        val movieDetails = networkRepository.movieDetails(APIKEY, MOVIE_ID)

        movieDetails shouldEqual Right(
            MovieSpec(
                "Title",
                "0000", "5", String.empty(), String.empty(),
                String.empty(), String.empty(), String.empty(), String.empty()
            )
        )
        verify(exactly = 1) { service.movieDetails(APIKEY, MOVIE_ID) }
    }

    @Test
    fun `movie details service should return network failure when no connection`() {
        every { networkHandler.isNetworkAvailable() } returns false

        val movieDetails = networkRepository.movieDetails(APIKEY, MOVIE_ID)

        movieDetails shouldBeInstanceOf Either::class.java
        movieDetails.isLeft shouldEqual true
        movieDetails.fold(
            { failure -> failure shouldBeInstanceOf NetworkConnection::class.java },
            {})
        verify { service wasNot Called }
    }

    @Test
    fun `movie details service should return server error if no successful response`() {
        every { networkHandler.isNetworkAvailable() } returns true
        every { movieDetailsResponse.body() } returns null
        every { movieDetailsResponse.isSuccessful } returns false
        every { movieDetailsCall.execute() } returns movieDetailsResponse
        every { service.movieDetails(APIKEY, MOVIE_ID) } returns movieDetailsCall

        val movieDetails = networkRepository.movieDetails(APIKEY, MOVIE_ID)

        movieDetails shouldBeInstanceOf Either::class.java
        movieDetails.isLeft shouldEqual true
        movieDetails.fold({ failure -> failure shouldBeInstanceOf ServerError::class.java }, {})
    }

    @Test
    fun `movie details request should catch exceptions`() {
        every { networkHandler.isNetworkAvailable() } returns true
        every { movieDetailsCall.execute() } returns movieDetailsResponse
        every { service.movieDetails(APIKEY, MOVIE_ID) } returns movieDetailsCall

        val movieDetails = networkRepository.movieDetails(APIKEY, MOVIE_ID)

        movieDetails shouldBeInstanceOf Either::class.java
        movieDetails.isLeft shouldEqual true
        movieDetails.fold({ failure -> failure shouldBeInstanceOf ServerError::class.java }, {})
    }

    companion object {
        private const val MOVIE_ID: String = "1"
        private const val APIKEY = "key"
        private const val TYPE = "movie"
        private const val GENRE = "genre"
        private const val PAGE = "1"
    }
}