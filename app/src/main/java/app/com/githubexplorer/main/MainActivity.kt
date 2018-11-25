package app.com.githubexplorer.main

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.Menu
import app.com.githubexplorer.R
import app.com.githubexplorer.RepositoryQuery
import app.com.githubexplorer.di.Dependencies
import app.com.githubexplorer.main.adapter.ReposAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), MainView {

    private lateinit var presenter: MainPresenter
    private lateinit var adapter: ReposAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        presenter = MainPresenter(this, Dependencies().mainInteractor)

        repos_list_view.layoutManager = LinearLayoutManager(this)
        adapter = ReposAdapter(mutableListOf(), this)
        repos_list_view.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_main_menu, menu)
        val searchItem = menu?.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                //ToDo
                return false
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { presenter.searchRepositories(it) }
                return false
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

    override fun showResults(results: MutableList<RepositoryQuery.AsRepository>) {
        adapter.reposList = results
        adapter.notifyDataSetChanged()
    }

    override fun onPause() {
        presenter.unbind()
        super.onPause()
    }
}
