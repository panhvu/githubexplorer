package app.com.githubexplorer.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import app.com.githubexplorer.App
import app.com.githubexplorer.R
import app.com.githubexplorer.detail.DetailActivity
import app.com.githubexplorer.main.adapter.ReposAdapter
import app.com.githubexplorer.network.model.Repository
import app.com.githubexplorer.utils.OnBottomReachedListener
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(), ReposAdapter.RepoOnClickListener, OnBottomReachedListener {

    private val viewModel by viewModels<SearchViewModel> { viewModelFactory }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var adapter: ReposAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        (application as App).component.inject(this)

        repos_list_view.layoutManager = LinearLayoutManager(this)
        adapter = ReposAdapter(mutableListOf(), this, this, this)
        repos_list_view.adapter = adapter

        viewModel.data.observe(this, Observer { repositories ->
            if (repos_list_view.visibility == View.GONE) {
                repos_list_view.visibility = View.VISIBLE
                empty_view.visibility = View.GONE
            }
            if (load_more_view.visibility == View.VISIBLE) {
                load_more_view.visibility = View.GONE
            }
            adapter.reposList = repositories.toMutableList()
            adapter.notifyDataSetChanged()
        })

        viewModel.uiEvents.observe(this, Observer { event ->
            handleUIEvent(event.getContentIfNotHandled())
        })
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
                query?.let { viewModel.searchRepositories(it) }
                return false
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

    override fun onRepoClicked(repo: Repository) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("repo", repo)
        startActivity(intent)
    }

    override fun onBottomReached() {
        viewModel.onBottomReached()
    }

    private fun handleUIEvent(event: SearchUIEvent?) {
        when (event) {
            is SearchUIEvent.Empty-> {
                if (empty_view.visibility == View.GONE) {
                    empty_view.visibility = View.VISIBLE
                    repos_list_view.visibility = View.GONE
                }
                if (load_more_view.visibility == View.VISIBLE) {
                    load_more_view.visibility = View.GONE
                }
            }
            is SearchUIEvent.HasMoreData -> {
                if (event.flag)
                    load_more_view.visibility = View.VISIBLE
                else
                    load_more_view.visibility = View.GONE
            }
        }
    }
}
