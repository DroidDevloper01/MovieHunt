package com.scb.app.ui.home.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import androidx.annotation.StringRes
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.scb.app.R
import com.scb.app.exception.Failure
import com.scb.app.exception.Failure.ServerError
import com.scb.app.exception.Failure.NetworkConnection
import com.scb.app.ui.core.BaseFragment
import com.scb.app.ui.home.adapters.MoviesAdapter
import com.scb.app.ui.extensions.failure
import com.scb.app.ui.extensions.invisible
import com.scb.app.ui.extensions.observe
import com.scb.app.ui.extensions.visible
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.scb.app.network.api.MoviesRepository
import com.scb.app.ui.home.MovieFailure
import com.scb.app.ui.home.MovieView
import com.scb.app.ui.home.usecases.GetMovies
import com.scb.app.ui.home.viewmodels.MoviesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_movies.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject

@AndroidEntryPoint
class MoviesFragment : BaseFragment(), SearchView.OnQueryTextListener,
    android.widget.SearchView.OnCloseListener {
    @Inject
    lateinit var moviesAdapter: MoviesAdapter
    private var isLoading: Boolean = false
    private lateinit var moviesViewModel: MoviesViewModel

    private var visibleThreshold = 5
    private var lastVisibleItem = 0
    private var totalItemCount = 0

    override fun layoutId() = R.layout.fragment_movies

    private val PARAM_MOVIE = "param_movie"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        moviesViewModel = ViewModelProvider(requireActivity())[MoviesViewModel::class.java]
        activity?.toolbar?.title = "Film List"
        with(moviesViewModel) {
            observe(movies, ::renderMoviesList)
            failure(failure, ::handleFailure)
        }
    }


    override fun onResume() {
        super.onResume()
        Handler(Looper.getMainLooper()).postDelayed({
            activity?.toolbar?.visibility = View.VISIBLE
        }, 400)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeView()
        if (moviesViewModel.movies.value.isNullOrEmpty()) {
            loadMoviesList()
        } else {
            renderMoviesList(moviesViewModel.movies.value)
        }
    }


    private fun initializeView() {
        val movieList = view?.findViewById(R.id.movieList) as RecyclerView
        var layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        movieList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                //total no. of items
                totalItemCount = layoutManager.itemCount
                //last visible item position
                lastVisibleItem =
                    getLastVisibleItem(layoutManager.findLastCompletelyVisibleItemPositions(null))
                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    moviesViewModel.loadMovies()
                    isLoading = true
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    isLoading = false
                }
            }
        })
        movieList.layoutManager = layoutManager
        moviesAdapter = MoviesAdapter()
        movieList.adapter = moviesAdapter
        val options = navOptions {
            anim {
                enter = R.anim.slide_in_right
                exit = R.anim.slide_out_left
                popEnter = R.anim.slide_in_left
                popExit = R.anim.slide_out_right
            }
        }
//        moviesAdapter.listener = this
        moviesAdapter.clickListener = { movie ->
//            navigator.showMovieDetails(requireActivity(), movie, navigationExtras)
            val bundle = Bundle().apply {
                putParcelable(PARAM_MOVIE, MovieView(movie.id, movie.name, movie.poster))
            }
            findNavController().navigate(R.id.movie_details_dest, bundle, options)
        }
    }

    private fun loadMoviesList() {
        this.emptyView.invisible()
        this.movieList.visible()
        moviesViewModel.loadMovies()
    }

    private fun renderMoviesList(movies: List<MovieView>?) {
        isLoading = false
        moviesAdapter.addData(movies.orEmpty())
        hideProgress()
    }

    private fun handleFailure(failure: Failure?) {
        when (failure) {
            is NetworkConnection -> renderFailure(R.string.failure_network_connection)
            is ServerError -> renderFailure(R.string.failure_server_error)
            is MovieFailure.ListNotAvailable -> renderFailure(R.string.failure_movies_list_unavailable)
            else -> renderFailure(R.string.failure_server_error)
        }
    }

    private fun renderFailure(@StringRes message: Int) {
        hideProgress()
        if (moviesViewModel.movies.value == null || moviesViewModel.movies.value?.isNullOrEmpty() == true) {
            this.movieList.invisible()
            this.emptyView.visible()
        }
        notifyWithAction(message, R.string.action_refresh, ::loadMoviesList)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.app_menu, menu)
        val searchItem: MenuItem = menu.findItem(R.id.action_search)
        val searchView: SearchView = searchItem.actionView as SearchView
        searchView.maxWidth = Integer.MAX_VALUE
        searchView.queryHint = "Enter Movie Name"
        searchView.setOnQueryTextListener(this)
        super.onCreateOptionsMenu(menu, inflater)

    }

    override fun onQueryTextChange(query: String?): Boolean {
        // Here is where we are going to implement the filter logic
        Log.e("SearchText", "" + query)
        isLoading = true
        moviesAdapter.filter.filter(query)
        return false
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        moviesAdapter.filter.filter(query)
        isLoading = true
        return false
    }

    override fun onOptionsMenuClosed(menu: Menu) {
        super.onOptionsMenuClosed(menu)
        isLoading = false
    }

    fun getLastVisibleItem(lastVisibleItemPositions: IntArray): Int {
        var maxSize = 0
        for (i in lastVisibleItemPositions.indices) {
            if (i == 0) {
                maxSize = lastVisibleItemPositions[i]
            } else if (lastVisibleItemPositions[i] >= maxSize) {
                maxSize = lastVisibleItemPositions[i]
            }
        }
        return maxSize + 1
    }

    operator fun <T> MutableLiveData<ArrayList<T>>.plusAssign(values: List<T>) {
        val value = this.value ?: arrayListOf()
        value.addAll(values)
        this.value = value
    }

    override fun onClose(): Boolean {
        isLoading = false
        return false
    }

}
