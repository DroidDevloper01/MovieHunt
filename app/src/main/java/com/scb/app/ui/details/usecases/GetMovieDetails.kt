package com.scb.app.ui.details.usecases

import com.scb.app.interactor.UseCase
import com.scb.app.network.api.MoviesRepository
import com.scb.app.network.data.MovieSpec
import javax.inject.Inject

class GetMovieDetails
@Inject constructor(private val moviesRepository: MoviesRepository) :
    UseCase<MovieSpec, GetMovieDetails.Params>() {

    override suspend fun run(params: Params) = moviesRepository.movieDetails(params.apiKey, params.id)

    data class Params(val apiKey: String, val id: String)
}
