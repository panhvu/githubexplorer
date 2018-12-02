package app.com.githubexplorer.detail

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import app.com.githubexplorer.R
import app.com.githubexplorer.main.adapter.WatchersAdapter
import app.com.githubexplorer.network.data.Repository
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

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
        val adapter = WatchersAdapter(repo.watchers, this)
        watchers_list_view.adapter = adapter
    }

}
