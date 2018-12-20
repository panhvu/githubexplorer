package app.com.githubexplorer.main

import app.com.githubexplorer.network.data.Repository

interface MainView {

    fun showResults(results: MutableList<Repository>)
    fun showEmptyMessage()
    fun hasNext(flag: Boolean)
}