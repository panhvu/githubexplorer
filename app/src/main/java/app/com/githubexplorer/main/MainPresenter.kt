package app.com.githubexplorer.main

import app.com.githubexplorer.RepositoryQuery
import app.com.githubexplorer.data.Repository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

/**
 * Created by panhvu on 25.11.18.
 */

class MainPresenter(
        private val view: MainView,
        private val interactor: MainInteractor
) {

    private val disposeBag = CompositeDisposable()

    fun searchRepositories(keyword: String) {
        disposeBag.add(
                interactor.getRepositories(keyword)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeBy {
                            if(it.data()?.search()?.repositoryCount()!! > 0) {
                                val repos = it.data()?.search()?.repositories() as MutableList<RepositoryQuery.AsRepository>
                                val resultList = mutableListOf<Repository>()
                                var r: Repository
                                for (repo in repos) {
                                    r = Repository(repo.owner().avatarUrl().toString(), repo.name(), repo.description() ?: "", repo.forkCount(),
                                            repo.createdAt(), repo.stargazers().totalCount(), repo.watchers().totalCount())
                                    resultList.add(r)
                                }
                                view.showResults(resultList)
                            }
                            else {
                                view.showEmptyMessage()
                            }
                        }


        )
    }

    fun unbind() {
        disposeBag.clear()
    }
}