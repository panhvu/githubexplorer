package app.com.githubexplorer.network.data

import java.io.Serializable

/**
 * Created by panhvu on 26.11.18.
 */

data class Repository(
        val owner: Owner,
        val name: String,
        val description: String,
        val forkCount: Int,
        val stargazers: Int,
        val watchersCount: Int
) : Serializable