package akh.core.di

import akh.core.app.App

interface MainToolsProvider {

    fun provideContext(): App

}