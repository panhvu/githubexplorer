package app.com.githubexplorer

import app.com.githubexplorer.main.MainInteractor
import app.com.githubexplorer.main.MainPresenter
import app.com.githubexplorer.main.MainView
import com.apollographql.apollo.api.Response
import com.nhaarman.mockito_kotlin.*
import io.reactivex.Observable
import org.junit.Test

/**
 * Created by panhvu on 25.11.18.
 */

class MainPresenterTest {

    @Test
    fun testSearchRepositories() {
        val mockView = mock<MainView>()

        val testResponse = mock<Response<RepositoryQuery.Data>>()
        val testResults = testResponse.data()?.search()?.repositories as MutableList<RepositoryQuery.AsRepository>

        val mockInteractor = mock<MainInteractor> {
            on { getRepositories(any()) } doReturn Observable.just(testResponse)
        }

        val presenter = MainPresenter(mockView, mockInteractor)
        presenter.searchRepositories(any())
        verify(mockView, times(1)).showResults(testResults)

    }

}