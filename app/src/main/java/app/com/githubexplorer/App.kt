package app.com.githubexplorer

import android.app.Application
import app.com.githubexplorer.di.AppComponent
import app.com.githubexplorer.di.AppModule
import app.com.githubexplorer.di.DaggerAppComponent

class App : Application() {

    val component: AppComponent by lazy {
        DaggerAppComponent
                .builder()
                .appModule(AppModule(this))
                .build()
    }

    override fun onCreate() {
        super.onCreate()
        component.inject(this)
    }
}