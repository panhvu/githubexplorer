package app.com.githubexplorer.detail

import app.com.githubexplorer.network.GraphQLRepository
import app.com.githubexplorer.network.SchedulerProvider
import app.com.githubexplorer.network.model.Watcher
import com.nhaarman.mockito_kotlin.*
import io.reactivex.schedulers.TestScheduler
import org.junit.Before
import org.junit.Test

/**
 * Created by panhvu on 25.11.18.
 */

class DetailPresenterTest {

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
    fun testGetWatchers_Success() {
        // test if watchers are shown in DetailView for given repo
        val mockView = mock<DetailView>()
        val presenter = DetailPresenter(testSchedulerProvider, GraphQLRepository(), mockView)
        presenter.getWatchers("barkinet", "Barkinet")
        testScheduler.triggerActions()

        verify(mockView, times(1))
                .showResults(mutableListOf(Watcher("https://avatars2.githubusercontent.com/u/2127162?v=4", "Mustapha Ben Daoud Barki")))
        verify(mockView, times(1)).hasNext(false)
    }

    @Test
    fun testGetWatchers_Empty() {
        // test when zero watchers found for repo
        val mockView = mock<DetailView>()
        val presenter = DetailPresenter(testSchedulerProvider, GraphQLRepository(), mockView)
        presenter.getWatchers("jeanloic179", "hamien")
        testScheduler.triggerActions()

        verify(mockView, never()).showResults(any())
        verify(mockView, times(1)).hasNext(false)
    }

    @Test
    fun testLoadMore_Success() {
        // test if more watchers are loaded when available for repo
        val mockView = mock<DetailView>()
        val presenter = DetailPresenter(testSchedulerProvider, GraphQLRepository(), mockView)
        presenter.getWatchers("pytest-dev", "pytest")
        testScheduler.triggerActions()

        verify(mockView, times(1)).showResults(any())
        verify(mockView, times(1)).hasNext(true)

        presenter.loadMore()
        testScheduler.triggerActions()

        verify(mockView, times(1)).showResults(any())
        verify(mockView, times(1)).hasNext(true)
    }

}