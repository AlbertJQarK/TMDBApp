package com.albertgl.tmdbapp.api

import com.albertgl.tmdbapp.BuildConfig
import com.albertgl.tmdbapp.model.Results

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @author albertj (alberto.guillen.lobo@gmail.com)
 * @since 6-6-18
 */

interface APIService {

    @GET("3/movie/top_rated")
    fun getTopRatedMovies(@Query("api_key") apiKey: String, @Query("language") language: String, @Query("page") page: Int): Call<Results>

    @GET("3/search/movie")
    fun doGetMovieList(@Query("api_key") apiKey: String, @Query("query") query: String, @Query("page") page: Int): Call<Results>

    companion object {
        fun create(): APIService {

            val interceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

            val retrofit = Retrofit.Builder()
                    .baseUrl(BuildConfig.TMDB_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build()

            return retrofit.create(APIService::class.java)
        }
    }
}