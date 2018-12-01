package app.com.githubexplorer.main.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import app.com.githubexplorer.R
import app.com.githubexplorer.network.Repository
import app.com.githubexplorer.network.Watcher

/**
 * Created by panhvu on 25.11.18.
 */

class WatchersAdapter(
        var watchersList: List<Watcher>,
        val context: Context
) : RecyclerView.Adapter<WatchersViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            WatchersViewHolder(
                    context,
                    LayoutInflater.from(parent.context).inflate(R.layout.list_item_watcher, parent, false)
            )

    override fun onBindViewHolder(holder: WatchersViewHolder, position: Int) {
        holder.bindItems(watchersList[position])
    }

    override fun getItemCount(): Int {
        return watchersList.size
    }
}