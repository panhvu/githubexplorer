package app.com.githubexplorer.detail

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import app.com.githubexplorer.App
import app.com.githubexplorer.R
import app.com.githubexplorer.detail.adapter.WatchersAdapter
import app.com.githubexplorer.network.model.Repository
import app.com.githubexplorer.utils.OnBottomReachedListener
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.activity_detail.load_more_view
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class DetailActivity : AppCompatActivity(), OnBottomReachedListener {

    private val viewModel by viewModels<DetailViewModel> { viewModelFactory }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var adapter: WatchersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        (application as App).component.inject(this)

        val repo = intent?.extras?.get("repo") as Repository

        name.text = repo.name
        description.text = repo.description
        fork_count.text = "${repo.forkCount}"
        stargazers.text = "${repo.stargazers}"
        watchers.text = "${repo.watchersCount}"

        watchers_list_view.layoutManager = LinearLayoutManager(this)
        adapter = WatchersAdapter(emptyList(), this, this)
        watchers_list_view.adapter = adapter

        viewModel.data.observe(this, Observer { watchers ->
            if (load_more_view.visibility == View.VISIBLE) {
                load_more_view.visibility = View.GONE
            }

            adapter.watchersList = watchers
            adapter.notifyDataSetChanged()
        })

        viewModel.uiEvents.observe(this, Observer { event ->
            handleUIEvent(event.getContentIfNotHandled())
        })

        viewModel.loadData(repo.owner.login, repo.name)
    }

    private fun handleUIEvent(event: DetailUIEvent?) {
        when (event) {
            is DetailUIEvent.Empty-> {
                if (empty_view.visibility == View.GONE) {
                    empty_view.visibility = View.VISIBLE
                    repos_list_view.visibility = View.GONE
                }
                if (load_more_view.visibility == View.VISIBLE) {
                    load_more_view.visibility = View.GONE
                }
            }
            is DetailUIEvent.HasMoreData -> {
                if (event.flag)
                    load_more_view.visibility = View.VISIBLE
                else
                    load_more_view.visibility = View.GONE
            }
        }
    }

    override fun onBottomReached() {
        viewModel.onBottomReached()
    }
}
