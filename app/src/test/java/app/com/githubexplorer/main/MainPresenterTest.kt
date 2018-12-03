package app.com.githubexplorer.main

import app.com.githubexplorer.network.GraphQLManager
import app.com.githubexplorer.network.SchedulerProvider
import app.com.githubexplorer.network.data.Owner
import app.com.githubexplorer.network.data.Repository
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
    fun testSearchRepositories_Success() {
        // test if repositories are shown in MainView for valid search query
        val expectedRepository = Repository(Owner("viknesh05", "https://avatars3.githubusercontent.com/u/7587145?v=4"),
                "tkadabench", "", 0, 0, 1)

        val mockView = mock<MainView>()
        val interactor = MainInteractor(GraphQLManager())
        val presenter = MainPresenter(testSchedulerProvider, interactor, mockView)
        presenter.searchRepositories("tkadabench")
        testScheduler.triggerActions()

        verify(mockView, times(1)).showResults(mutableListOf(expectedRepository))
        verify(mockView, never()).showEmptyMessage()
        verify(mockView, times(1)).hasNext(false)
    }

    @Test
    fun testSearchRepositories_Empty() {
        // test if empty message is shown in MainView for invalid search query
        val mockView = mock<MainView>()
        val interactor = MainInteractor(GraphQLManager())
        val presenter = MainPresenter(testSchedulerProvider, interactor, mockView)
        presenter.searchRepositories("tjcgg")
        testScheduler.triggerActions()

        verify(mockView, never()).showResults(any())
        verify(mockView, times(1)).showEmptyMessage()
        verify(mockView, never()).hasNext(any())
    }

    @Test
    fun testLoadMore_Success() {
        // test if more repositories are loaded when available for query
        val mockView = mock<MainView>()
        val interactor = MainInteractor(GraphQLManager())
        val presenter = MainPresenter(testSchedulerProvider, interactor, mockView)
        presenter.searchRepositories("test")
        testScheduler.triggerActions()

        verify(mockView, times(1)).showResults(any())
        verify(mockView, never()).showEmptyMessage()
        verify(mockView, times(1)).hasNext(true)

        presenter.loadMore()
        testScheduler.triggerActions()

        verify(mockView, times(1)).showResults(any())
        verify(mockView, never()).showEmptyMessage()
        verify(mockView, times(1)).hasNext(true)
    }

}