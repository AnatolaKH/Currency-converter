package akh.core.di.data

import akh.core.repository.SecretDBRepository

interface SecretDBToolsProvider {

    fun provideSecretDBRepository(): SecretDBRepository

}