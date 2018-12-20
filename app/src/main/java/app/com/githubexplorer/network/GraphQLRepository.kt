package app.com.githubexplorer.network

import android.net.Uri
import app.com.githubexplorer.RepositoryQuery
import app.com.githubexplorer.WatcherQuery
import app.com.githubexplorer.type.CustomType
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.fetcher.ApolloResponseFetchers
import com.apollographql.apollo.response.CustomTypeAdapter
import com.apollographql.apollo.response.CustomTypeValue
import okhttp3.OkHttpClient

open class GraphQLRepository {

    private val BASE_URL = "https://api.github.com/graphql"
    private val LIMIT = 20

    private val okHttpClient = OkHttpClient.Builder()
            .addInterceptor{ chain ->
                chain.proceed(chain.request().newBuilder()
                        .addHeader("Authorization",
                                "bearer 73badfc2042466552fe8d709d979f1041fca74cb")
                        .build() )}
            .build()

    private val uriCustomTypeAdapter = object : CustomTypeAdapter<Uri> {
        override fun decode(value: CustomTypeValue<*>): Uri {
            return Uri.parse(value.value.toString())
        }

        override fun encode(value: Uri): CustomTypeValue<*> {
            return CustomTypeValue.GraphQLString(value.toString())
        }

    }

    private val apolloClient = ApolloClient.builder()
            .serverUrl(BASE_URL)
            .okHttpClient(okHttpClient)
            .addCustomTypeAdapter(CustomType.URI, uriCustomTypeAdapter)
            .build()

    fun getRepositoriesCall(keyword: String): ApolloCall<RepositoryQuery.Data> {
        val repositoriesCall: ApolloCall<RepositoryQuery.Data>
        val repositoryQuery = RepositoryQuery.builder()
                .keyword(keyword)
                .limit(LIMIT)
                .build()
        repositoriesCall = apolloClient.query(repositoryQuery)
                .responseFetcher(ApolloResponseFetchers.NETWORK_FIRST)

        return repositoriesCall
    }

    fun loadMoreRepositoriesCall(keyword: String, endCursor: String): ApolloCall<RepositoryQuery.Data> {
        val repositoriesCall: ApolloCall<RepositoryQuery.Data>
        val repositoryQuery = RepositoryQuery.builder()
                .keyword(keyword)
                .limit(LIMIT)
                .endCursorId(endCursor)
                .build()
        repositoriesCall = apolloClient.query(repositoryQuery)
                .responseFetcher(ApolloResponseFetchers.NETWORK_FIRST)

        return repositoriesCall
    }

    fun getWatchersCall(owner: String, name: String): ApolloCall<WatcherQuery.Data> {
        val watchersCall: ApolloCall<WatcherQuery.Data>
        val watchersQuery = WatcherQuery.builder()
                .owner(owner)
                .name(name)
                .limit(LIMIT)
                .build()
        watchersCall = apolloClient.query(watchersQuery)
                .responseFetcher(ApolloResponseFetchers.NETWORK_FIRST)

        return watchersCall
    }

    fun loadMoreWatchersCall(owner: String, name: String, endCursor: String): ApolloCall<WatcherQuery.Data> {
        val watchersCall: ApolloCall<WatcherQuery.Data>
        val watchersQuery = WatcherQuery.builder()
                .owner(owner)
                .name(name)
                .limit(LIMIT)
                .endCursor(endCursor)
                .build()
        watchersCall = apolloClient.query(watchersQuery)
                .responseFetcher(ApolloResponseFetchers.NETWORK_FIRST)

        return watchersCall
    }
}