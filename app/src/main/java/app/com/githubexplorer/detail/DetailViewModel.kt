package app.com.githubexplorer.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.com.githubexplorer.WatcherQuery
import app.com.githubexplorer.network.GraphQLRepository
import app.com.githubexplorer.network.model.Watcher
import app.com.githubexplorer.utils.OneTimeEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext


internal sealed class DetailUIEvent {
    object Empty : DetailUIEvent()
    class HasMoreData(val flag: Boolean) : DetailUIEvent()
}

class DetailViewModel @Inject constructor(
        private val graphQLRepository: GraphQLRepository
) : ViewModel(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = viewModelScope.coroutineContext

    private val _data = MutableLiveData<List<Watcher>>()
    val data: LiveData<List<Watcher>> = _data
    private val _uiEvent = MutableLiveData<OneTimeEvent<DetailUIEvent>>()
    internal val uiEvents: LiveData<OneTimeEvent<DetailUIEvent>> = _uiEvent

    private lateinit var repoOwner: String
    private lateinit var repoName: String
    private lateinit var endCursor: String
    private var hasNext = false

    fun loadData(owner: String, name: String) {
        this.repoOwner = owner
        this.repoName = name

        launch {
            val watchersResult = graphQLRepository.getWatchersCall(owner, name).repository()?.watchers()
            if (watchersResult?.totalCount() == 0) {
                _uiEvent.value = OneTimeEvent(DetailUIEvent.Empty)
            } else {
                _data.value = watchersResult?.toUIModel()

                // pagination in case there are more results
                hasNext = watchersResult?.pageInfo()!!.hasNextPage()
                endCursor = watchersResult.pageInfo().endCursor() ?: ""
            }
        }
    }

    fun onBottomReached() {
        if (hasNext) {
            _uiEvent.value = OneTimeEvent(DetailUIEvent.HasMoreData(true))
            launch {
                val searchResult = graphQLRepository.loadMoreWatchersCall(repoOwner, repoName, endCursor).repository()?.watchers()
                if (searchResult?.totalCount()!! > 0) {
                    _data.value = searchResult.toUIModel()
                }
            }
        } else {
            _uiEvent.value = OneTimeEvent(DetailUIEvent.HasMoreData(false))
        }
    }

    private fun WatcherQuery.Watchers.toUIModel(): List<Watcher> {
        val watcherList = mutableListOf<Watcher>()
        for (watcher in this.nodes()!!) {
            watcherList.add(parseWatcher(watcher))
        }
        return watcherList.toList()
    }

    private fun parseWatcher(watcher: WatcherQuery.Node): Watcher {
        return Watcher(watcher.avatarUrl().toString(), watcher.name() ?: "")
    }
}