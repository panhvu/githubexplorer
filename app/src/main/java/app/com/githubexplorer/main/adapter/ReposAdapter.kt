package app.com.githubexplorer.main.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import app.com.githubexplorer.R
import app.com.githubexplorer.RepositoryQuery

/**
 * Created by panhvu on 25.11.18.
 */

class ReposAdapter(
        var reposList: MutableList<RepositoryQuery.AsRepository>,
        val context: Context
) : RecyclerView.Adapter<ReposViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            ReposViewHolder(
                    context,
                    LayoutInflater.from(parent.context).inflate(R.layout.list_item_repo, parent, false)
            )

    override fun onBindViewHolder(holder: ReposViewHolder, position: Int) {
        holder.bindItems(reposList[position])
    }

    override fun getItemCount(): Int {
        return reposList.size
    }
}