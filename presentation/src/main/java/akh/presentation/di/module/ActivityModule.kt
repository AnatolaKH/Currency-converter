package akh.presentation.di.module

import dagger.Module

@Suppress("unused")
@Module(
    includes = [
        MainActivityModule::class,
        SplashActivityModule::class
    ]
)
interface ActivityModule
