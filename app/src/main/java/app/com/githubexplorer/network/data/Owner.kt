package app.com.githubexplorer.network.data

import java.io.Serializable

data class Owner (
        val login: String,
        val avatarUrl: String
): Serializable