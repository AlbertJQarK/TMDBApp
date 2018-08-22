package com.albertgl.tmdbapp.view

import android.support.v7.widget.RecyclerView

/**
 * @author albertj (alberto.guillen.lobo@gmail.com)
 * @since 6-6-18
 */

interface MainView {

    fun setRV(adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>)
}
