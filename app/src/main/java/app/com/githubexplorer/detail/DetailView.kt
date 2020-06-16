package app.com.githubexplorer.detail

import app.com.githubexplorer.network.model.Watcher

interface DetailView {

    fun showResults(results: MutableList<Watcher>)
    fun hasNext(flag: Boolean)
}