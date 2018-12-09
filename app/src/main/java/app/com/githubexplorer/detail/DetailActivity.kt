package app.com.githubexplorer.detail

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import app.com.githubexplorer.R
import app.com.githubexplorer.main.adapter.WatchersAdapter
import app.com.githubexplorer.network.Dependencies
import app.com.githubexplorer.network.SchedulerProvider
import app.com.githubexplorer.network.data.Repository
import app.com.githubexplorer.network.data.Watcher
import app.com.githubexplorer.uiutils.OnBottomReachedListener
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity(), DetailView, OnBottomReachedListener {

    private lateinit var presenter: DetailPresenter
    private lateinit var adapter: WatchersAdapter
    private var hasNext = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val repo = intent.extras.get("repo") as Repository

        name.text = repo.name
        description.text = repo.description
        fork_count.text = "${repo.forkCount}"
        stargazers.text = "${repo.stargazers}"
        watchers.text = "${repo.watchersCount}"

        watchers_list_view.layoutManager = LinearLayoutManager(this)
        adapter = WatchersAdapter(emptyList(), this, this)
        watchers_list_view.adapter = adapter

        presenter = DetailPresenter(SchedulerProvider.default, Dependencies().graphQLRepository, this)
        presenter.getWatchers(repo.owner.login, repo.name)

    }

    override fun onPause() {
        presenter.unbind()
        super.onPause()
    }

    override fun onBottomReached() {
        if (hasNext) {
            load_more_view.visibility = View.VISIBLE
            presenter.loadMore()
        } else {
            load_more_view.visibility = View.GONE
        }
    }

    override fun showResults(results: MutableList<Watcher>) {
        if (load_more_view.visibility == View.VISIBLE) {
            load_more_view.visibility = View.GONE
        }

        adapter.watchersList = results
        adapter.notifyDataSetChanged()
    }

    override fun hasNext(flag: Boolean) {
        hasNext = flag
    }
}
