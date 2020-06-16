package app.com.githubexplorer.network

import app.com.githubexplorer.RepositoryQuery
import app.com.githubexplorer.WatcherQuery
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

interface GraphQLRepository {
    suspend fun getRepositoriesCall(keyword: String): RepositoriesResponse
    suspend fun loadMoreRepositoriesCall(keyword: String, endCursor: String): RepositoriesResponse
    suspend fun getWatchersCall(owner: String, name: String): WatchersResponse
    suspend fun loadMoreWatchersCall(owner: String, name: String, endCursor: String): WatchersResponse
}

typealias RepositoriesResponse = RepositoryQuery.Data
typealias WatchersResponse = WatcherQuery.Data

internal class GraphQLRepositoryImpl @Inject constructor(
        private val apolloClient: ApolloClient
): GraphQLRepository {

    private val LIMIT = 20

    override suspend fun getRepositoriesCall(keyword: String): RepositoriesResponse = suspendCoroutine { continuation ->
        val repositoryQuery = RepositoryQuery.builder()
                .keyword(keyword)
                .limit(LIMIT)
                .build()
        apolloClient.query(repositoryQuery)
                .enqueue(object : ApolloCall.Callback<RepositoryQuery.Data>() {
                    override fun onFailure(e: ApolloException) {
                        //TODO error handling
                    }

                    override fun onResponse(response: Response<RepositoryQuery.Data>) {
                        response.data()?.run {
                            continuation.resume(this)
                            return
                        }
                    }
                })
    }

    override suspend fun loadMoreRepositoriesCall(keyword: String, endCursor: String): RepositoriesResponse = suspendCoroutine { continuation ->
        val repositoryQuery = RepositoryQuery.builder()
                .keyword(keyword)
                .limit(LIMIT)
                .endCursorId(endCursor)
                .build()
        apolloClient.query(repositoryQuery)
                .enqueue(object : ApolloCall.Callback<RepositoryQuery.Data>() {
                    override fun onFailure(e: ApolloException) {
                        //TODO error handling
                    }

                    override fun onResponse(response: Response<RepositoryQuery.Data>) {
                        response.data()?.run {
                            continuation.resume(this)
                            return
                        }
                    }
                })
    }

    override suspend fun getWatchersCall(owner: String, name: String): WatchersResponse = suspendCoroutine { continuation ->
        val watchersQuery = WatcherQuery.builder()
                .owner(owner)
                .name(name)
                .limit(LIMIT)
                .build()
        apolloClient.query(watchersQuery)
                .enqueue(object : ApolloCall.Callback<WatcherQuery.Data>() {
                    override fun onFailure(e: ApolloException) {
                        //TODO error handling
                    }

                    override fun onResponse(response: Response<WatcherQuery.Data>) {
                        response.data()?.run {
                            continuation.resume(this)
                            return
                        }
                    }
                })
    }

    override suspend fun loadMoreWatchersCall(owner: String, name: String, endCursor: String): WatchersResponse = suspendCoroutine { continuation ->
        val watchersQuery = WatcherQuery.builder()
                .owner(owner)
                .name(name)
                .limit(LIMIT)
                .endCursor(endCursor)
                .build()
        apolloClient.query(watchersQuery)
                .enqueue(object : ApolloCall.Callback<WatcherQuery.Data>() {
                    override fun onFailure(e: ApolloException) {
                        //TODO error handling
                    }

                    override fun onResponse(response: Response<WatcherQuery.Data>) {
                        response.data()?.run {
                            continuation.resume(this)
                            return
                        }
                    }
                })
    }
}