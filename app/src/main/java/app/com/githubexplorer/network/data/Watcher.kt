package app.com.githubexplorer.network.data

import java.io.Serializable

/**
 * Created by panhvu on 26.11.18.
 */

data class Watcher(
        val avatarUrl: String,
        val name: String
) : Serializable