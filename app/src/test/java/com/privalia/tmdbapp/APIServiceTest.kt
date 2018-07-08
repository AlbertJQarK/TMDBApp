package com.privalia.tmdbapp

import com.privalia.tmdbapp.api.APIService
import com.privalia.tmdbapp.model.Movie
import com.privalia.tmdbapp.model.Results
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @author albertj (alberto.guillen.lobo@gmail.com)
 * @since 6-6-18
 */

class ServiceTest {
    lateinit var service: APIService
    companion object {
        private const val LANGUAGE = "en_US"
    }
    var result: Boolean? = null

    @Before
    internal fun setUp() {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

        val retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.TMDB_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()

        service = retrofit.create(APIService::class.java)
    }

    @Test
    internal fun getTopRatedMovies() {
        var result = false
        service.getTopRatedMovies(BuildConfig.TMDB_API_KEY,LANGUAGE,1).enqueue(object : Callback<Results> {
            override fun onResponse(call: Call<Results>, response: Response<Results>) {

                var popularity = 0
                result = true
                val topRatedMovies = response.body()
                for (movie in topRatedMovies!!.results) {
                    result = result && popularity < movie.popularity.toInt()
                    popularity = movie.popularity.toInt()
                }
            }

            override fun onFailure(call: Call<Results>, t: Throwable) {
                t.printStackTrace()
                result = false
            }
        })

        Assert.assertEquals("Fail gettting top rated movies.",true, result)
    }

    @Test
    internal fun SearchMovies() {
        var result: Boolean? = null
        service.doGetMovieList(BuildConfig.TMDB_API_KEY,"matrix", 1).enqueue(object : Callback<Results> {
            override fun onResponse(call: Call<Results>, response: Response<Results>) {

                result = true
                val titles = arrayOf("The Matrix", "The Matrix Reloaded",
                        "The Matrix Revolutions","The Matrix Revisited", "The Matrix Recalibrated", "Sexual Matrix",
                        "Armitage III: Poly Matrix", "Armitage: Dual Matrix", "Cyber Wars", "The Matrix: ASCII",
                        "Matrix", "Darkdrive", "The American Matrix - Age Of Deception",
                        "Return to Source: The Philosophy of The Matrix", "The Matrix Revolutions Revisited",
                        "Matrix of Evil", "Die Luther Matrix", "The Living Matrix", "Matrix III", "Chess Boxing Matrix")

                val search = response.body()
                var movies = ArrayList<Movie>()
                search!!.results.forEachIndexed { index, movie ->
                    movies.add(Movie(movie.id, movie.title, movie.release_date, movie.overview, movie.posterPath, movie.popularity))
                    if(movie.title != titles[index])
                        result = false
                }
            }

            override fun onFailure(call: Call<Results>, t: Throwable) {
                t.printStackTrace()
                result = false
            }
        })

        Assert.assertEquals("Fail gettting top rated movies.",true, result)
    }


}