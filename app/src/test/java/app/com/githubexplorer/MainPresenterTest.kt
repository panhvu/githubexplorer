package app.com.githubexplorer

import app.com.githubexplorer.main.MainInteractor
import app.com.githubexplorer.main.MainPresenter
import app.com.githubexplorer.main.MainView
import app.com.githubexplorer.network.GraphQLManager
import app.com.githubexplorer.network.Repository
import app.com.githubexplorer.network.SchedulerProvider
import com.nhaarman.mockito_kotlin.*
import io.reactivex.schedulers.TestScheduler
import org.junit.Before
import org.junit.Test

/**
 * Created by panhvu on 25.11.18.
 */

class MainPresenterTest {

    private lateinit var testScheduler: TestScheduler
    private lateinit var testSchedulerProvider: SchedulerProvider

    @Before
    fun setup() {
        testScheduler = TestScheduler()
        testSchedulerProvider = mock {
            on { main } doReturn testScheduler
            on { io } doReturn testScheduler
        }
    }

    @Test
    fun testSearchRepositories() {
        val expectedRepository = Repository("", "tkadabench", description = "",
                forkCount = 0, stargazers = 0, watchers = 1)

        val mockView = mock<MainView>()
        val interactor = MainInteractor(GraphQLManager())
        val presenter = MainPresenter(testSchedulerProvider, interactor, mockView)
        presenter.searchRepositories("tkadabench")
        testScheduler.triggerActions()

        verify(mockView, never()).showEmptyMessage()
        verify(mockView, times(1)).showResults(mutableListOf(expectedRepository))
    }

    @Test
    fun testSearchRepositories_emptyResult() {
        val mockView = mock<MainView>()
        val interactor = MainInteractor(GraphQLManager())
        val presenter = MainPresenter(testSchedulerProvider, interactor, mockView)
        presenter.searchRepositories("zzcr5")
        testScheduler.triggerActions()

        verify(mockView, times(1)).showEmptyMessage()
        verify(mockView, never()).showResults(any())
    }

}