package app.com.githubexplorer.network

import android.net.Uri
import app.com.githubexplorer.RepositoryQuery
import app.com.githubexplorer.type.CustomType
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.fetcher.ApolloResponseFetchers
import com.apollographql.apollo.response.CustomTypeAdapter
import com.apollographql.apollo.response.CustomTypeValue
import okhttp3.OkHttpClient

/**
 * Created by panhvu on 25.11.18.
 */

open class GraphQLManager {

    private val BASE_URL = "https://api.github.com/graphql"
    private val LIMIT = 20

    private val okHttpClient = OkHttpClient.Builder()
            .addInterceptor{ chain ->
                chain.proceed(chain.request().newBuilder()
                        .addHeader("Authorization",
                                "bearer 30aecf77eb9ee843d257b254790433d60a4e8739")
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
}