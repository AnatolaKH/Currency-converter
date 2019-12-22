package akh.presentation.di.module

import akh.presentation.di.MainActivityScope
import akh.presentation.ui.features.splash.SplashActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.android.support.AndroidSupportInjectionModule

@Suppress("unused")
@Module(includes = [AndroidSupportInjectionModule::class])
interface SplashActivityModule {

    @MainActivityScope
    @ContributesAndroidInjector
    fun splashActivityInjector(): SplashActivity

}