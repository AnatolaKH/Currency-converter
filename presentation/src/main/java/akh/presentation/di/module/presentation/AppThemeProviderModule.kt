package akh.presentation.di.module.presentation

import akh.core.app.AppThemeProvider
import akh.presentation.common.theme.AppThemeProviderImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface AppThemeProviderModule {

    @Binds
    @Singleton
    fun appThemeProviderImpl(appThemeProvider: AppThemeProviderImpl): AppThemeProvider
}