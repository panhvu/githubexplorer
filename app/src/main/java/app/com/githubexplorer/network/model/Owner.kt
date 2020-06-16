package app.com.githubexplorer.network.model

import java.io.Serializable

data class Owner (
        val login: String,
        val avatarUrl: String
): Serializable