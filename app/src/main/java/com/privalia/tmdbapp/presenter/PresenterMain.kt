package com.privalia.tmdbapp.presenter

import com.privalia.tmdbapp.BuildConfig
import com.privalia.tmdbapp.adapter.PaginationAdapter
import com.privalia.tmdbapp.api.APIService
import com.privalia.tmdbapp.model.Movie
import com.privalia.tmdbapp.model.Results

import android.support.v7.app.AlertDialog
import android.content.Context;

import java.util.ArrayList

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.app.Activity
import com.privalia.tmdbapp.R

/**
 * @author albertj (alberto.guillen.lobo@gmail.com)
 * @since 6-6-18
 */

class PresenterMain(private val context: Context, private val mainView: MainView) {

    private var pagAdapter: PaginationAdapter = PaginationAdapter()
    private val API by lazy {
        APIService.create()
    }

    private companion object {
        private const val PAGE_START = 1
        private const val LANGUAGE = "en_US"
    }

    var isLoading = false
    var isLastPage = false
    var total_pages: Int = 0
    var currentPage = PAGE_START


    fun loadFirstPage() {
        currentPage = 1
        API.getTopRatedMovies(BuildConfig.TMDB_API_KEY, LANGUAGE, currentPage).enqueue(object : Callback<Results> {
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
                showErrorDialogAndFinish(context)
            }
        })
    }


    fun loadNextPage() {
        API.getTopRatedMovies(BuildConfig.TMDB_API_KEY, LANGUAGE, currentPage).enqueue(object : Callback<Results> {
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
                showErrorDialogAndFinish(context)
            }
        })
    }

    private fun fetchResults(response: Response<Results>): List<Movie> {
        val topRatedMovies = response.body()
        total_pages = topRatedMovies!!.total_pages
        val movies = ArrayList<Movie>()
        for (movie in topRatedMovies.results) {
            movies.add(Movie(movie.id, movie.title, movie.release_date, movie.overview, movie.posterPath, movie.popularity))
        }

        return movies
    }

    private fun showErrorDialogAndFinish(context: Context){
        val dialog: AlertDialog = AlertDialog.Builder(context)
                .setTitle(context?.resources?.getString(R.string.network_problem))
                .setMessage(context?.resources?.getString(R.string.network_problem))

                .setPositiveButton(context?.resources?.getString(R.string.ok)){dialog, which ->
                    dialog.dismiss()
                    (context as Activity).finish()
                }.create()
        dialog.show()
    }
}
