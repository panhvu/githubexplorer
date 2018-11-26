package app.com.githubexplorer.network

import app.com.githubexplorer.main.MainInteractor

/**
 * Created by panhvu on 25.11.18.
 */

open class Dependencies {

    val graphQlManager = GraphQLManager()
    val mainInteractor = MainInteractor(graphQlManager)
}