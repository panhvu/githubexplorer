package app.com.githubexplorer.main

import app.com.githubexplorer.RepositoryQuery

/**
 * Created by panhvu on 25.11.18.
 */

interface MainView {

    fun showResults(results: MutableList<RepositoryQuery.AsRepository>)

}