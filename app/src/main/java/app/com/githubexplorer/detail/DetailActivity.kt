package app.com.githubexplorer.detail

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import app.com.githubexplorer.R
import app.com.githubexplorer.data.Repository
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
        watchers.text = "${repo.watchers}"
    }

}
