package com.scb.app.network.api

import com.scb.app.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.content.Context
import okhttp3.Cache
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MoviesService
@Inject constructor(retrofit: Retrofit) : MoviesApi {
    private val moviesApi by lazy { retrofit.create(MoviesApi::class.java) }
    override fun movies(apiKey: String, genre: String, type: String, page: String) = moviesApi.movies(apiKey , genre , type,page)
    override fun movieDetails(apiKey: String, movieId: String) = moviesApi.movieDetails(apiKey,movieId)

}
