package app.com.githubexplorer.network.data

import java.io.Serializable

/**
 * Created by panhvu on 02.12.18.
 */
data class Owner (
        val login: String,
        val avatarUrl: String
): Serializable