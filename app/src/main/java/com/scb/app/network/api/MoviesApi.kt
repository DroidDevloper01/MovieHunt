package com.scb.app.network.api

import com.scb.app.network.data.MovieListResponse
import com.scb.app.network.data.MovieSpec
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

internal interface MoviesApi {
    companion object {
        private const val PARAM_MOVIE_ID = "movieId"
        private const val APIKEY = "apikey"
        private const val TYPE = "type"
        private const val GENRE = "s"
        private const val PAGE = "page"
        private const val ID = "i"
        private const val MOVIES = "/"
        private const val MOVIE_DETAILS = "/"
    }

    @GET(MOVIES)
    fun movies(
        @Query(APIKEY) apiKey: String,
        @Query(GENRE) genre: String,
        @Query(TYPE) type: String,
        @Query(PAGE) page: String
    ): Call<MovieListResponse>

    @GET(MOVIE_DETAILS)
    fun movieDetails(@Query(APIKEY) apiKey: String, @Query(ID) movieId: String): Call<MovieSpec>
}
