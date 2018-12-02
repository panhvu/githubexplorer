package app.com.githubexplorer.main

import app.com.githubexplorer.RepositoryQuery
import app.com.githubexplorer.network.SchedulerProvider
import app.com.githubexplorer.network.data.Owner
import app.com.githubexplorer.network.data.Repository
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy

/**
 * Created by panhvu on 25.11.18.
 */

class MainPresenter(
        private val scheduler: SchedulerProvider,
        private val interactor: MainInteractor,
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
                interactor.getRepositories(keyword)
                        .subscribeOn(scheduler.io)
                        .observeOn(scheduler.main)
                        .subscribeBy {
                            handleSearchResponse(it.data()?.search()!!)
                        }
        )
    }

    fun loadMore() {
        disposeBag.add(
                interactor.loadMore(keyword, endCursor)
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