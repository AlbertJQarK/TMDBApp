package com.privalia.tmdbapp.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.privalia.tmdbapp.BuildConfig
import com.privalia.tmdbapp.model.Movie

import java.util.ArrayList

import com.privalia.tmdbapp.R
import com.privalia.tmdbapp.viewholder.LoadingVH
import com.privalia.tmdbapp.viewholder.MovieVH

/**
 * @author albertj (alberto.guillen.lobo@gmail.com)
 * @since 6-6-18
 */

class PaginationAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val ITEM = 0
    private val LOADING = 1
    private var movieResults: MutableList<Movie>? = null
    private var isLoadingAdded = false
    private var context: Context? = null

    init {
        movieResults = ArrayList()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder? {

        this.context = parent.context
        var viewHolder: RecyclerView.ViewHolder? = null
        val inflater = LayoutInflater.from(parent.context)

        when (viewType) {
            ITEM -> viewHolder = getViewHolder(parent, inflater)
            LOADING -> {
                val v2 = inflater.inflate(R.layout.item_loading, parent, false)
                viewHolder = LoadingVH(v2)
            }
        }

        return viewHolder
    }

    private fun getViewHolder(parent: ViewGroup, inflater: LayoutInflater): RecyclerView.ViewHolder {

        val viewHolder: RecyclerView.ViewHolder
        val v1 = inflater.inflate(R.layout.item_list, parent, false)
        viewHolder = MovieVH(v1)
        return viewHolder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val result: Movie = movieResults!![position]

        when (getItemViewType(position)) {
            ITEM -> {
                val movieVH = holder as MovieVH
                movieVH.mTitle.text = result.title

                if (result.release_date.isBlank()) {
                    movieVH.mYear.text = context?.resources?.getString(R.string.noyear)
                } else {
                    movieVH.mYear.text = result.release_date.substring(0, 4)
                }

                movieVH.mOverview.text = result.overview

                Glide.with(context).load(BuildConfig.TMDB_URL_IMG + result.posterPath)
                        .listener(object : RequestListener<String, GlideDrawable> {
                            override fun onException(e: Exception, model: String, target: Target<GlideDrawable>, isFirstResource: Boolean): Boolean {
                                movieVH.mProgress.visibility = View.GONE
                                return false
                            }

                            override fun onResourceReady(resource: GlideDrawable, model: String, target: Target<GlideDrawable>, isFromMemoryCache: Boolean, isFirstResource: Boolean): Boolean {
                                movieVH.mProgress.visibility = View.GONE
                                return false
                            }
                        })
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerCrop()
                        .crossFade()
                        .into(movieVH.mPoster)
            }

            LOADING -> {
                //Do Nothing
            }
        }
    }

    override fun getItemCount(): Int {
        return if (movieResults == null) 0 else movieResults!!.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == movieResults!!.size - 1 && isLoadingAdded) LOADING else ITEM
    }

    fun removeAll() {
        movieResults!!.clear()
    }

    fun add(r: Movie) {
        movieResults!!.add(r)
        notifyItemInserted(movieResults!!.size - 1)
    }

    fun addAll(moveResults: List<Movie>) {
        for (result in moveResults) {
            add(result)
        }
    }

    fun addLoadingFooter() {
        isLoadingAdded = true
        add(Movie())
    }

    fun removeLoadingFooter() {
        isLoadingAdded = false

        val position = movieResults!!.size - 1
        val result = getItem(position)

        if (result != null) {
            movieResults!!.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun getItem(position: Int): Movie? {
        return movieResults!![position]
    }
}