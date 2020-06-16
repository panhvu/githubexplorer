package app.com.githubexplorer.main

import app.com.githubexplorer.network.model.Repository

interface MainView {

    fun showResults(results: MutableList<Repository>)
    fun showEmptyMessage()
    fun hasNext(flag: Boolean)
}