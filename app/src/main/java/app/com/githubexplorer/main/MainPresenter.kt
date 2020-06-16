package app.com.githubexplorer.main

import app.com.githubexplorer.RepositoryQuery
import app.com.githubexplorer.network.GraphQLRepository
import app.com.githubexplorer.network.SchedulerProvider
import app.com.githubexplorer.network.model.Owner
import app.com.githubexplorer.network.model.Repository
import com.apollographql.apollo.rx2.Rx2Apollo
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy

class MainPresenter(
        private val scheduler: SchedulerProvider,
        private val graphQLRepository: GraphQLRepository,
        private val view: MainView
) {

    private val disposeBag = CompositeDisposable()
    private lateinit var reposList: MutableList<Repository>
    private lateinit var endCursor: String
    private lateinit var keyword: String


    fun searchRepositories(keyword: String) {
        reposList = mutableListOf()
        this.keyword = keyword

        disposeBag.add(
                Rx2Apollo.from(graphQLRepository.getRepositoriesCall(keyword))
                        .subscribeOn(scheduler.io)
                        .observeOn(scheduler.main)
                        .subscribeBy {
                            handleSearchResponse(it.data()?.search()!!)
                        }
        )
    }

    fun loadMore() {
        disposeBag.add(
                Rx2Apollo.from(graphQLRepository.loadMoreRepositoriesCall(keyword, endCursor))
                        .subscribeOn(scheduler.io)
                        .observeOn(scheduler.main)
                        .subscribeBy {
                            handleSearchResponse(it.data()?.search()!!)
                        }
        )
    }

    private fun handleSearchResponse(searchResult: RepositoryQuery.Search) {
        if(searchResult.repositoryCount() > 0) {
            // get results and pass to view
            val repos = searchResult.repositories() as MutableList<RepositoryQuery.AsRepository>
            for (repo in repos) {
                reposList.add(parseRepo(repo))
            }
            view.showResults(reposList)

            // pagination in case there are more results
            view.hasNext(searchResult.pageInfo().hasNextPage())
            endCursor = searchResult.pageInfo().endCursor() ?: ""
        }
        else {
            view.showEmptyMessage()
        }
    }

    private fun parseRepo(repo: RepositoryQuery.AsRepository): Repository {
        return Repository(parseOwner(repo), repo.name(),
                repo.description() ?: "", repo.forkCount(),
                repo.stargazers().totalCount(), repo.watchers().totalCount())
    }

    private fun parseOwner(repo: RepositoryQuery.AsRepository) : Owner {
        return Owner(repo.owner().login(), repo.owner().avatarUrl().toString())
    }

    fun unbind() {
        disposeBag.clear()
    }
}