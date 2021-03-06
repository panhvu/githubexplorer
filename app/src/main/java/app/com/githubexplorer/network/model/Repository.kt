package app.com.githubexplorer.network.model

import java.io.Serializable

data class Repository(
        val owner: Owner,
        val name: String,
        val description: String,
        val forkCount: Int,
        val stargazers: Int,
        val watchersCount: Int
) : Serializable