package app.com.githubexplorer.main.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import app.com.githubexplorer.RepositoryQuery
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_item_repo.view.*

/**
 * Created by panhvu on 25.11.18.
 */

class ReposViewHolder(val context: Context, view: View) : RecyclerView.ViewHolder(view) {
    val owner = view.owner_img
    val name = view.repo_name
    val description = view.repo_description
    val forkCount = view.fork_count

    fun bindItems(repo: RepositoryQuery.AsRepository) {
        Picasso.get().load(repo.owner().avatarUrl()).into(owner)
        name.text = repo.name()
        description.text = repo.description()
        forkCount.text = "${repo.forkCount()}"
    }
}
