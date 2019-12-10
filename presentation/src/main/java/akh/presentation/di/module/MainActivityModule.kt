package akh.presentation.di.module

import akh.presentation.ui.features.MainActivity
import akh.presentation.di.MainActivityScope
import akh.presentation.di.module.mvvm.ViewModelFactoryModule
import akh.presentation.di.module.mvvm.ViewModelModule
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.android.support.AndroidSupportInjectionModule


@Suppress("unused")
@Module(
    includes = [
        AndroidSupportInjectionModule::class,
        ViewModelFactoryModule::class,
        ViewModelModule::class]
)
interface MainActivityModule {

    @MainActivityScope
    @ContributesAndroidInjector(modules = [MainActivityFragmentModule::class])
    fun mainActivityInjector(): MainActivity
}