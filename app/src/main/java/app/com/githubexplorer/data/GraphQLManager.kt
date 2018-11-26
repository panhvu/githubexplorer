package app.com.githubexplorer.data

import android.net.Uri
import app.com.githubexplorer.RepositoryQuery
import app.com.githubexplorer.type.CustomType
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.fetcher.ApolloResponseFetchers
import com.apollographql.apollo.response.CustomTypeAdapter
import com.apollographql.apollo.response.CustomTypeValue
import okhttp3.OkHttpClient
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by panhvu on 25.11.18.
 */

open class GraphQLManager {

    private val BASE_URL = "https://api.github.com/graphql"


    private val okHttpClient = OkHttpClient.Builder()
            .addInterceptor{ chain ->
                chain.proceed(chain.request().newBuilder()
                        .addHeader("Authorization",
                                "bearer ede413efeee2b5081eb8b62b710ed21e6c4016bf")
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
    private val GRAPHQL_DATE_FORMAT = SimpleDateFormat("yyyy-mm-dd", Locale.US)
    private val dateCustomTypeAdapter = object : CustomTypeAdapter<Date> {
        override fun decode(value: CustomTypeValue<*>): Date {
            return GRAPHQL_DATE_FORMAT.parse(value.value.toString())
        }
        override fun encode(value: Date): CustomTypeValue<*> {
            return CustomTypeValue.GraphQLString(GRAPHQL_DATE_FORMAT.format(value.toString()))
        }

    }

    private val apolloClient = ApolloClient.builder()
            .serverUrl(BASE_URL)
            .okHttpClient(okHttpClient)
            .addCustomTypeAdapter(CustomType.URI, uriCustomTypeAdapter)
            .addCustomTypeAdapter(CustomType.DATETIME, dateCustomTypeAdapter)
            .build()

    fun getRepositoriesCall(keyword: String): ApolloCall<RepositoryQuery.Data> {
        val repositoriesCall: ApolloCall<RepositoryQuery.Data>
        val repositoryQuery = RepositoryQuery.builder()
                .keyword(keyword)
                .build()
        repositoriesCall = apolloClient.query(repositoryQuery)
                .responseFetcher(ApolloResponseFetchers.NETWORK_FIRST)

        return repositoriesCall
    }

}