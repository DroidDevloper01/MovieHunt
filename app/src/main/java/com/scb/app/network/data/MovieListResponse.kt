package com.scb.app.network.data

import com.google.gson.annotations.SerializedName

data class MovieListResponse(

    @SerializedName("Search") val search: List<Search>,
    @SerializedName("totalResults") val totalResults: Int,
    @SerializedName("Response") val response: Boolean
) {

    companion object {
        val empty = MovieListResponse(
            emptyList(), 0, false
        )

    }
}

data class Search(

    @SerializedName("Title") val title: String,
    @SerializedName("Year") val year: Int,
    @SerializedName("imdbID") val imdbID: String,
    @SerializedName("Type") val type: String,
    @SerializedName("Poster") val poster: String
)
