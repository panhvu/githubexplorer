package app.com.githubexplorer.data

import java.io.Serializable
import java.util.*

/**
 * Created by panhvu on 26.11.18.
 */

data class Repository(
        val ownerUrl: String,
        val name: String,
        val description: String,
        val forkCount: Int,
        val createdAt: Date,
        val stargazers: Int,
        val watchers: Int
) : Serializable