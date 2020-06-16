package app.com.githubexplorer.network.model

import java.io.Serializable

data class Watcher(
        val avatarUrl: String,
        val name: String
) : Serializable