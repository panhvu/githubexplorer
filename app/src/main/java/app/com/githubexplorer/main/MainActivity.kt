package app.com.githubexplorer.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import app.com.githubexplorer.R
import app.com.githubexplorer.detail.DetailActivity
import app.com.githubexplorer.main.adapter.ReposAdapter
import app.com.githubexplorer.network.Dependencies
import app.com.githubexplorer.network.SchedulerProvider
import app.com.githubexplorer.network.model.Repository
import app.com.githubexplorer.uiutils.OnBottomReachedListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), MainView, ReposAdapter.RepoOnClickListener, OnBottomReachedListener {

    private lateinit var presenter: MainPresenter
    private lateinit var adapter: ReposAdapter
    private var hasNext = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        presenter = MainPresenter(SchedulerProvider.default, Dependencies().graphQLRepository, this)

        repos_list_view.layoutManager = LinearLayoutManager(this)
        adapter = ReposAdapter(mutableListOf(), this, this, this)
        repos_list_view.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_main_menu, menu)
        val searchItem = menu?.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { presenter.searchRepositories(it) }
                return false
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

    override fun onPause() {
        presenter.unbind()
        super.onPause()
    }

    override fun onRepoClicked(repo: Repository) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("repo", repo)
        startActivity(intent)
    }

    override fun onBottomReached() {
        if (hasNext) {
            load_more_view.visibility = View.VISIBLE
            presenter.loadMore()
        } else {
            load_more_view.visibility = View.GONE
        }
    }

    override fun showResults(results: MutableList<Repository>) {
        if (repos_list_view.visibility == View.GONE) {
            repos_list_view.visibility = View.VISIBLE
            empty_view.visibility = View.GONE
        }
        if (load_more_view.visibility == View.VISIBLE) {
            load_more_view.visibility = View.GONE
        }

        adapter.reposList = results
        adapter.notifyDataSetChanged()
    }

    override fun showEmptyMessage() {
        if (empty_view.visibility == View.GONE) {
            empty_view.visibility = View.VISIBLE
            repos_list_view.visibility = View.GONE
        }
        if (load_more_view.visibility == View.VISIBLE) {
            load_more_view.visibility = View.GONE
        }
    }

    override fun hasNext(flag: Boolean) {
        hasNext = flag
    }
}
