package app.com.githubexplorer.detail

import app.com.githubexplorer.network.data.Watcher

/**
 * Created by panhvu on 25.11.18.
 */

interface DetailView {

    fun showResults(results: MutableList<Watcher>)
    fun hasNext(flag: Boolean)
}