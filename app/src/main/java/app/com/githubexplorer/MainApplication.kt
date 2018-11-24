package app.com.githubexplorer

import android.app.Application
import com.apollographql.apollo.ApolloClient
import okhttp3.OkHttpClient


/**
 * Created by panhvu on 24.11.18.
 */

class MainApplication: Application() {

    private val BASE_URL = "https://api.github.com/graphql"
    lateinit var apolloClient: ApolloClient

    override fun onCreate() {
        super.onCreate()
        val okHttpClient = OkHttpClient.Builder()
                .build()


        apolloClient = ApolloClient.builder()
                .serverUrl(BASE_URL)
                .okHttpClient(okHttpClient)
                .build()
    }

    fun apolloClient(): ApolloClient? {
        return apolloClient
    }

}