package akh.data.di.module.db

import akh.core.repository.SecretDBRepository
import akh.data.db.SecretDBRepositoryImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton


@Suppress("unused")
@Module
interface SecretDBModule {

    @Binds
    @Singleton
    fun secretDBRepository(secretDBRepositoryImpl: SecretDBRepositoryImpl): SecretDBRepository
}