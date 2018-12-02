package app.com.githubexplorer.network.data

import java.io.Serializable

/**
 * Created by panhvu on 26.11.18.
 */

data class Repository(
        val ownerUrl: String,
        val name: String,
        val description: String,
        val forkCount: Int,
        val stargazers: Int,
        val watchersCount: Int,
        val watchers: List<Watcher>
) : Serializable