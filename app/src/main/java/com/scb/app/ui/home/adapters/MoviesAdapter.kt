package com.scb.app.ui.home.adapters

import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.scb.app.R
import com.scb.app.ui.extensions.inflate
import com.scb.app.ui.extensions.loadFromUrl
import com.scb.app.ui.home.MovieView
import javax.inject.Inject
import kotlin.properties.Delegates

class MoviesAdapter
@Inject constructor() : RecyclerView.Adapter<MoviesAdapter.ViewHolder>(), Filterable {

    internal var collection: List<MovieView> by Delegates.observable(emptyList()) { _, _, _ ->
        notifyDataSetChanged()
    }
    internal var searchResultCollection: List<MovieView> by Delegates.observable(emptyList()) { _, _, _ ->
        notifyDataSetChanged()
    }

    internal var clickListener: (MovieView) -> Unit = { _ -> }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(parent.inflate(R.layout.row_movie))

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) =
        viewHolder.bind(searchResultCollection[position], clickListener)

    override fun getItemCount() = searchResultCollection.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(movieView: MovieView, clickListener: (MovieView) -> Unit) {
            itemView.findViewById<ImageView>(R.id.moviePoster).loadFromUrl(movieView.poster)
            itemView.setOnClickListener {
                clickListener(movieView)
            }
            itemView.findViewById<TextView>(R.id.movieName).text = movieView.name
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint?.toString() ?: ""
                var searchResults = emptyList<MovieView>()
                searchResults = if (charString.isEmpty()) collection else {
                    val filteredList = ArrayList<MovieView>()
                    collection
                        .filter {
                            (it.id.contains(constraint!!)) or
                                    (it.name.contains(constraint, ignoreCase = true))

                        }
                        .forEach { filteredList.add(it) }
                    filteredList

                }
                return FilterResults().apply { values = searchResults }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {

                searchResultCollection = if (results?.values == null)
                    ArrayList()
                else
                    results.values as ArrayList<MovieView>
            }
        }
    }

    fun addData(list: List<MovieView>) {
        if (collection.isEmpty()) {
            collection = ArrayList<MovieView>()
        }
        (collection as kotlin.collections.ArrayList).clear()
        (collection as kotlin.collections.ArrayList).addAll(list)
        searchResultCollection = collection/*.value*/ as List<MovieView>
        notifyDataSetChanged()
    }
}
