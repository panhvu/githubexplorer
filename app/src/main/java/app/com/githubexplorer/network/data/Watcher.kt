package app.com.githubexplorer.network.data

import java.io.Serializable

data class Watcher(
        val avatarUrl: String,
        val name: String
) : Serializable