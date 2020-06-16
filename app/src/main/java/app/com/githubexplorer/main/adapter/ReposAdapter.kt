package app.com.githubexplorer.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.com.githubexplorer.R
import app.com.githubexplorer.network.model.Repository
import app.com.githubexplorer.utils.OnBottomReachedListener

class ReposAdapter(
        var reposList: MutableList<Repository>,
        val context: Context,
        private val onClickListener: RepoOnClickListener,
        private val onBottomReachedListener: OnBottomReachedListener
) : RecyclerView.Adapter<ReposViewHolder>() {

    interface RepoOnClickListener {
        fun onRepoClicked(repo: Repository)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            ReposViewHolder(
                    context,
                    LayoutInflater.from(parent.context).inflate(R.layout.list_item_repo, parent, false)
            )

    override fun onBindViewHolder(holder: ReposViewHolder, position: Int) {
        holder.bindItems(reposList[position], onClickListener)
        if (position == itemCount-1) {
            onBottomReachedListener.onBottomReached()
        }
    }

    override fun getItemCount(): Int {
        return reposList.size
    }
}