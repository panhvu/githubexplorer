package app.com.githubexplorer.network

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
        val watchers: Int
) : Serializable