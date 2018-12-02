package app.com.githubexplorer.detail

import app.com.githubexplorer.WatcherQuery
import app.com.githubexplorer.network.GraphQLManager
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.rx2.Rx2Apollo
import io.reactivex.Observable

/**
 * Created by panhvu on 25.11.18.
 */


class DetailInteractor (
        private val graphQLManager: GraphQLManager
) {

    fun getWatchers(owner: String, name: String): Observable<Response<WatcherQuery.Data>> {
        return Rx2Apollo.from(graphQLManager.getWatchersCall(owner, name))
    }

}