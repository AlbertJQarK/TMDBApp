package com.albertgl.tmdbapp.model

import com.google.gson.annotations.SerializedName

import java.io.Serializable

/**
 * @author albertj (alberto.guillen.lobo@gmail.com)
 * @since 6-6-18
 */

class Movie(

        @SerializedName("id") val id: String,
        @SerializedName("title") val title: String,
        @SerializedName("release_date") val release_date: String,
        @SerializedName("overview") val overview: String,
        @SerializedName("poster_path") val posterPath: String,
        @SerializedName("popularity") val popularity: String


) : Serializable {

    constructor() : this("", "", "", "", "", "")

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
