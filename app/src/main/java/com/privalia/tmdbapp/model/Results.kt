package com.privalia.tmdbapp.model

import java.util.ArrayList

/**
 * @author albertj (alberto.guillen.lobo@gmail.com)
 * @since 6-6-18
 */

data class Results(
        val total_results: kotlin.Int,
        val total_pages: kotlin.Int,
        val page: kotlin.Int,
        val results: List<Movie> = ArrayList()
)