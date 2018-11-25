package app.com.githubexplorer.main

import app.com.githubexplorer.RepositoryQuery
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
                            view.showResults(it.data()?.search()?.repositories() as MutableList<RepositoryQuery.AsRepository>)
                        }
        )
    }

    fun unbind() {
        disposeBag.clear()
    }
}