package com.scb.app.network.data

import com.google.gson.annotations.SerializedName
import com.scb.app.ui.extensions.empty

data class MovieSpec(

    @SerializedName("Title") var Title: String = "",
    @SerializedName("Year") var Year: String = "",
    @SerializedName("Rated") var Rated: String = "",
    @SerializedName("Released") var Released: String = "",
    @SerializedName("Runtime") var Runtime: String = "",
    @SerializedName("Genre") var Genre: String = "",
    @SerializedName("Director") var Director: String = "",
    @SerializedName("Writer") var Writer: String = "",
    @SerializedName("Actors") var Actors: String? = null,
    @SerializedName("Plot") var Plot: String? = null,
    @SerializedName("Language") var Language: String? = null,
    @SerializedName("Country") var Country: String? = null,
    @SerializedName("Awards") var Awards: String? = null,
    @SerializedName("Poster") var Poster: String? = null,
    @SerializedName("Ratings") var Ratings: List<Ratings> = arrayListOf(),
    @SerializedName("Metascore") var Metascore: String? = null,
    @SerializedName("imdbRating") var imdbRating: String? = null,
    @SerializedName("imdbVotes") var imdbVotes: String? = null,
    @SerializedName("imdbID") var imdbID: String? = null,
    @SerializedName("Type") var Type: String? = null,
    @SerializedName("DVD") var DVD: String? = null,
    @SerializedName("BoxOffice") var BoxOffice: String? = null,
    @SerializedName("Production") var Production: String? = null,
    @SerializedName("Website") var Website: String? = null,
    @SerializedName("Response") var Response: String? = null

) {
    companion object {
        val empty = MovieSpec(
            String.empty(), String.empty(), String.empty(), String.empty(),
            String.empty(), String.empty(), String.empty(), String.empty()
        )

    }
}

data class Ratings(

    @SerializedName("Source") var Source: String? = null,
    @SerializedName("Value") var Value: String? = null

)

