package app.com.githubexplorer.network

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by panhvu on 26.11.18.
 */

interface SchedulerProvider {
    val main: Scheduler
    val io: Scheduler

    companion object {
        val default: SchedulerProvider = object : SchedulerProvider {
            override val main = AndroidSchedulers.mainThread()
            override val io = Schedulers.io()
        }
    }
}