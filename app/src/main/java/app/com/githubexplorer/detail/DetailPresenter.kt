package app.com.githubexplorer.detail

import app.com.githubexplorer.WatcherQuery
import app.com.githubexplorer.network.SchedulerProvider
import app.com.githubexplorer.network.data.Watcher
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy

/**
 * Created by panhvu on 02.12.18.
 */
class DetailPresenter(
        private val scheduler: SchedulerProvider,
        private val interactor: DetailInteractor,
        private val view: DetailView
) {

    private val disposeBag = CompositeDisposable()
    private lateinit var watchersList: MutableList<Watcher>
    private lateinit var endCursor: String

    fun getWatchers(owner: String, name: String) {
        watchersList = mutableListOf()

        disposeBag.add(
                interactor.getWatchers(owner, name)
                        .subscribeOn(scheduler.io)
                        .observeOn(scheduler.main)
                        .subscribeBy {
                            val watchers = it.data()?.repository()?.watchers()
                            if(watchers?.totalCount()!! > 0) {
                                // get results and pass to view
                                for (watcher in watchers.nodes()!!) {
                                    watchersList.add(parseWatcher(watcher))
                                }

                                view.showResults(watchersList)

                                // pagination in case there are more results
                                view.hasNext(watchers.pageInfo().hasNextPage())
                                endCursor = watchers.pageInfo().endCursor() ?: ""
                            }
                        }
        )
    }

    /*fun loadMore() {
        disposeBag.add(
                interactor.loadMore(keyword, endCursor)
                        .subscribeOn(scheduler.io)
                        .observeOn(scheduler.main)
                        .subscribeBy {
                            handleSearchResponse(it.data()?.search()!!)
                        }
        )
    }*/

    private fun parseWatcher(watcher: WatcherQuery.Node): Watcher {
        return Watcher(watcher.avatarUrl().toString(), watcher.name() ?: "")
    }

    fun unbind() {
        disposeBag.clear()
    }
}