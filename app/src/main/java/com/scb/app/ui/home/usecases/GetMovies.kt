package com.scb.app.ui.home.usecases

import com.scb.app.network.data.MovieListResponse
import com.scb.app.interactor.UseCase
import com.scb.app.network.api.MoviesRepository
import javax.inject.Inject

class GetMovies
@Inject  constructor(private val moviesRepository: MoviesRepository) : UseCase<MovieListResponse, GetMovies.Params>() {

    override suspend fun run(params: Params) = moviesRepository.movies(params.apiKey,params.genre,params.type,params.page)
    data class Params(val apiKey: String,val genre: String,val type: String,val page: String)
 }
