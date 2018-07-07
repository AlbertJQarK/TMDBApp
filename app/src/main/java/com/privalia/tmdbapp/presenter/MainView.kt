package com.privalia.tmdbapp.presenter

import android.support.v7.widget.RecyclerView

/**
 * @author albertj (alberto.guillen.lobo@gmail.com)
 * @since 6-6-18
 */

interface MainView {
    /**
     * Set RecyclerView for result search
     * @param adapter
     */
    fun setRV(adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>)
}
