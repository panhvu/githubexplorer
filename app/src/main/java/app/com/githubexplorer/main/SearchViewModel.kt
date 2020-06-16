package app.com.githubexplorer.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.com.githubexplorer.RepositoryQuery
import app.com.githubexplorer.network.GraphQLRepository
import app.com.githubexplorer.network.model.Repository
import app.com.githubexplorer.network.model.toUIModel
import app.com.githubexplorer.utils.OneTimeEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

internal sealed class SearchUIEvent {
    object Empty : SearchUIEvent()
    class HasMoreData(val flag: Boolean) : SearchUIEvent()
}

class SearchViewModel @Inject constructor(
        private val graphQLRepository: GraphQLRepository
) : ViewModel(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = viewModelScope.coroutineContext

    private val _data = MutableLiveData<MutableList<Repository>>()
    val data: LiveData<MutableList<Repository>> = _data
    private val _uiEvent = MutableLiveData<OneTimeEvent<SearchUIEvent>>()
    internal val uiEvents: LiveData<OneTimeEvent<SearchUIEvent>> = _uiEvent
    private lateinit var endCursor: String
    private lateinit var keyword: String
    private var hasNext = false

    fun searchRepositories(keyword: String) {
        this.keyword = keyword

        launch {
            val searchResult = graphQLRepository.getRepositoriesCall(keyword).search()
            handleSearchResponse(searchResult)
        }
    }

    fun onBottomReached() {
        if (hasNext) {
            _uiEvent.value = OneTimeEvent(SearchUIEvent.HasMoreData(true))
            launch {
                val searchResult = graphQLRepository.loadMoreRepositoriesCall(keyword, endCursor).search()
                handleSearchResponse(searchResult)
            }
        } else {
            _uiEvent.value = OneTimeEvent(SearchUIEvent.HasMoreData(false))
        }
    }

    private fun handleSearchResponse(searchResult: RepositoryQuery.Search){
        if (searchResult.repositoryCount() == 0) {
            _uiEvent.value = OneTimeEvent(SearchUIEvent.Empty)
        } else {
            val repos = _data.value ?: mutableListOf()
            repos.addAll(searchResult.toUIModel())
            _data.value = repos

            // pagination in case there are more results
            hasNext = searchResult.pageInfo().hasNextPage()
            endCursor = searchResult.pageInfo().endCursor() ?: ""
        }
    }

}