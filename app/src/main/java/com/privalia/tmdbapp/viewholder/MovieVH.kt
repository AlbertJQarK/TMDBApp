package com.privalia.tmdbapp.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView

import com.privalia.tmdbapp.R

/**
 * @author albertj (alberto.guillen.lobo@gmail.com)
 * @since 6-6-18
 */

class MovieVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var mTitle: TextView
    var mOverview: TextView
    var mYear: TextView
    var mPoster: ImageView
    var mProgress: ProgressBar

    init {

        mTitle = itemView.findViewById(R.id.title) as TextView
        mOverview = itemView.findViewById(R.id.overview) as TextView
        mYear = itemView.findViewById(R.id.year) as TextView
        mPoster = itemView.findViewById(R.id.poster) as ImageView
        mProgress = itemView.findViewById(R.id.image_load) as ProgressBar
    }
}