package com.scb.app.ui.home.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.scb.app.ui.core.BaseViewModel
import com.scb.app.ui.home.MovieView
import com.scb.app.ui.home.usecases.GetMovies

import com.scb.app.network.data.MovieListResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.math.ceil

@HiltViewModel
class MoviesViewModel
@Inject constructor(private val getMovies: GetMovies) : BaseViewModel() {
    private val _movies: MutableLiveData<List<MovieView>> = MutableLiveData()
    val movies: LiveData<List<MovieView>> = _movies
    var totalPages: Int = 0;

    private val APIKEY = "b9bd48a6"
    private val TYPE = "movie"
    private val GENRE = "Marvel"
    fun loadMovies() {
        val page: Int =
            if (_movies.value == null) 0 else ceil(_movies.value!!.size.toDouble() / 10).toInt()
        if (page <= 1 || (page) <= totalPages) {
            getMovies(
                GetMovies.Params(APIKEY, GENRE, TYPE, (page + 1).toString()),
                viewModelScope
            ) {
                it.fold(
                    ::handleFailure,
                    ::handleMovieList
                )
            }
        }
    }

    private fun handleMovieList(movies: MovieListResponse) {
        totalPages = movies.totalResults / 10

        var tempList = _movies.value.orEmpty()
        if (tempList.isEmpty()) {
            tempList = ArrayList<MovieView>()
        }
        val moviesList = movies.search.orEmpty()
        val localList: List<MovieView> =
            moviesList.map { MovieView(it.imdbID, it.title, it.poster) }
        val movieSet: MutableSet<MovieView> = LinkedHashSet(tempList)
        movieSet.addAll(localList)
        _movies.value = ArrayList(movieSet)
    }

}
