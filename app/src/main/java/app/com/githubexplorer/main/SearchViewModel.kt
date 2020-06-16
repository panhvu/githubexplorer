package app.com.githubexplorer.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.com.githubexplorer.RepositoryQuery
import app.com.githubexplorer.network.GraphQLRepository
import app.com.githubexplorer.network.model.Owner
import app.com.githubexplorer.network.model.Repository
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

    private val _data = MutableLiveData<List<Repository>>()
    val data: LiveData<List<Repository>> = _data
    private val _uiEvent = MutableLiveData<OneTimeEvent<SearchUIEvent>>()
    internal val uiEvents: LiveData<OneTimeEvent<SearchUIEvent>> = _uiEvent
    private lateinit var endCursor: String
    private lateinit var keyword: String
    private var hasNext = false

    fun searchRepositories(keyword: String) {
        this.keyword = keyword

        launch {
            val searchResult = graphQLRepository.getRepositoriesCall(keyword).search()
            if (searchResult.repositoryCount() == 0) {
                _uiEvent.value = OneTimeEvent(SearchUIEvent.Empty)
            } else {
                _data.value = searchResult.toUIModel()

                // pagination in case there are more results
                hasNext = searchResult.pageInfo().hasNextPage()
                endCursor = searchResult.pageInfo().endCursor() ?: ""
            }
        }
    }

    fun onBottomReached() {
        if (hasNext) {
            _uiEvent.value = OneTimeEvent(SearchUIEvent.HasMoreData(true))
            launch {
                val searchResult = graphQLRepository.loadMoreRepositoriesCall(keyword, endCursor).search()
                if (searchResult.repositoryCount() > 0) {
                    _data.value = searchResult.toUIModel()
                }
            }
        } else {
            _uiEvent.value = OneTimeEvent(SearchUIEvent.HasMoreData(false))
        }
    }

    private fun RepositoryQuery.Search.toUIModel(): List<Repository> {
        val reposList = mutableListOf<Repository>()
        // get results and pass to view
        val repos = this.repositories() as MutableList<RepositoryQuery.AsRepository>
        for (repo in repos) {
            reposList.add(parseRepo(repo))
        }
        return reposList.toList()
    }

    private fun parseRepo(repo: RepositoryQuery.AsRepository): Repository {
        return Repository(parseOwner(repo), repo.name(),
                repo.description() ?: "", repo.forkCount(),
                repo.stargazers().totalCount(), repo.watchers().totalCount())
    }

    private fun parseOwner(repo: RepositoryQuery.AsRepository): Owner {
        return Owner(repo.owner().login(), repo.owner().avatarUrl().toString())
    }

}