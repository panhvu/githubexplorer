package app.com.githubexplorer.network.model

import app.com.githubexplorer.RepositoryQuery
import app.com.githubexplorer.WatcherQuery

fun RepositoryQuery.Search.toUIModel(): List<Repository> {
    val reposList = mutableListOf<Repository>()
    // get results and pass to view
    val repos = this.repositories() as MutableList<RepositoryQuery.AsRepository>
    for (repo in repos) {
        reposList.add(parseRepo(repo))
    }
    return reposList.toList()
}

fun parseRepo(repo: RepositoryQuery.AsRepository): Repository {
    return Repository(parseOwner(repo), repo.name(),
            repo.description() ?: "", repo.forkCount(),
            repo.stargazers().totalCount(), repo.watchers().totalCount())
}

fun parseOwner(repo: RepositoryQuery.AsRepository): Owner {
    return Owner(repo.owner().login(), repo.owner().avatarUrl().toString())
}

fun WatcherQuery.Watchers.toUIModel(): List<Watcher> {
    val watcherList = mutableListOf<Watcher>()
    for (watcher in this.nodes()!!) {
        watcherList.add(parseWatcher(watcher))
    }
    return watcherList.toList()
}

fun parseWatcher(watcher: WatcherQuery.Node): Watcher {
    return Watcher(watcher.avatarUrl().toString(), watcher.name() ?: "")
}