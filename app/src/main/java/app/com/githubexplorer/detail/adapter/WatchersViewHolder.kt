package app.com.githubexplorer.main.adapter

import android.content.Context
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.View
import app.com.githubexplorer.network.data.Watcher
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_item_repo.view.*

/**
 * Created by panhvu on 25.11.18.
 */

class WatchersViewHolder(val context: Context, view: View) : RecyclerView.ViewHolder(view) {
    val owner = view.owner_img
    val name = view.repo_name

    fun bindItems(watcher: Watcher) {
        Picasso.get().load(Uri.parse(watcher.avatarUrl)).into(owner)
        name.text = watcher.name
    }
}
