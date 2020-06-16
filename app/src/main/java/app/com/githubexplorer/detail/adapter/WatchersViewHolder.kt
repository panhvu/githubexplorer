package app.com.githubexplorer.detail.adapter

import android.content.Context
import android.net.Uri
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import app.com.githubexplorer.network.model.Watcher
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_item_repo.view.*

class WatchersViewHolder(val context: Context, view: View) : RecyclerView.ViewHolder(view) {
    val owner = view.owner_img
    val name = view.repo_name

    fun bindItems(watcher: Watcher) {
        Picasso.get().load(Uri.parse(watcher.avatarUrl)).into(owner)
        name.text = watcher.name
    }
}
