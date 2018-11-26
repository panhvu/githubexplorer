package app.com.githubexplorer

import android.net.Uri
import app.com.githubexplorer.main.MainInteractor
import app.com.githubexplorer.main.MainPresenter
import app.com.githubexplorer.main.MainView
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Response
import com.nhaarman.mockito_kotlin.*
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.mockwebserver.MockWebServer
import org.junit.Before
import org.junit.Test
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by panhvu on 25.11.18.
 */

class MainPresenterTest {

    val mockServer = MockWebServer()
    private lateinit var apolloClient: ApolloClient

    @Before
    fun setup() {
        val okHttpClient = OkHttpClient.Builder()
                .connectTimeout(2, TimeUnit.SECONDS)
                .readTimeout(2, TimeUnit.SECONDS)
                .build()
        apolloClient = ApolloClient.builder()
                .serverUrl(mockServer.url("/"))
                .okHttpClient(okHttpClient)
                .build()
    }

    @Test
    fun testSearchRepositories() {

        //given response of interactor
        //list of expected repositories is returned
        //view configured

        val mockRepo = RepositoryQuery.AsRepository("repo", "testName", "testDescription",
                1, RepositoryQuery.Owner("owner", Uri.parse("testUrl")),
                RepositoryQuery.Stargazers("stargazers", 1), RepositoryQuery.Watchers("watchers", 1),
                Date(), Date())
        val mockSearch = RepositoryQuery.Search("search", 1, listOf(mockRepo))
        val mockData = RepositoryQuery.Data(mockSearch)

        val mockView = mock<MainView>()
        val mockInteractor = mock<MainInteractor> {
            on { getRepositories(any()) } doReturn { Observable.just(mockData) }
        }

        val presenter = MainPresenter(mockView, mockInteractor)
        presenter.searchRepositories(any())
        verify(mockView, times(1)).showResults(any())
    }

}