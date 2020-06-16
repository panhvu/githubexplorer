package app.com.githubexplorer.di

import app.com.githubexplorer.App
import app.com.githubexplorer.detail.DetailActivity
import app.com.githubexplorer.main.MainActivity
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(
    modules = [
        AppModule::class,
        ViewModelsModule::class
    ]
)


interface AppComponent {
    val app: App

    fun inject(app: App)
    fun inject(mainActivity: MainActivity)
    fun inject(detailActivity: DetailActivity)
}