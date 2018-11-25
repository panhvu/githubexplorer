package app.com.githubexplorer.main

import app.com.githubexplorer.RepositoryQuery
import app.com.githubexplorer.data.GraphQLManager
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.rx2.Rx2Apollo
import io.reactivex.Observable

/**
 * Created by panhvu on 25.11.18.
 */

class MainInteractor(
        private val graphQLManager: GraphQLManager
) {

    fun getRepositories(keyword: String): Observable<Response<RepositoryQuery.Data>> {
        return Rx2Apollo.from(graphQLManager.getRepositoriesCall(keyword))
    }

}