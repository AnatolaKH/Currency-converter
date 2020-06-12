package akh.core.di.presentation

import akh.core.app.AppThemeProvider

interface AppThemeToolsProvider {

    fun provideAppThemeProvider(): AppThemeProvider
}