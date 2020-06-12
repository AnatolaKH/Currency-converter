package akh.data.di.module

import akh.core.app.AppPresentationStorage
import akh.data.app.AppPresentationStorageImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface AppToolsModule {

    @Binds
    @Singleton
    fun AppPresentationStorage(AppPresentationStorageImpl: AppPresentationStorageImpl): AppPresentationStorage
}