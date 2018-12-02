package app.com.githubexplorer.main

import app.com.githubexplorer.RepositoryQuery
import app.com.githubexplorer.network.SchedulerProvider
import app.com.githubexplorer.network.data.Repository
import app.com.githubexplorer.network.data.Watcher
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


    fun searchRepositories(keyword: String) {
        reposList = mutableListOf()

        disposeBag.add(
                interactor.getRepositories(keyword)
                        .subscribeOn(scheduler.io)
                        .observeOn(scheduler.main)
                        .subscribeBy {
                            if(it.data()?.search()?.repositoryCount()!! > 0) {
                                // get results and pass to view
                                val repos = it.data()?.search()?.repositories() as MutableList<RepositoryQuery.AsRepository>
                                for (repo in repos) {
                                    reposList.add(parseRepo(repo))
                                }
                                view.showResults(reposList)

                                // pagination in case there are more results
                                view.hasNext(it.data()?.search()?.pageInfo()?.hasNextPage() ?: false)
                                endCursor = it.data()?.search()?.pageInfo()?.endCursor() ?: ""

                            }
                            else {
                                view.showEmptyMessage()
                            }
                        }


        )
    }

    fun loadMore() {

    }

    private fun parseRepo(repo: RepositoryQuery.AsRepository): Repository {
        return Repository(repo.owner().avatarUrl().toString(), repo.name(),
                repo.description() ?: "", repo.forkCount(),
                repo.stargazers().totalCount(), repo.watchers().totalCount(),
                parseWatchers(repo))
    }

    private fun parseWatchers(repo: RepositoryQuery.AsRepository): MutableList<Watcher> {
        val watchers = mutableListOf<Watcher>()
        var w: Watcher
        for (watcher in repo.watchers().watchers()!!){
            w = Watcher(watcher.avatarUrl().toString(), watcher.name() ?: "")
            watchers.add(w)
        }
        return watchers
    }

    fun unbind() {
        disposeBag.clear()
    }
}