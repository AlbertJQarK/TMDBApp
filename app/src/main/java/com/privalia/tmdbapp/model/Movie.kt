package com.privalia.tmdbapp.model

import com.google.gson.annotations.SerializedName

import java.io.Serializable

/**
 * @author albertj (alberto.guillen.lobo@gmail.com)
 * @since 6-6-18
 */
data class Movie(
        @SerializedName("id") val id: String,
        @SerializedName("release_date") var release_date: String,
        @SerializedName("popularity") var popularity: Float,
        @SerializedName("title") var title: String,
        @SerializedName("overview") var overview: String,
        @SerializedName("poster_path") private var posterPath: String,
        @SerializedName("backdrop_path") private var backdropPath: String

) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as Movie
        if (other.id == id) return true

        return false
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + title.hashCode()
        return result
    }
}
