package com.scb.app.network.api

import com.scb.app.network.data.MovieListResponse
import com.scb.app.exception.Failure
import com.scb.app.exception.Failure.ServerError
import com.scb.app.exception.Failure.NetworkConnection
import com.scb.app.functional.Either
import com.scb.app.functional.Either.Left
import com.scb.app.functional.Either.Right
import com.scb.app.network.data.MovieSpec
import com.scb.app.util.NetworkHandler
import retrofit2.Call
import javax.inject.Inject

interface MoviesRepository {
    fun movies(
        apiKey: String,
        genre: String,
        type: String,
        page: String
    ): Either<Failure, MovieListResponse>

    fun movieDetails(apiKey: String, movieId: String): Either<Failure, MovieSpec>

    class Network
    @Inject constructor(
        private val networkHandler: NetworkHandler,
        private val service: MoviesService
    ) : MoviesRepository {

        override fun movies(
            apiKey: String,
            genre: String,
            type: String,
            page: String
        ): Either<Failure, MovieListResponse> {
            return when (networkHandler.isNetworkAvailable()) {
                true -> request(
                    service.movies(apiKey, genre, type, page),
                    { it },
                    MovieListResponse.empty
                )
                false -> Left(NetworkConnection)
            }
        }

        override fun movieDetails(apiKey: String, movieId: String): Either<Failure, MovieSpec> {
            return when (networkHandler.isNetworkAvailable()) {
                true -> request(
                    service.movieDetails(apiKey, movieId),
                    { it },
                    MovieSpec.empty
                )
                false -> Left(NetworkConnection)
            }
        }

        private fun <T, R> request(
            call: Call<T>,
            transform: (T) -> R,
            default: T
        ): Either<Failure, R> {
            return try {
                val response = call.execute()
                when (response.isSuccessful) {
                    true -> Right(transform((response.body() ?: default)))
                    false -> Left(ServerError)
                }
            } catch (exception: Throwable) {
                Left(ServerError)
            }
        }
    }

}
