package akh.core.di.data

import akh.core.app.AppPresentationStorage

interface AppPresentationToolsProvider {

    fun provideAppPresentationStorage(): AppPresentationStorage
}