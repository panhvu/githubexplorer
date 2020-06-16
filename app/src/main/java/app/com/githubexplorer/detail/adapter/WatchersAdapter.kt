package app.com.githubexplorer.detail.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.com.githubexplorer.R
import app.com.githubexplorer.network.model.Watcher
import app.com.githubexplorer.utils.OnBottomReachedListener

class WatchersAdapter(
        var watchersList: List<Watcher>,
        val context: Context,
        val onBottomReachedListener: OnBottomReachedListener
) : RecyclerView.Adapter<WatchersViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            WatchersViewHolder(
                    context,
                    LayoutInflater.from(parent.context).inflate(R.layout.list_item_watcher, parent, false)
            )

    override fun onBindViewHolder(holder: WatchersViewHolder, position: Int) {
        holder.bindItems(watchersList[position])
        if (position == itemCount-1) {
            onBottomReachedListener.onBottomReached()
        }
    }

    override fun getItemCount(): Int {
        return watchersList.size
    }
}