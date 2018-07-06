package com.privalia.tmdbapp.presenter

import com.privalia.tmdbapp.BuildConfig
import com.privalia.tmdbapp.adapter.PaginationAdapter
import com.privalia.tmdbapp.api.APIService
import com.privalia.tmdbapp.model.Movie
import com.privalia.tmdbapp.model.Results

import java.util.ArrayList

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * @author albertj (alberto.guillen.lobo@gmail.com)
 * @since 6-6-18
 */

class PresenterMain(private val mainView: MainView) {

    private var pagAdapter: PaginationAdapter = PaginationAdapter()
    private val API by lazy {
        APIService.create()
    }

    private companion object {
        private const val PAGE_START = 1
    }

    var isLoading = false
    var isLastPage = false
    var total_pages: Int = 0
    var currentPage = PAGE_START


    fun loadFirstPage() {
        currentPage = 1
        API.getTopRatedMovies(BuildConfig.TMDB_API_KEY, "en_US", currentPage).enqueue(object : Callback<Results> {
            override fun onResponse(call: Call<Results>, response: Response<Results>) {

                val results = fetchResults(response)
                pagAdapter.removeAll()
                pagAdapter.addAll(results)
                mainView.setRV(pagAdapter)

                if (currentPage != total_pages) {
                    pagAdapter.addLoadingFooter()
                } else {
                    isLastPage = true
                }
            }

            override fun onFailure(call: Call<Results>, t: Throwable) {
                t.printStackTrace()
                // TODO: Handle failure
            }
        })
    }


    fun loadNextPage() {
        API.getTopRatedMovies(BuildConfig.TMDB_API_KEY, "en_US", currentPage).enqueue(object : Callback<Results> {
            override fun onResponse(call: Call<Results>, response: Response<Results>) {

                isLoading = false
                val results = fetchResults(response)
                pagAdapter.removeLoadingFooter()
                pagAdapter.addAll(results)

                if (currentPage != total_pages)
                    pagAdapter.addLoadingFooter()
                else
                    isLastPage = true
            }

            override fun onFailure(call: Call<Results>, t: Throwable) {
                t.printStackTrace()
                // TODO: Handle failure
            }
        })
    }

    private fun fetchResults(response: Response<Results>): List<Movie> {
        val topRatedMovies = response.body()
        total_pages = topRatedMovies!!.total_pages
        val movies = ArrayList<Movie>()
        for (movie in topRatedMovies.results) {
            movies.add(Movie(movie.id, movie.title, movie.release_date, movie.overview, movie.posterPath))
        }

        return movies
    }
}
