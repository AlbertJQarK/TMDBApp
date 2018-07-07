package com.privalia.tmdbapp

import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

import com.privalia.tmdbapp.pagination.PaginationScrollListener
import com.privalia.tmdbapp.presenter.MainView
import com.privalia.tmdbapp.presenter.PresenterMain

/**
 * @author albertj (alberto.guillen.lobo@gmail.com)
 * @since 6-6-18
 */

class MainActivity : AppCompatActivity(), MainView{

    private var rv: RecyclerView? = null
    private var presenterMain: PresenterMain? = null
    private var linearLayoutManager: LinearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initializeView()
        initializePresenter()
        presenterMain!!.loadFirstPage()
    }

    private fun initializeView() {
        rv = findViewById(R.id.main_recycler) as RecyclerView
    }

    private fun initializePresenter() {
        presenterMain = PresenterMain(this,this)
    }

    override fun setRV(adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>) {
        rv!!.getRecycledViewPool().clear()
        adapter.notifyDataSetChanged()
        rv!!.setItemAnimator(DefaultItemAnimator())
        rv!!.setLayoutManager(linearLayoutManager)
        rv!!.setAdapter(adapter)
        rv!!.addOnScrollListener(object : PaginationScrollListener(linearLayoutManager) {

            override val totalPageCount: Int
                get() = presenterMain!!.total_pages

            override val isLastPage: Boolean
                get() = presenterMain!!.isLastPage

            override val isLoading: Boolean
                get() = presenterMain!!.isLoading

            override fun loadMoreItems() {
                presenterMain!!.isLoading = true
                presenterMain!!.currentPage = presenterMain!!.currentPage + 1

                Handler().postDelayed({ presenterMain!!.loadNextPage()}, 1000)
            }
        })
    }
}
