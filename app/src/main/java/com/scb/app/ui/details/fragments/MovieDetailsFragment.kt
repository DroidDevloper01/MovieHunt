package com.scb.app.ui.details.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.google.android.material.appbar.AppBarLayout
import com.scb.app.R
import com.scb.app.exception.Failure
import com.scb.app.exception.Failure.NetworkConnection
import com.scb.app.exception.Failure.ServerError
import com.scb.app.ui.extensions.*
import com.scb.app.network.data.MovieSpec
import com.scb.app.ui.animations.MovieDetailsAnimator
import com.scb.app.ui.core.BaseFragment
import com.scb.app.ui.details.viewmodels.MovieDetailsViewModel
import com.scb.app.ui.home.MovieFailure
import com.scb.app.ui.home.MovieView
import com.scb.app.util.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_movie_details.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject
import kotlin.math.abs
@AndroidEntryPoint
class MovieDetailsFragment : BaseFragment() {

    companion object {
        private const val PARAM_MOVIE = "param_movie"

        fun forMovie(movie: MovieView?) = MovieDetailsFragment().apply {
            arguments = bundleOf(PARAM_MOVIE to movie)
        }
    }
    @Inject
    lateinit var movieDetailsAnimator: MovieDetailsAnimator
    private val movieDetailsViewModel by viewModels<MovieDetailsViewModel>()

    override fun layoutId() = R.layout.fragment_movie_details

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        activity?.toolbar?.visibility = View.GONE

        movieDetailsAnimator = MovieDetailsAnimator()
        activity?.let { movieDetailsAnimator.postponeEnterTransition(it) }
    }

    private val APIKEY = "b9bd48a6"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        appbar_fragment?.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            when {

                abs(verticalOffset) == appBarLayout.totalScrollRange -> {
                    // Collapsed
//                    Log.e("Toolbar Collapsed", "offset = $verticalOffset")
                    movieTitleContainer.animate().alpha(0F).duration = 100
                    movieTitleContainer.visibility = View.GONE
                }
                verticalOffset == 0 -> {
                    // Expanded
//                    Log.e("Toolbar Expanded", "offset = $verticalOffset")
                    movieTitleContainer.animate().alpha(1F).duration = 100
                    movieTitleContainer.visibility = View.VISIBLE
                }
                else -> {
                    // Somewhere in between
//                    Log.e("Toolbar in-between", "offset = $verticalOffset")
                }
            }
        })
        if (firstTimeCreated(savedInstanceState)) {
            showProgress()
            movieDetailsViewModel.loadMovieDetails(
                APIKEY,
                (arguments?.get(PARAM_MOVIE) as MovieView).id
            )
        } else {
            movieDetailsAnimator.cancelTransition(moviePoster)
            moviePoster.loadFromUrl((requireArguments()[PARAM_MOVIE] as MovieView).poster)
        }


        with(movieDetailsViewModel) {
            observe(movieDetails, ::renderMovieDetails)
            failure(failure, ::handleFailure)
        }
    }

    override fun onBackPressed() {
        movieDetailsAnimator.fadeInvisible(scrollView, movieDetails)

        movieDetailsAnimator.cancelTransition(moviePoster)
    }

    private fun renderMovieDetails(movie: MovieSpec?) {
        movie?.let {
            with(movie) {
                activity?.let {
                    moviePoster.loadUrlAndPostponeEnterTransition(movie.Poster!!, it)
                    // toolbar.title = movie.Title
                }
                movieSummary.text = movie.Plot
                movieCast.text = movie.Actors
                movieDirector.text = movie.Director
                movieYear.text = movie.Year
                movieTitle.text = movie.Title
                movieGenre.text = movie.Genre
                movieRating.text = movie.imdbRating
                movieTime.text = movie.Runtime
                moviePopularity.text = movie.BoxOffice
                movieReviews.text = movie.imdbVotes
                movieScore.text = movie.Metascore
                movieWriter.text = movie.Writer

            }
        }
        movieDetailsAnimator.fadeVisible(scrollView, movieDetails)
        hideProgress()
    }

    private fun handleFailure(failure: Failure?) {
        hideProgress()
        when (failure) {
            is NetworkConnection -> {
                notify(R.string.failure_network_connection); close()
            }
            is ServerError -> {
                notify(R.string.failure_server_error); close()
            }
            is MovieFailure.NonExistentMovie -> {
                notify(R.string.failure_movie_non_existent); close()
            }
            else -> {
                notify(R.string.failure_server_error); close()
            }
        }
        hideProgress()
    }

}
