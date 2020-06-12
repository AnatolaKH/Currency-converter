package akh.data.app

import akh.core.app.AppPresentationStorage
import akh.core.app.CurrentTheme
import akh.core.repository.SecretDBRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppPresentationStorageImpl @Inject constructor(
    private val secretDBRepository: SecretDBRepository
) : AppPresentationStorage {

    override fun getCurrentTheme(): CurrentTheme =
        CurrentTheme.values()[secretDBRepository.getTheme()]

    override fun putCurrentTheme(theme: CurrentTheme) =
        secretDBRepository.putTheme(theme.ordinal)

}