package app.com.githubexplorer.di

import android.content.Context
import android.net.Uri
import app.com.githubexplorer.App
import app.com.githubexplorer.network.GraphQLRepository
import app.com.githubexplorer.network.GraphQLRepositoryImpl
import app.com.githubexplorer.type.CustomType
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.response.CustomTypeAdapter
import com.apollographql.apollo.response.CustomTypeValue
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
class AppModule(
    @get:Provides
    @get:Singleton
    val app: App
) {

    val applicationContext: Context
        @Provides
        @Singleton
        get() = app

    @Provides
    fun provideBaseUrl(): String = "https://api.github.com/graphql"

    @Singleton
    @Provides
    fun provideOkHTTPClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor{ chain ->
                chain.proceed(chain.request().newBuilder()
                        .addHeader("Authorization",
                                "bearer b3b402651278f69ecd8c3baecc218df83c816545")
                        .build() )}
            .build()
    }

    @Provides
    fun provideUriCustomTypeAdapter()= object : CustomTypeAdapter<Uri> {
        override fun decode(value: CustomTypeValue<*>): Uri {
            return Uri.parse(value.value.toString())
        }

        override fun encode(value: Uri): CustomTypeValue<*> {
            return CustomTypeValue.GraphQLString(value.toString())
        }

    }

    @Singleton
    @Provides
    fun provideApolloClient(
            baseUrl: String,
            okHttpClient: OkHttpClient,
            customTypeAdapter: CustomTypeAdapter<Uri>
    ): ApolloClient {
        return ApolloClient.builder()
            .serverUrl(baseUrl)
            .okHttpClient(okHttpClient)
            .addCustomTypeAdapter(CustomType.URI, customTypeAdapter)
            .build()
    }

    @Provides
    fun graphQLRepository(
            apolloClient: ApolloClient
    ): GraphQLRepository = GraphQLRepositoryImpl(apolloClient)
}