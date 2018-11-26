package app.com.githubexplorer.main

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.View
import app.com.githubexplorer.R
import app.com.githubexplorer.data.Dependencies
import app.com.githubexplorer.data.Repository
import app.com.githubexplorer.detail.DetailActivity
import app.com.githubexplorer.main.adapter.ReposAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), MainView, ReposAdapter.RepoOnClickListener {

    private lateinit var presenter: MainPresenter
    private lateinit var adapter: ReposAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        presenter = MainPresenter(this, Dependencies().mainInteractor)

        repos_list_view.layoutManager = LinearLayoutManager(this)
        adapter = ReposAdapter(mutableListOf(), this, this)
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

    override fun showResults(results: MutableList<Repository>) {
        if (repos_list_view.visibility == View.GONE) {
            repos_list_view.visibility = View.VISIBLE
            empty_view.visibility = View.GONE
        }
        adapter.reposList = results
        adapter.notifyDataSetChanged()
    }

    override fun showEmptyMessage() {
        if (empty_view.visibility == View.GONE) {
            empty_view.visibility = View.VISIBLE
            repos_list_view.visibility = View.GONE
        }
    }

    override fun onRepoClicked(repo: Repository) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("repo", repo)
        startActivity(intent)
    }

    override fun onPause() {
        presenter.unbind()
        super.onPause()
    }
}
