package com.scb.app.ui.details.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.scb.app.network.data.MovieSpec
import com.scb.app.ui.core.BaseViewModel
import com.scb.app.ui.details.usecases.GetMovieDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel
@Inject constructor(
    private val getMovieDetails: GetMovieDetails
) : BaseViewModel() {

    private val _movieDetails: MutableLiveData<MovieSpec> = MutableLiveData()
    val movieDetails: LiveData<MovieSpec> = _movieDetails
    fun loadMovieDetails(apiKey: String, movieId: String) =
        getMovieDetails(GetMovieDetails.Params(apiKey, movieId), viewModelScope) {
            it.fold(
                ::handleFailure,
                ::handleMovieDetails
            )
        }

    private fun handleMovieDetails(movie: MovieSpec) {
        _movieDetails.value = movie
    }

}
