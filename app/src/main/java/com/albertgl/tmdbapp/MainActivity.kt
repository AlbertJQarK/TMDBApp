package com.albertgl.tmdbapp

import android.os.Bundle
import android.os.Handler

import android.support.v4.view.MenuItemCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.*

import android.view.Menu

import com.albertgl.tmdbapp.view.pagination.PaginationScrollListener
import com.albertgl.tmdbapp.view.MainView
import com.albertgl.tmdbapp.presenter.PresenterMain

/**
 * @author albertj (alberto.guillen.lobo@gmail.com)
 * @since 6-6-18
 */

class MainActivity : AppCompatActivity(), MainView, SearchView.OnQueryTextListener{

    private var rv: RecyclerView? = null
    private var presenterMain: PresenterMain? = null
    private var linearLayoutManager: LinearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    private var query = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        initializeView()
        initializePresenter()
        presenterMain!!.loadFirstPage("")
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

                Handler().postDelayed({ presenterMain!!.loadNextPage(query)}, 1000)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        val menuItem = menu.findItem(R.id.action_search)
        val searchView = MenuItemCompat.getActionView(menuItem) as android.support.v7.widget.SearchView
        searchView.setOnQueryTextListener(this)
        return true
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String): Boolean {
        query = newText
        presenterMain!!.loadFirstPage(query)

        return true
    }

}
