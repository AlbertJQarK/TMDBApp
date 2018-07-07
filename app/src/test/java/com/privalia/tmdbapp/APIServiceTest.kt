package com.privalia.tmdbapp

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import org.junit.Assert
import org.junit.Before
import org.junit.Test

import com.privalia.tmdbapp.api.APIService
import com.privalia.tmdbapp.model.Results
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * @author albertj (alberto.guillen.lobo@gmail.com)
 * @since 6-6-18
 */

class ServiceTest {
    lateinit var service: APIService
    companion object {
        private const val LANGUAGE = "en_US"
    }

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
        service.getTopRatedMovies(BuildConfig.TMDB_API_KEY,LANGUAGE,1).enqueue(object : Callback<Results> {
            override fun onResponse(call: Call<Results>, response: Response<Results>) {

                var popularity = 0
                var result = false;
                val topRatedMovies = response.body()
                for (movie in topRatedMovies!!.results) {
                    result = result && popularity < movie.popularity.toInt()
                    popularity = movie.popularity.toInt()
                }
                Assert.assertTrue("Checking movies popularity order", result)

            }

            override fun onFailure(call: Call<Results>, t: Throwable) {
                t.printStackTrace()
                Assert.assertTrue("Checking movies popularity order", false)
            }
        })
    }
}