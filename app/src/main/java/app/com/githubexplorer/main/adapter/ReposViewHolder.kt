package app.com.githubexplorer.main.adapter

import android.content.Context
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.View
import app.com.githubexplorer.network.data.Repository
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_item_repo.view.*

class ReposViewHolder(val context: Context, view: View) : RecyclerView.ViewHolder(view) {
    val owner = view.owner_img
    val name = view.repo_name
    val description = view.repo_description
    val forkCount = view.fork_count

    fun bindItems(repo: Repository, listener: ReposAdapter.RepoOnClickListener) {
        Picasso.get().load(Uri.parse(repo.owner.avatarUrl)).into(owner)
        name.text = repo.name
        description.text = repo.description
        forkCount.text = "${repo.forkCount}"
        itemView.setOnClickListener { listener.onRepoClicked(repo)}
    }
}
