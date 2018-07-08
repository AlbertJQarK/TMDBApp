package com.privalia.tmdbapp.presenter

import android.app.Activity
import android.content.Context
import android.support.v7.app.AlertDialog
import com.privalia.tmdbapp.BuildConfig
import com.privalia.tmdbapp.R
import com.privalia.tmdbapp.api.APIService
import com.privalia.tmdbapp.model.Movie
import com.privalia.tmdbapp.model.Results
import com.privalia.tmdbapp.view.MainView
import com.privalia.tmdbapp.view.pagination.PaginationAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

    fun loadFirstPage(query: String){
        currentPage = 1
        isLastPage = false
        if (query.isEmpty()) {
            API.getTopRatedMovies(BuildConfig.TMDB_API_KEY, LANGUAGE, currentPage).enqueue(object : Callback<Results> {
                override fun onResponse(call: Call<Results>, response: Response<Results>) {

                    val movies = fetchResults(response)
                    if(!pagAdapter.isEmpty())
                        pagAdapter.removeLoadingFooter()
                    pagAdapter.removeAll()
                    pagAdapter.addAll(movies)
                    mainView.setRV(pagAdapter)

                    if (currentPage < total_pages) {
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
        }else{
            API.doGetMovieList(BuildConfig.TMDB_API_KEY, query, currentPage).enqueue(object : Callback<Results> {
                override fun onResponse(call: Call<Results>, response: Response<Results>) {

                    val movies = fetchResults(response)
                    if(!pagAdapter.isEmpty())
                        pagAdapter.removeLoadingFooter()
                    pagAdapter.removeAll()
                    pagAdapter.addAll(movies)
                    mainView.setRV(pagAdapter)

                    if (currentPage < total_pages) {
                        if (!pagAdapter.isEmpty())
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
    }


    fun loadNextPage(query: String){
        if (query.isEmpty()) {
            API.getTopRatedMovies(BuildConfig.TMDB_API_KEY, LANGUAGE, currentPage).enqueue(object : Callback<Results> {
                override fun onResponse(call: Call<Results>, response: Response<Results>) {

                    isLoading = false
                    val movies = fetchResults(response)
                    pagAdapter.removeLoadingFooter()
                    pagAdapter.addAll(movies)

                    if (currentPage < total_pages)
                        pagAdapter.addLoadingFooter()
                    else
                        isLastPage = true
                }

                override fun onFailure(call: Call<Results>, t: Throwable) {
                    t.printStackTrace()
                    showErrorDialogAndFinish(context)
                }
            })
        }else{
            API.doGetMovieList(BuildConfig.TMDB_API_KEY, query, currentPage).enqueue(object : Callback<Results> {
                override fun onResponse(call: Call<Results>, response: Response<Results>) {

                    isLoading = false
                    val movies = fetchResults(response)
                    pagAdapter.removeLoadingFooter()
                    pagAdapter.addAll(movies)

                    if (currentPage < total_pages)
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
    }

    private fun fetchResults(response: Response<Results>): List<Movie> {
        val data = response.body()
        total_pages = data!!.total_pages
        var movies = ArrayList<Movie>()
        var title = ""
        var posterPath = ""
        var release_date = ""
        var overview = ""
        var popularity = ""
        for (movie in data.results) {
            if(movie.title != null)
                title = movie.title
            if(movie.release_date != null)
                release_date = movie.release_date
            if(movie.overview != null)
                overview = movie.overview
            if(movie.posterPath != null)
                posterPath = movie.posterPath
            if(movie.popularity != null)
                popularity = movie.popularity
            movies.add(Movie(movie.id, title, release_date, overview, posterPath, popularity))
        }

        return movies
    }

    private fun showErrorDialogAndFinish(context: Context){
        val dialog: AlertDialog = AlertDialog.Builder(context)
                .setTitle(context?.resources?.getString(R.string.network_problem))
                .setMessage(context?.resources?.getString(R.string.network_problem_message))

                .setPositiveButton(context?.resources?.getString(R.string.ok)){ dialog, _ ->
                    dialog.dismiss()
                    (context as Activity).finish()
                }.create()
        dialog.show()
    }
}
