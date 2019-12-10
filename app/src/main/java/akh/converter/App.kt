package akh.converter

import akh.core.App
import akh.converter.common.TimberReleaseTree
import akh.converter.di.AppComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import timber.log.Timber

class App : DaggerApplication(), App {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> = appComponent

    private lateinit var appComponent: AppComponent

    override fun onCreate() {
        initAppComponent()
        super.onCreate()
        initTimber()
    }

    private fun initAppComponent() {
        appComponent = createAppComponent()
        appComponent.inject(this)
    }

    private fun createAppComponent() = AppComponent.Initializer.init(this)

    private fun initTimber() {
        if (BuildConfig.DEBUG)
            Timber.plant(Timber.DebugTree())
        else Timber.plant(TimberReleaseTree())
    }

}