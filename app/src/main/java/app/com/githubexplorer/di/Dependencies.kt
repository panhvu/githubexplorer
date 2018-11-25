package app.com.githubexplorer.di

import app.com.githubexplorer.data.GraphQLManager
import app.com.githubexplorer.main.MainInteractor

/**
 * Created by panhvu on 25.11.18.
 */

open class Dependencies {

    //val apolloClient = GraphQLManager().apolloClient

    val graphQlManager = GraphQLManager()
    val mainInteractor = MainInteractor(graphQlManager)
}