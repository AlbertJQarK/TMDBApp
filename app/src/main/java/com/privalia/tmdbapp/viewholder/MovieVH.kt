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
    var title: TextView
    var overview: TextView
    var year: TextView
    var poster: ImageView
    var progress: ProgressBar

    init {

        title = itemView.findViewById(R.id.title) as TextView
        overview = itemView.findViewById(R.id.overview) as TextView
        year = itemView.findViewById(R.id.year) as TextView
        poster = itemView.findViewById(R.id.poster) as ImageView
        progress = itemView.findViewById(R.id.image_load) as ProgressBar
    }
}